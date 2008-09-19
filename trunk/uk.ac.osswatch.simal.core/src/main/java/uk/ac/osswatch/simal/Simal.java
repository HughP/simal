package uk.ac.osswatch.simal;

/*
 * Copyright 2007 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryFactory;
import uk.ac.osswatch.simal.tools.Ohloh;
import uk.ac.osswatch.simal.tools.PTSWImport;

/**
 * The Command Line Interface for a Simal repository.
 * 
 */
public class Simal {
  private static final Logger logger = LoggerFactory.getLogger(Simal.class);
  private static ISimalRepository repository;

  public Simal() throws SimalRepositoryException {
  }

  /**
   * @param args
   * @throws SimalRepositoryException
   * @throws ParseException
   */
  @SuppressWarnings( { "unchecked", "static-access" })
  public static void main(String[] args) throws SimalRepositoryException {
    try {
      repository = SimalRepositoryFactory.getInstance();
    } catch (SimalRepositoryException e) {
      throw new IllegalStateException("Unable to create repository", e);
    }

    Options opts = new Options();

    Option help = new Option("h", "help", false,
        "Print help for the Simal command line interface adn exit");
    opts.addOption(help);

    Option version = new Option("v", "version", false,
        "Print the version number of Simal and exit");
    opts.addOption(version);

    Option directory = new Option("d", "dir", true,
        "The directory in which the persistent data store resides");
    opts.addOption(directory);

    Option properties = new Option("p", "properties", true,
        "The properties file to use");
    opts.addOption(properties);

    Option test = new Option(
        "t",
        "test",
        false,
        "Start the repository in test mode, the repository is populated with some test data, all changes are lost when the CLI commands have been executed");
    opts.addOption(test);

    Option property = OptionBuilder.withArgName("property=value").hasArg()
        .withValueSeparator().withDescription("use value for given property")
        .create("D");
    opts.addOption(property);

    PosixParser parser = new PosixParser();
    CommandLine cl = null;
    try {
      cl = parser.parse(opts, args);
    } catch (ParseException e) {
      logger.error(e.getMessage());
      logger
          .error("Please use the \"--help\" option to see a list of valid commands and options");
      System.exit(1);
    }

    PropertyConfigurator
        .configure(Simal.class.getResource("/log4j.properties"));

    if (cl.hasOption('h')) {
      printHelp(opts);
    } else {
      Iterator<Option> options = cl.iterator();
      Option opt;
      while (options.hasNext()) {
        opt = options.next();
        if (opt.equals(test)) {
          try {
            logger.info("Setting repository to test mode");
            repository.setIsTest(true);
          } catch (SimalRepositoryException e) {
            logger.error("Weird, that shouldn't happen...", e);
            System.exit(1);
          }
        }
      }

      String propsPath = cl.getOptionValue("properties");
      if (propsPath != null && propsPath.length() > 0) {
        File file;
        if (!(propsPath.indexOf(":") < 0)
            || propsPath.startsWith(File.separator)) {
          file = new File(propsPath);
        } else {
          String workingDir = System.getProperty("user.dir");
          file = new File(workingDir + File.separator + propsPath);
        }
        logger.info("using properties file " + file.toString());
        SimalProperties.setLocalPropertiesFile(file);
      }

      if (cl.hasOption('v')) {
        printVersion();
      }

      String dir = cl.getOptionValue("dir");
      logger.info("Setting database directory to " + dir);

      logger.info("Initialising repository...");
      initRepository(dir);
      logger.info("Executing commands...");

      String[] cmds = cl.getArgs();
      String cmd;
      for (int i = 0; i < cmds.length; i++) {
        cmd = cmds[i];
        if (cmd.equals("addxml")) {
          addXMLFile((String) cmds[i + 1]);
          i++;
        } else if (cmd.equals("addxmldir")) {
          try {
            repository.addXMLDirectory((String) cmds[i + 1]);
          } catch (SimalRepositoryException e) {
            logger.error("Error adding data - adborting", e);
            System.exit(1);
          }
          i++;
        } else if (cmd.equals("writexml")) {
          writeXML((String) cmds[i + 1]);
          i++;
        } else if (cmd.equals("importPTSW")) {
          try {
            importPTSW();
          } catch (SimalException e) {
            logger.error(
                "Unable to Import from PTSW: " + e.getMessage() + "\n", e);
            System.exit(1);
          }
          i++;
        } else if (cmd.equals("importOhloh")) {
          try {
            importOhloh((String) cmds[i + 1]);
          } catch (SimalException e) {
            logger.error("Unable to Import from Ohloh: " + e.getMessage()
                + "\n", e);
            System.exit(1);
          }
          i++;
        } else {
          logger.info("Ignoring unrecognised command: " + cmd);
        }
      }
    }
  }

  /**
   * Import a project from Ohloh
   * 
   * @param id
   */
  private static void importOhloh(String id) throws SimalException {
    Ohloh ohloh = new Ohloh();
    ohloh.addProjectToSimal(id);
  }

  /**
   * Import all the documents updated since the last time we updated from PTSW.
   * 
   * @throws SimalException
   */
  private static void importPTSW() throws SimalException {
    PTSWImport importer = new PTSWImport();
    Document pings = importer.getLatestPingsAsRDF();
    OutputFormat format = new OutputFormat(pings);
    StringWriter writer = new StringWriter();
    XMLSerializer serial = new XMLSerializer(writer, format);
    try {
      serial.serialize(pings);
    } catch (IOException e) {
      throw new SimalException("Unable to serialize PTSW response");
    }
    logger.info("Updated DOAP documents:\n");
    logger.info(writer.toString());

    File tmpFile = new File(System.getProperty("java.io.tmpdir")
        + File.separator + "PTSWExport.xml");
    FileWriter fw = null;
    try {
      fw = new FileWriter(tmpFile);
      fw.write(writer.toString());
    } catch (IOException e) {
      throw new SimalException(
          "Unable to write PTSW export file to temporary space");
    } finally {
      if (fw != null) {
        try {
          fw.close();
        } catch (IOException e) {
          throw new RuntimeException("Attempt to close a file writer failed", e);
        }
      }
    }

    try {
      repository.addProject(tmpFile.toURI().toURL(), tmpFile.toURI().toURL()
          .toExternalForm());
    } catch (MalformedURLException e) {
      throw new SimalException("Unable to add projects from PTSW Export", e);
    }
  }

  /**
   * Write an XML file for the given QName to the log.
   * 
   * @param qname
   */
  private static void writeXML(final String uri) {
    logger.info("Writing XML for " + uri);
    try {
      IResource resource = repository.getResource(uri);
      logger.info(resource.toXML());
    } catch (SimalRepositoryException e) {
      logger.error("Unable to write XML to standard out");
      System.exit(1);
    }
  }

  /**
   * Add an RDF/XML file to the repository.
   */
  private static void addXMLFile(final String filenameOrURL) {
    logger.info("Adding XML from " + filenameOrURL);

    try {
      URL fileURL;
      if (!filenameOrURL.contains(":/")) {
        if (filenameOrURL.startsWith("/") || filenameOrURL.indexOf(':') == 1) {
          fileURL = new File(filenameOrURL).toURI().toURL();
        } else {
          fileURL = new File(System.getProperty("user.dir") + File.separator
              + filenameOrURL).toURI().toURL();
        }
      } else {
        fileURL = new URL(filenameOrURL);
      }
      try {
        repository.addProject(fileURL, "");
      } catch (SimalException e) {
        logger.error("Unable to add an RDF/XML documet {}", fileURL
            .toExternalForm(), e);
        System.exit(1);
      }
    } catch (MalformedURLException e) {
      logger.error("The URL specified in --addxml  is not a valid URL.");
      System.exit(1);
    }
    logger.info("Data added.");
  }

  private static void initRepository(String dir) {
    try {
      if (dir == null || dir.equals("")) {
        repository.initialise();
      } else {
        repository.initialise(dir);
      }
    } catch (SimalRepositoryException e) {
      logger.error("Unable to start repository: {}", e.getMessage());
      e.printStackTrace();
    }
  }

  private static void printVersion() {
    String version;
    try {
      version = SimalProperties
          .getProperty(SimalProperties.PROPERTY_SIMAL_VERSION);
    } catch (SimalRepositoryException e) {
      version = "ERROR: unable to read properties file";
    }
    logger.info("Simal version   : " + version);
    logger.info("Java version    : " + System.getProperty("java.version"));
    logger.info("Java vendor     : " + System.getProperty("java.vendor"));
    logger.info("OS              : " + System.getProperty("os.name"));
    logger.info("OS Architecture : " + System.getProperty("os.arch"));
    System.exit(0);
  }

  private static void printHelp(Options opts) {
    HelpFormatter f = new HelpFormatter();

    String header = "Options";

    StringBuffer footer = new StringBuffer("\n");
    footer.append("Command      Argument(s)   Description\n\n");
    footer.append("=======      ===========   ===========\n\n");
    footer
        .append("addxml       FILE_OR_URL   add an RDF/XML file to the repository\n");
    footer
        .append("addxmldir    DIRECTORY     add all RDF/XML files found in a directory\n");
    footer
        .append("writexml     URI           retrive the RDF/XML of an entity and write it to standard IO\n");
    footer
        .append("importPTSW                 Import all recently updated DOAP files from PTSW\n");
    footer
        .append("importOhloh  ID            import a project from Ohloh with a given ID.");

    f.printHelp("simal [options] [command [args] [command [args]] ... ]",
        header, opts, footer.toString(), false);
  }

  public static ISimalRepository getRepository() {
    return repository;
  }

}

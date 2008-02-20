package uk.ac.osswatch.simal;

import java.io.File;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.xml.namespace.QName;

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

import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * The Command Line Interface for a Simal repository.
 * 
 */
public class Simal {
	private static final Logger logger = LoggerFactory.getLogger(Simal.class);
	private static final String SIMAL_VERSION = "0.2-SNAPSHOT";

	/**
	 * @param args
	 * @throws ParseException
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public static void main(String[] args) {
		Options opts = new Options();

		Option help = new Option("h", "help", false,
				"Print help for the Simal command line interface adn exit");
		opts.addOption(help);

		Option version = new Option("v", "version", false,
				"Print the version number of Simal and exit");
		opts.addOption(version);

		Option test = new Option("t", "test", false,
				"Start the repository in test mode, the repository is populated with some test data, all changes are lost when the CLI commands have been executed");
		opts.addOption(test);

		Option property = OptionBuilder.withArgName("property=value").hasArg()
				.withValueSeparator().withDescription(
						"use value for given property").create("D");
		opts.addOption(property);

		PosixParser parser = new PosixParser();
		CommandLine cl = null;
		try {
			cl = parser.parse(opts, args);
		} catch (ParseException e) {
			logger.error(e.getMessage());
			logger.error("Please use the \"--help\" option to see a list of valid commands and options");
			System.exit(1);
		}
		
		PropertyConfigurator.configure("log4j.properties");

		if (cl.hasOption('h')) {
			printHelp(opts);
		} else if (cl.hasOption('v')) {
			printVersion();
		} else {
			
			Iterator<Option> options = cl.iterator();
			Option opt;
			while (options.hasNext()) {
				opt = options.next();
				if (opt.equals(test)) {
					try {
						logger.info("Setting repository to test mode");
						SimalRepository.setIsTest(true);
					} catch (SimalRepositoryException e) {
						logger.error("Weird, that shouldn't happen...", e);
						System.exit(1);
					}
				}
			}
			
			logger.info("Initialising repository...");
			initRepository();
			logger.info("Executing commands...");

			String[] cmds = cl.getArgs();
			String cmd;
			for (int i = 0; i < cmds.length; i++) {
				cmd = cmds[i];
				if (cmd.equals("addxml")) {
					addXMLFile((String)cmds[i+1]);
					i++;
				} else if (cmd.equals("writexml")) {
					writeXML(new QName((String)cmds[i+1]));
					i++;
				} else {
					logger.info("Ignoring unrecognised command: " + cmd);
				}
			}
		}
	}

	/**
	 * Write an XML file for the given QName to standard out.
	 * 
	 * @param qname
	 */
	private static void writeXML(final QName qname) {
		logger.info("Writing XML for " + qname);
		try {
			SimalRepository.writeXML(new OutputStreamWriter(System.out), qname);
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
			if (!filenameOrURL.contains("://")) {
				fileURL = new File(System.getProperty("user.dir")
						+ File.separator + filenameOrURL).toURL();
			} else {
				fileURL = new URL(filenameOrURL);
			}
			try {
				SimalRepository.addProject(fileURL, "");
			} catch (SimalRepositoryException e) {
				logger.error("Unable to add an RDF/XML documet {}",
						fileURL.toExternalForm());
				logger.error("Root cause: {}",  e.getMessage());
				System.exit(1);
			}
		} catch (MalformedURLException e) {
			logger.error("The URL specified in --addxml  is not a valid URL.");
			System.exit(1);
		}
		logger.info("Data added.");
	}

	private static void initRepository() {
		try {
			SimalRepository.initialise();
		} catch (SimalRepositoryException e) {
			logger.error("Unable to start repository: {}", e.getMessage());
			e.printStackTrace();
		}
	}

	private static void printVersion() {
		logger.info("Simal version   : " + SIMAL_VERSION);
		logger.info("Java version    : "
				+ System.getProperty("java.version"));
		logger.info("Java vendor     : "
				+ System.getProperty("java.vendor"));
		logger.info("OS              : " + System.getProperty("os.name"));
		logger.info("OS Architecture : " + System.getProperty("os.arch"));
		System.exit(0);
	}

	private static void printHelp(Options opts) {
		HelpFormatter f = new HelpFormatter();
		
		String header = "Options";
		
		StringBuffer footer = new StringBuffer("\n");
		footer.append("Command   Argument(s)   Description\n\n");
		footer.append("=======   ===========   ===========\n\n");
		footer.append("addxml    FILE_OR_URL   add an RDF/XML file to the repository\n");
		footer.append("writexml  QNAME         write RDF/XML to standard out");
		
		f.printHelp("simal [options] [command [args] [command [args]] ... ]", header, opts, footer.toString(), false);
			System.exit(0);
	}

}

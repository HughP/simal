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

import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * The Command Line Interface for a Simal repository.
 * 
 */
public class Simal {

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
				"Start the repository in test mode");
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
			System.out.println(e.getMessage());
			System.out
					.println("Please use the \"--help\" option to see a list of valid commands and options");
			System.exit(1);
		}

		System.out.println("=================================================");
		System.out.println("NOTE: The repository is in test mode by default");
		System.out
				.println("This means all data is volotile and will be deleted");
		System.out.println("after the CLI has completed execution.");
		System.out
				.println("If you want to do something more serious join the ");
		System.out.println("developer mailing and help out.");
		System.out.println("=================================================");
		System.out.println();

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
						System.out.println("Setting repository to test mode");
						SimalRepository.setIsTest(true);
					} catch (SimalRepositoryException e) {
						// should never be thrown since we are controlling when to start repo
						e.printStackTrace();
						System.exit(1);
					}
				}
			}
			
			System.out.println("Initialising repository...");
			initRepository();
			System.out.println("Executing commands...");

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
					System.out.println("Ignoring unrecognised command: " + cmd);
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
		System.out.println("Writing XML for " + qname);
		try {
			SimalRepository.writeXML(new OutputStreamWriter(System.out), qname);
			System.out.println();
		} catch (SimalRepositoryException e) {
			System.out.println("Unable to write XML to standard out");
			System.exit(1);
		}
	}

	/**
	 * Add an RDF/XML file to the repository.
	 */
	private static void addXMLFile(final String filenameOrURL) {
		System.out.println("Adding XML from " + filenameOrURL);

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
				System.out.println("Unable to add an RDF/XML documet "
						+ fileURL.toExternalForm());
				System.out.println("Root cause: " + e.getMessage());
				System.exit(1);
			}
		} catch (MalformedURLException e) {
			System.out
					.println("The URL specified in --addxml  is not a valid URL.");
			System.exit(1);
		}
		System.out.println("Data added.");
	}

	private static void initRepository() {
		try {
			SimalRepository.initialise();
		} catch (SimalRepositoryException e) {
			System.out.println("Unable to start repository: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void printVersion() {
		System.out.println("Simal version   : " + SIMAL_VERSION);
		System.out.println("Java version    : "
				+ System.getProperty("java.version"));
		System.out.println("Java vendor     : "
				+ System.getProperty("java.vendor"));
		System.out
				.println("OS              : " + System.getProperty("os.name"));
		System.out
				.println("OS Architecture : " + System.getProperty("os.arch"));
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

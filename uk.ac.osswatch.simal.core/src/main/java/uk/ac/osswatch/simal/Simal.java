package uk.ac.osswatch.simal;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

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
	public static void main(String[] args) throws ParseException {
		Options opt = new Options();

		opt.addOption("h", "help", false,
				"Print help for the Simal command line interface adn exit");
		opt.addOption("v", "version", false, "Print the version number of Simal and exit");
		
		PosixParser parser = new PosixParser();
		CommandLine cl = parser.parse(opt, args);

		if (cl.hasOption('h')) {
			HelpFormatter f = new HelpFormatter();
			f.printHelp("simal", opt, true);
		} else if (cl.hasOption('v')) {
			System.out.println("Simal version   : " + SIMAL_VERSION);
			System.out.println("Java version    : " + System.getProperty("java.version"));
			System.out.println("Java vendor     : " + System.getProperty("java.vendor"));
			System.out.println("OS              : " + System.getProperty("os.name"));
			System.out.println("OS Architecture : " + System.getProperty("os.arch"));
		} else {
			// do other commands
		}
	}

}

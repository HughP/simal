package uk.ac.osswatch.simal.rdf;

/**
 * An exception that is thrown when an attempt is made to 
 * add a second entity with an already existing QName
 *
 */
public class DuplicateQNameException extends Exception {
	private static final long serialVersionUID = 7147690671523551807L;

	/**
	 * @param message
	 * @param cause
	 */
	public DuplicateQNameException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicateQNameException(String message) {
		super(message);
	}

}

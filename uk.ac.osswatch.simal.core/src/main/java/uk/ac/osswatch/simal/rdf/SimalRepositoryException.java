package uk.ac.osswatch.simal.rdf;

/**
 * An exception that is thrown when the repository 
 * cannot be configured or accessed correctly.
 *
 */
public class SimalRepositoryException extends Exception {
	private static final long serialVersionUID = 9157131958514241674L;

	/**
	 * @param message
	 * @param cause
	 */
	public SimalRepositoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public SimalRepositoryException(String message) {
		super(message);
	}

}

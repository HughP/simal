package uk.ac.osswatch.simal.rdf;

public class TransactionException extends Exception {
	private static final long serialVersionUID = 4579805552051400215L;

	/**
	 * @param message
	 * @param cause
	 */
	public TransactionException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransactionException(String message) {
		super(message);
	}
}

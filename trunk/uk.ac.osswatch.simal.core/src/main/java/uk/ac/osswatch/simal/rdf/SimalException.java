package uk.ac.osswatch.simal.rdf;

/**
 * A base level Simal Exception. This should be used whenever 
 * Simal wants to throw an exception that is not covered by another
 * SimalException class.
 *
 */
public class SimalException extends Exception {
  private static final long serialVersionUID = -3554180177554295441L;

  /**
   * @param message
   * @param cause
   */
  public SimalException(String message, Throwable cause) {
    super(message, cause);
  }

  public SimalException(String message) {
    super(message);
  }


}

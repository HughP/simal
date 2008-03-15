package uk.ac.osswatch.simal.rest;

/**
 * An exception representing a problem with calling the REST
 * API.
 *
 */
public class SimalAPIException extends Exception {
  private static final long serialVersionUID = 4451929444420972751L;

  
  public SimalAPIException(String message) {
    super(message);
  }


  public SimalAPIException(String message, Exception cause) {
    super(message, cause);
  }
}

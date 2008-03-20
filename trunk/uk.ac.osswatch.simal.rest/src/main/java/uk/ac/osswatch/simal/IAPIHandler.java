package uk.ac.osswatch.simal;


/**
 * An API Handler is a class that will process a given 
 * API request.
 *
 */
public interface IAPIHandler {

  /**
   * Execute a command.
   * 
   * @param cmd
   * @throws SimalAPIException 
   */
  public String execute(String cmd) throws SimalAPIException;

}

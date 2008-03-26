package uk.ac.osswatch.simal.rest;


/**
 * An API Handler is a class that will process a given 
 * API request.
 *
 */
public interface IAPIHandler {

  /**
   * Execute the command.
   * 
   * @throws SimalAPIException 
   */
  public String execute() throws SimalAPIException;

}

package uk.ac.osswatch.simal.rest;

import java.net.URI;
import java.net.URISyntaxException;


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

  /**
   * Get the URI that represents the current state as
   * it relates to the command being handled.
   * 
   * @return
   * @throws URISyntaxException 
   */
  public URI getStateURI() throws URISyntaxException;

}

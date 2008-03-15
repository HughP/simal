package uk.ac.osswatch.simal.rest;

import uk.ac.osswatch.simal.rdf.SimalRepository;

/**
 * A factory class for generating a specific handler for a
 * given API request.
 * 
 */
public class HandlerFactory {

  /**
   * Create the required API handler for a given command.
   * 
   * @param command the command to execute
   * @param repo the repo that the command is to operate on
   * @return
   * @throws SimalAPIException 
   */
  public static IAPIHandler createHandler(String command, SimalRepository repo) throws SimalAPIException {
    IAPIHandler handler = null;
    if (command.contains(RESTServlet.COMMAND_ALL_PROJECTS)) {
      handler = new ProjectAPI(repo);
    } else if (command.contains(RESTServlet.COMMAND_ALL_COLLEAGUES)) {
      handler = new PersonAPI(repo);
    }
    
    if (handler == null) {
      throw new SimalAPIException("Unable to create API Handler for command (" + command + "}");
    }
    
    return handler;
    
  }
}

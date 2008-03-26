package uk.ac.osswatch.simal.rest;

import uk.ac.osswatch.simal.rdf.SimalRepository;

/**
 * A factory class for generating a specific handler for a
 * given API request.
 * 
 */
public class SimalHandlerFactory {

  /**
   * Create the required API handler for a given command.
   * 
   * @param command the command to execute
   * @param repo the repo that the command is to operate on
   * @return
   * @throws SimalAPIException 
   */
  public static IAPIHandler createHandler(RESTCommand command, SimalRepository repo) throws SimalAPIException {
    IAPIHandler handler = null;
    if (command.isGetAllProjects()) {
      handler = new ProjectAPI(command);
    } else if (command.isGetColleagues()) {
      handler = new PersonAPI(command);
    }
    
    if (handler == null) {
      throw new SimalAPIException("Unable to create API Handler for command (" + command + "}");
    }
    
    return handler;
    
  }
}

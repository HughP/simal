package uk.ac.osswatch.simal.myExperiment;

import uk.ac.osswatch.simal.rest.IAPIHandler;
import uk.ac.osswatch.simal.rest.RESTCommand;
import uk.ac.osswatch.simal.rest.SimalAPIException;

/**
 * A factory class for generating a specific handler for a
 * given MyExperiment API request.
 * 
 */
public class MyExperimentHandlerFactory {

  /**
   * Create the required API handler for a given command.
   * 
   * @param command the command to execute
   * @param url the URL of the MyExperiment server
   * @return
   * @throws SimalAPIException 
   */
  public static IAPIHandler createHandler(RESTCommand command, String uri) throws SimalAPIException {
    IAPIHandler handler = null;
    if (command.isPersonCommand()) {
      handler = new PersonAPI(uri);
    }
    
    if (handler == null) {
      throw new SimalAPIException("Unable to create API Handler for command (" + command + "}");
    }
    
    return handler;
    
  }
}

package uk.ac.osswatch.simal.rest;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * An abstract handler for the REST API. All handlers should 
 * extend this abstract class.
 *
 */
public abstract class AbstractHandler implements IAPIHandler {
  private static final String SIMAL_REST_BASE_URI = "http://localhost:8080/simal-rest";
  protected RESTCommand command;

  /**
   * Create a handler to operate on a given repository.
   * Handlers should not be instantiated directly,
   * use HandlerFactory.createHandler(...) instead.
   * @param cmd 
   */
  protected AbstractHandler(RESTCommand cmd) {
    this.command = cmd;
  }
  
  public URI getStateURI() throws URISyntaxException {
    URI uri = new URI(SIMAL_REST_BASE_URI + command.toPathInfo());
    return uri;
  }

  /**
   * Get the Base URI for this command.
   * @return
   */
  public String getBaseURI() {
    if (command.getSource().equals(RESTCommand.SOURCE_TYPE_SIMAL)) {
      return SIMAL_REST_BASE_URI;
    } else if (command.getSource().equals(RESTCommand.SOURCE_TYPE_MYEXPERIMENT)) {
      return "http://www.myexperiment.org";
    }
    return null;
  }
}

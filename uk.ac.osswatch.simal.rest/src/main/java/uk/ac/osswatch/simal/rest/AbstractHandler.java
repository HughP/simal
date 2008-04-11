package uk.ac.osswatch.simal.rest;

import java.net.URI;
import java.net.URISyntaxException;

import uk.ac.osswatch.simal.rdf.SimalRepository;

/**
 * An abstract handler for the REST API. All handlers should 
 * extend this abstract class.
 *
 */
public abstract class AbstractHandler implements IAPIHandler {
  protected RESTCommand command;
  private String baseurl;

  /**
   * Create a handler to operate on a given repository.
   * Handlers should not be instantiated directly,
   * use HandlerFactory.createHandler(...) instead.
   * @param cmd 
   */
  protected AbstractHandler(RESTCommand cmd) {
    this.command = cmd;
    this.baseurl = SimalRepository.getProperty(SimalRepository.PROPERTY_REST_BASEURL);
  }
  
  public URI getStateURI() throws URISyntaxException {
    URI uri = new URI(baseurl + command.toPathInfo());
    return uri;
  }

  /**
   * Get the Base URI for this command.
   * @return
   */
  public String getBaseURI() {
    if (command.getSource().equals(RESTCommand.SOURCE_TYPE_SIMAL)) {
      return baseurl;
    } else if (command.getSource().equals(RESTCommand.SOURCE_TYPE_MYEXPERIMENT)) {
      return "http://www.myexperiment.org";
    }
    return null;
  }
}

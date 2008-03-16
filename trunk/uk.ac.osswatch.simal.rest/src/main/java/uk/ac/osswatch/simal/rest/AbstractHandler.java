package uk.ac.osswatch.simal.rest;

import uk.ac.osswatch.simal.rdf.SimalRepository;

/**
 * An abstract handler for the REST API. All handlers should 
 * extend this abstract class.
 *
 */
public abstract class AbstractHandler implements IAPIHandler {

  public static final String XML_SUFFIX = "/xml";
  public static final String JSON_SUFFIX = "/json";
  protected static SimalRepository repo;

  /**
   * Create a handler to operate on a given repository.
   * Handlers should not be instantiated directly,
   * use HandlerFactory.createHandler(...) instead.
   * 
   * @param repo
   */
  protected AbstractHandler(SimalRepository repo) {
    AbstractHandler.repo = repo;
  }
}

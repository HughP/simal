package uk.ac.osswatch.simal;

/**
 * An abstract handler for the REST API. All handlers should 
 * extend this abstract class.
 *
 */
public abstract class AbstractHandler implements IAPIHandler {

  public static final String PARAM_PERSON_ID = "person-";

  /**
   * Create a handler to operate on a given repository.
   * Handlers should not be instantiated directly,
   * use HandlerFactory.createHandler(...) instead.
   */
  protected AbstractHandler() {
  }
}

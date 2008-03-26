package uk.ac.osswatch.simal.rest;

/**
 * An abstract handler for the REST API. All handlers should 
 * extend this abstract class.
 *
 */
public abstract class AbstractHandler implements IAPIHandler {

  /**
   * Create a handler to operate on a given repository.
   * Handlers should not be instantiated directly,
   * use HandlerFactory.createHandler(...) instead.
   */
  protected AbstractHandler() {
  }
}

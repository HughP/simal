package uk.ac.osswatch.simal.rest;

/**
 * An abstract handler for the REST API. All handlers should 
 * extend this abstract class.
 *
 */
public abstract class AbstractHandler implements IAPIHandler {
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
}

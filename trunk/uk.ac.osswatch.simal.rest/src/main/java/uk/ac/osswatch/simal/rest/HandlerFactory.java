package uk.ac.osswatch.simal.rest;

import uk.ac.osswatch.simal.myExperiment.MyExperimentHandlerFactory;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A factory class for REST command handlers.
 * 
 */
public class HandlerFactory {

  private static SimalRepository simalRepo;
  private static HandlerFactory factory;

  /**
   * No need to instantiate this calls, use get(RESTCommand cmd) instead.
   * 
   * @throws SimalRepositoryException
   */
  private HandlerFactory() throws SimalAPIException {
    try {
      simalRepo = new SimalRepository();
      simalRepo.setIsTest(true);
      simalRepo.initialise();
    } catch (SimalRepositoryException e) {
      throw new SimalAPIException("Unable to create HandlerFactory", e);
    }
  }

  /**
   * Get a handler for the supplied command.
   * 
   * @param cmd
   * @return
   * @throws SimalAPIException
   */
  public static IAPIHandler get(RESTCommand cmd) throws SimalAPIException {
    initFactory();
    
    if (cmd.getSource().equals(RESTCommand.SOURCE_TYPE_SIMAL)) {
      return SimalHandlerFactory.createHandler(cmd, simalRepo);
    } else if (cmd.getSource().equals(RESTCommand.SOURCE_TYPE_MYEXPERIMENT)) {
      return MyExperimentHandlerFactory.createHandler(cmd);
    } else {
      throw new SimalAPIException("Unable to get handler for source type "
          + cmd.getSource());
    }
  }

  /**
   * Initialise the handler factory if not already done so. This
   * init method uses the default settings for the factory.
   * 
   * @throws SimalAPIException
   */
  private static void initFactory() throws SimalAPIException {
    if (factory == null) {
      factory = new HandlerFactory();
    }
  }
  
  /**
   * Get the Simal repository this HandlerFactory is working with.
   * @throws SimalAPIException if unable to connect to the repo
   */
  public static SimalRepository getSimalRepository() throws SimalAPIException {
    initFactory();
    return simalRepo;
  }
}

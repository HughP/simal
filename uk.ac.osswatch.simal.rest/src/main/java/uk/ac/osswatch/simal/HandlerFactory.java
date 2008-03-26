package uk.ac.osswatch.simal;

import uk.ac.osswatch.simal.myExperiment.MyExperimentHandlerFactory;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rest.SimalHandlerFactory;

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

  public static IAPIHandler get(RESTCommand cmd) throws SimalAPIException {
    if (factory == null) {
      factory = new HandlerFactory();
    }
    
    if (cmd.getSource().equals(RESTCommand.SOURCE_TYPE_SIMAL)) {
      return SimalHandlerFactory.createHandler(cmd, simalRepo);
    } else if (cmd.getSource().equals(RESTCommand.SOURCE_TYPE_MYEXPERIMENT)) {
      return MyExperimentHandlerFactory.createHandler(cmd,
          "http://www.myexperiment.org");
    } else {
      throw new SimalAPIException("Unable to get handler for source type "
          + cmd.getSource());
    }
  }

}

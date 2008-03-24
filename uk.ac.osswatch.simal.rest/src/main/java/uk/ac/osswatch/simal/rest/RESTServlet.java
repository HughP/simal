package uk.ac.osswatch.simal.rest;

import uk.ac.osswatch.simal.AbstractRESTServlet;
import uk.ac.osswatch.simal.IAPIHandler;
import uk.ac.osswatch.simal.RESTCommand;
import uk.ac.osswatch.simal.SimalAPIException;
import uk.ac.osswatch.simal.myExperiment.MyExperimentHandlerFactory;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class RESTServlet extends AbstractRESTServlet {
  private static final long serialVersionUID = -7003783530005464708L;

  private static SimalRepository repo;

  public RESTServlet() {
    try {
      repo = new SimalRepository();
      repo.setIsTest(true);
      repo.initialise();
    } catch (SimalRepositoryException e) {
      log("Unable to create repository object", e);
    }
  }

  @Override
  protected IAPIHandler getHandler(RESTCommand cmd) throws SimalAPIException {
    if (cmd.getSource().equals(RESTCommand.SOURCE_TYPE_SIMAL)) {
      return SimalHandlerFactory.createHandler(cmd, repo);
    } else if (cmd.getSource().equals(RESTCommand.SOURCE_TYPE_MYEXPERIMENT)) {
      return MyExperimentHandlerFactory.createHandler(cmd, "http://www.myexperiment.org");
    } else {
      throw new SimalAPIException("Unable to get handler for source type " + cmd.getSource());
    }
  }
}
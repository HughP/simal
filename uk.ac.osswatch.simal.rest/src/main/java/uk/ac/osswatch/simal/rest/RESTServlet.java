package uk.ac.osswatch.simal.rest;

import uk.ac.osswatch.simal.AbstractRESTServlet;
import uk.ac.osswatch.simal.IAPIHandler;
import uk.ac.osswatch.simal.SimalAPIException;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class RESTServlet extends AbstractRESTServlet {
  private static final long serialVersionUID = -7003783530005464708L;

  public static final String COMMAND_ALL_PROJECTS = "/allProjects/";
  public static final String COMMAND_ALL_COLLEAGUES = "/allColleagues/";

  public static final String XML_SUFFIX = "/xml";
  public static final String JSON_SUFFIX = "/json";

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
  protected IAPIHandler getHandler(String cmd) throws SimalAPIException {
    return SimalHandlerFactory.createHandler(cmd, repo);
  }
}
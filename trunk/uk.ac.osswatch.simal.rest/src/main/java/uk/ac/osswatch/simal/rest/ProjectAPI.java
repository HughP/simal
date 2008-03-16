package uk.ac.osswatch.simal.rest;

import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A class for handling all API calls relating to projects.
 *
 */
public class ProjectAPI extends AbstractHandler {
  
  /**
   * Create a new project API object to operate on projects in a given
   * Simal repository. Handlers should not be instantiated directly,
   * use HandlerFactory.createHandler(...) instead.
   * 
   * @param repo
   */
  protected ProjectAPI(SimalRepository repo) {
    super(repo);
  }
  
  /**
   * Execute a command.
   * 
   * @param cmd
   * @throws SimalAPIException 
   */
  public String execute(String cmd) throws SimalAPIException {
    if (cmd.contains(RESTServlet.COMMAND_ALL_PROJECTS)) {
      return getAllProjects(cmd);
    } else {
      throw new SimalAPIException("Unkown command: " + cmd);
    }
  }

  /**
   * Get all the projects from the repository.
   * 
   * @param req
   * @param cmd
   * @return
   * @throws SimalAPIException
   */
  public String getAllProjects(String cmd) throws SimalAPIException {
    if (cmd.endsWith(JSON_SUFFIX)) {
      try {
        return repo.getAllProjectsAsJSON();
      } catch (SimalRepositoryException e) {
        throw new SimalAPIException("Unable to get JSON representation of all projects from the repository", e);
      }
    } else {
      throw new SimalAPIException("Unkown format requested - "
          + cmd);
    }
  }

}

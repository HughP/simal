package uk.ac.osswatch.simal.rest;

import javax.servlet.http.HttpServletRequest;

import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A class for handling all API calls relating to projects.
 *
 */
public class ProjectAPI {

  private SimalRepository repo;

  /**
   * Create a new project API object to operate on projects in a given
   * Simal repository.
   * 
   * @param repo
   */
  public ProjectAPI(SimalRepository repo) {
    this.repo = repo;
  }

  /**
   * Get all the projects from the repository.
   * 
   * @param req
   * @param cmd
   * @return
   * @throws SimalAPIException
   */
  public String getAllProjects(HttpServletRequest req, String cmd) throws SimalAPIException {
    if (cmd.endsWith(".json")) {
      try {
        return repo.getAllProjectsAsJSON();
      } catch (SimalRepositoryException e) {
        throw new SimalAPIException("Unable to get JSON representation of all projects from the repository", e);
      }
    } else {
      throw new SimalAPIException("Unkown format requested - "
          + req.getPathInfo());
    }
  }

}

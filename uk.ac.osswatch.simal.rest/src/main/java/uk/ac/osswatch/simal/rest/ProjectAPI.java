package uk.ac.osswatch.simal.rest;

import uk.ac.osswatch.simal.model.IProject;
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
  protected ProjectAPI(RESTCommand cmd) {
    super(cmd);
  }
  
  /**
   * Execute a command.
   * 
   * @param cmd
   * @throws SimalAPIException 
   */
  public String execute() throws SimalAPIException {
    if (command.isGetAllProjects()) {
      return getAllProjects(command);
    } else if (command.isGetProject()) {
      return getProject(command);
    } else {
      throw new SimalAPIException("Unkown command: " + command);
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
  public String getAllProjects(RESTCommand cmd) throws SimalAPIException {
    if (cmd.isJSON()) {
      try {
        return HandlerFactory.getSimalRepository().getAllProjectsAsJSON();
      } catch (SimalRepositoryException e) {
        throw new SimalAPIException("Unable to get JSON representation of all projects from the repository", e);
      }
    } else {
      throw new SimalAPIException("Unkown format requested - "
          + cmd);
    }
  }

  /**
   * Get a single project from the repository.
   * 
   * @param command
   * @return
   * @throws SimalAPIException 
   */
  private String getProject(RESTCommand command) throws SimalAPIException {
    String id = command.getProjectID();
    
    if (command.isXML()) {
      try {
        IProject project = HandlerFactory.getSimalRepository().findProjectById(id);
        if (project == null) {
          throw new SimalAPIException("Project with Simal ID " + id + " does not exist");
        }
        return project.toXML();
      } catch (SimalRepositoryException e) {
        throw new SimalAPIException("Unable to get XML representation of project from the repository", e);
      }
    } else if (command.isJSON()) {
      try {
        return HandlerFactory.getSimalRepository().findProjectById(id).toJSON();
      } catch (SimalRepositoryException e) {
        throw new SimalAPIException("Unable to get JSON representation of project from the repository", e);
      }
    } else {
      throw new SimalAPIException("Unkown format requested - "
          + command);
    }
  }

}

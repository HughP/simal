package uk.ac.osswatch.simal.model;

import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * Extra data provided by simal.Project objects over and
 * above that provided by doap.Project objects.
 * 
 */
@rdf("http://simal.oss-watch.ac.uk/ns/simal#Project")
public interface IProject {

  /**
   * Get the Simal ID for this project. This is a unique identifier
   * within the repository from which it was retrieved.
   * 
   * @return
   * @throws SimalRepositoryException 
   */
  @rdf("http://simal.oss-watch.ac.uk/ns/simal#project-id")
  public String getID() throws SimalRepositoryException;
}

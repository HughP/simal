package uk.ac.osswatch.simal.model;

import org.openrdf.concepts.doap.Project;
import org.openrdf.elmo.annotations.rdf;

/**
 * Extra data provided by simal.Project objects over and
 * above that provided by doap.Project objects.
 * 
 */
@rdf("http://usefulinc.com/ns/doap#Project")
public interface IProject extends Project, IDoapProjectBehaviour {

  /**
   * Get the Simal ID for this project. This is a unique identifier
   * within the repository from which it was retrieved.
   * 
   * @return 
   */
  @rdf("http://simal.oss-watch.ac.uk/ns/simal#projectId")
  public String getProjectID();

  /**
   * Set the Simal ID for this project. This is a unique identifier
   * within the repository from which it was retrieved.
   * 
   * @return 
   */
  public void setProjectID(String newID);
}

package uk.ac.osswatch.simal.model;

import java.util.Set;

import org.openrdf.concepts.foaf.Person;
import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;


/**
 * A person in the simal space. This records simal 
 * specific data about a person over and above what
 * is possible in FOAF.
 *
 */

@rdf("http://xmlns.com/foaf/0.1/Person")
public interface IPerson extends Person, IFoafPersonBehaviour {
  
  /**
   * Get a set of people who this person knows. They are deemed to know someone
   * if any of the following are true:
   * 
   * <ul>
   * <li>foaf:knows is set</li>
   * <li>both people are listed in the same project record</li>
   * </ul>
   * 
   * @throws SimalRepositoryException
   */
  public Set<IPerson> getColleagues() throws SimalRepositoryException;
  

  /**
   * Get the Simal ID for this person. This is a unique identifier
   * within the repository from which it was retrieved.
   * 
   * @return 
   */
  @rdf("http://simal.oss-watch.ac.uk/ns/0.2/simal#personId")
  public String getSimalId();

  /**
   * Set the Simal ID for this person. This is a unique identifier
   * within the repository from which it was retrieved.
   * 
   * @return 
   */
  public void setSimalId(String newId);
}

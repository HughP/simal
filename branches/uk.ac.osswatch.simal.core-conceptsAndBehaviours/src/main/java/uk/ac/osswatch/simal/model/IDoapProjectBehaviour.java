package uk.ac.osswatch.simal.model;

import java.util.HashSet;

import org.openrdf.concepts.foaf.Person;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * Behaviour for a simal.Project object
 *
 * @see uk.ac.osswatch.simal.model.elmo.DoapProjectBehaviour
 */
public interface IDoapProjectBehaviour extends IDoapResourceBehaviour {

  /**
   * Get all the people known to be engaged with this project.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public HashSet<Person> getAllPeople() throws SimalRepositoryException;

  /**
   * Get the ID of this project. If no ID has been assigned
   * yet then get the next avialble ID from the repository 
   * and assign that.
   * @throws SimalRepositoryException 
   */
  public String getID() throws SimalRepositoryException;
  
  /**
   * Get the default name for this project.
   */
  public String getName();
} 

package uk.ac.osswatch.simal.model;

import java.util.HashSet;

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
  public HashSet<IFaofPersonBehaviour> getAllPeople() throws SimalRepositoryException;

}

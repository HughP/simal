package uk.ac.osswatch.simal.model;

import java.util.Set;

import org.openrdf.concepts.foaf.Document;

import uk.ac.osswatch.simal.model.elmo.FoafPersonBehaviour;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A behaviour for FOAF people objects.
 * 
 */
public interface IFaofPersonBehaviour extends IFoafResourceBehaviour {

  /**
   * Get the home page of this person.
   */
  public Set<Document> getHomepages();

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
  public Set<IFaofPersonBehaviour> getColleagues() throws SimalRepositoryException;

  /**
   * Get a set of people that know this person.
   * 
   * @throws SimalRepositoryException
   */
  public Set<FoafPersonBehaviour> getKnows() throws SimalRepositoryException;
}

package uk.ac.osswatch.simal.model;

import java.util.Set;

import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A behaviour for FOAF people objects.
 * 
 */
@rdf("http://xmlns.com/foaf/0.1/Person")
public interface IFoafPersonBehaviour extends IFoafResourceBehaviour {

  /**
   * Get all the colleagues of this person. A colleague is defined as anyone who
   * works on the same project as this person.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public Set<IPerson> getColleagues() throws SimalRepositoryException;
  

  /**
   * Get all the people that this person knows
   */
  public Set<IPerson> getKnows();

  /**
   * Get the label for this person. The label for a person is derived
   * from their known names. If the person does not have any defined
   * names then the toString() method is used..
   * 
   * @return
   */
  public String getLabel();
}

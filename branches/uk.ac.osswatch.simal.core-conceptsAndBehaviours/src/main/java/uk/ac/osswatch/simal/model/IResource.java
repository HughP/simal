package uk.ac.osswatch.simal.model;

import uk.ac.osswatch.simal.rdf.SimalProperties;

/**
 * A wrapper for an RDFS Resource in Elmo.
 * 
 * @see org.opeenrdf.sesame.elmo.concepts.rdfs.IResource
 * 
 */
public interface IResource {

  /**
   * A label for representing the resource to humans. If no label is defined
   * (using rdfs:label) then an attempt is made to find a label in the
   * repository (if fetchLabels is set to true). Otherwise the toString() value
   * of rdfs:resource is returned.
   * 
   * @return
   */
  public String getLabel(boolean fetchLabel);

  /**
   * A label for representing the resource to humans. If no label is defined
   * (using rdfs:label) then an attempt is made to find a label in the
   * repository (if fetchLabels is set to true). Otherwise, the value of
   * defaultLabel is returned. If defaultLabel is null then the to String()
   * value of rdfs:resource is returned.
   * 
   * @return
   */
  public String getLabel(String defaultLabel, boolean fetchLabel);

  /**
   * A human readable comment describing the resource.
   * 
   * @return
   */
  public String getComment();

  /**
   * Return the repository that this resource belongs to.
   * 
   * @return
   */
  public SimalProperties getRepository();
}

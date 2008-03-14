package uk.ac.osswatch.simal.model;

/**
 * A wrapper for an RDFS Resource in Elmo.
 * 
 * @see org.opeenrdf.sesame.elmo.concepts.rdfs.IResourceBehaviour
 * 
 */
public interface IResourceBehaviour {

  /**
   * Get the label for this resource. If the resource does not have a defined
   * label return value of the toString() method.
   * 
   * @return
   */
  public String getLabel();

  /**
   * Get the label for this resource. If the resource does not have a defined
   * label use the supplied default label (if not null) or the
   * resource return value of the toString() method.
   * 
   * @param defaultLabel
   * @return
   */
  public String getLabel(String defaultLabel);

  /**
   * A human readable comment describing the resource.
   * 
   * @return
   */
  public String getComment();

  /**
   * Create a JSON representation of this resource.
   * 
   * @param asRecord
   *          set to true if this is a record within another JSON file or false
   *          if you want this to be a complete JSON file.
   */
  public String toJSON(boolean asRecord);
}

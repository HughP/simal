package uk.ac.osswatch.simal.model.elmo;

import org.openrdf.concepts.rdfs.Resource;
import org.openrdf.elmo.Entity;
import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.model.IResourceBehaviour;

/**
 * Provides functionality for a Resource object provided by
 * Elmo.
 */
@rdf("http://www.w3.org/2000/01/rdf-schema#Resource")
public class ResourceBehavior implements IResourceBehaviour {
  protected final Entity elmoEntity;
  
  /**
   * Create a resource behaviour for a given Elmo resource.
   * 
   * @param elmoEntity
   */
  public ResourceBehavior(Resource resource) {
    this.elmoEntity = resource;
  }

  /**
   * Create a resource behaviour for a given Entity.
   * 
   * @param elmoEntity
   */
  public ResourceBehavior(Entity elmoEntity) {
    this.elmoEntity = elmoEntity;
  }

  /**
   * Get the label for this resource. If the resource does not have a defined
   * label return value of the toString() method.
   * 
   * @return
   */
  public String getLabel() {
    return getLabel(null);
  }

  /**
   * Get the label for this resource. If the resource does not have a defined
   * label use the supplied default label (if not null) or the
   * resource return value of the toString() method.
   * 
   * @param defaultLabel
   * @return
   */
  public String getLabel(String defaultLabel) {
    String label = getResource().getRdfsLabel();
    
    if (label == null) {
        label = defaultLabel;
    } else {
        label = elmoEntity.toString();
    }
    return label;
  }

  public String getComment() {
    String comment = getResource().getRdfsComment();
    if (comment == null) {
      comment = "";
    }
    return getResource().getRdfsComment();
  }

  protected Resource getResource() {
    return (Resource)elmoEntity;
  }
  
  public String toString() {
    return elmoEntity.toString();
  }

  public String toJSON(boolean asRecord) {
    // TODO Auto-generated method stub
    return null;
  }
}

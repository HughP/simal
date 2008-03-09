package uk.ac.osswatch.simal.model.elmo;

import java.util.Set;

import javax.xml.namespace.QName;

import uk.ac.osswatch.simal.model.IRCS;

/**
 * A wrapper around an Elmo doap repository object.
 * 
 * @see org.openrdf.concepts.doap.Repository
 * 
 */
public class RCS extends DoapResource implements IRCS {
  private static final long serialVersionUID = 1383011087069487935L;
  private org.openrdf.concepts.doap.Repository elmoRCS;

  /**
   * Create a new wrapper around an elmo Version object.
   * 
   * @param simalTestProject
   */
  public RCS(org.openrdf.concepts.doap.Repository elmoRCS) {
    this.elmoRCS = elmoRCS;
  }

  public String toString() {
    QName qname = elmoRCS.getQName();
    return qname.getNamespaceURI() + qname.getLocalPart();
  }

  public Set<String> getAnonRoots() {
    return convertToSetOfStrings(elmoRCS.getDoapAnonRoots());
  }

  public Set<String> getBrowse() {
    return convertToSetOfStrings(elmoRCS.getDoapBrowse());
  }

  public Set<String> getLocations() {
    return convertToSetOfStrings(elmoRCS.getDoapLocations());
  }

}

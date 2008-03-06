package uk.ac.osswatch.simal.model.elmo;

import java.util.Set;

import javax.xml.namespace.QName;

import uk.ac.osswatch.simal.model.IVersion;
import uk.ac.osswatch.simal.rdf.SimalRepository;

/**
 * A wrapper around an Elmo doap version object.
 * 
 * @see org.openrdf.concepts.doap.Version
 * 
 */
public class Version extends DoapResource implements IVersion {
  private static final long serialVersionUID = 7979313699896399149L;

  /**
   * Create a new wrapper around an elmo Version object.
   * 
   * @param simalTestProject
   */
  public Version(org.openrdf.concepts.doap.Version elmoVersion,
      SimalRepository repository) {
    super(elmoVersion, repository);
  }

  private org.openrdf.concepts.doap.Version getVersion() {
    return (org.openrdf.concepts.doap.Version) elmoResource;
  }

  public Set<String> getFileReleases() {
    return convertToSetOfStrings(getVersion().getDoapFileReleases());
  }

  public Set<String> getRevisions() {
    return convertToSetOfStrings(getVersion().getDoapRevisions());
  }

  public String toString() {
    QName qname = getVersion().getQName();
    return qname.getNamespaceURI() + qname.getLocalPart();
  }

}

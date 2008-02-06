package uk.ac.osswatch.simal.model.elmo;

import java.util.Set;

import javax.xml.namespace.QName;

import uk.ac.osswatch.simal.model.IVersion;

/**
 * A wrapper around an Elmo doap version object.
 * 
 * @see org.openrdf.concepts.doap.Version
 * 
 */
public class Version extends DoapResource implements IVersion {
	private static final long serialVersionUID = 7979313699896399149L;
	private org.openrdf.concepts.doap.Version elmoVersion;

	/**
	 * Create a new wrapper around an elmo Version object.
	 * 
	 * @param simalTestProject
	 */
	public Version(org.openrdf.concepts.doap.Version elmoVersion) {
		this.elmoVersion = elmoVersion;
	}
	
	public String toString() {
		QName qname = elmoVersion.getQName();
		return qname.getNamespaceURI() + qname.getLocalPart();
	}

	public Set<String> getFileReleases() {
		return convertToSetOfStrings(elmoVersion.getDoapFileReleases());
	}

	public Set<String> getRevisions() {
		return convertToSetOfStrings(elmoVersion.getDoapRevisions());
	}

}

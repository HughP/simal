package uk.ac.osswatch.simal.model.elmo;

import javax.xml.namespace.QName;

import uk.ac.osswatch.simal.model.IRCS;

/**
 * A wrapper around an Elmo doap repository object.
 * 
 * @see org.openrdf.concepts.doap.Repository
 * 
 */
public class RCS implements IRCS {
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

}

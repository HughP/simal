package uk.ac.osswatch.simal.model.elmo;

import javax.xml.namespace.QName;

import uk.ac.osswatch.simal.model.IPerson;

/**
 * A wrapper around an Elmo foaf person object.
 * 
 * @see org.openrdf.concepts.foaf.Person
 * 
 */
public class Person extends FoafResource implements IPerson {
	private static final long serialVersionUID = -6234779132155536113L;
	private org.openrdf.concepts.foaf.Person elmoPerson;

	/**
	 * Create a new wrapper around an elmo Person object.
	 * 
	 * @param simalTestProject
	 */
	public Person(org.openrdf.concepts.foaf.Person elmoPerson) {
		this.elmoPerson = elmoPerson;
	}
	
	public String toString() {
		QName qname = elmoPerson.getQName();
		return qname.getNamespaceURI() + qname.getLocalPart();
	}

}

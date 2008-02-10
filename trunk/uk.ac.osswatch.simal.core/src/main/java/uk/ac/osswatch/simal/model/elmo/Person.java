package uk.ac.osswatch.simal.model.elmo;

import java.util.Set;

import org.openrdf.concepts.foaf.Document;

import uk.ac.osswatch.simal.model.IPerson;

/**
 * A wrapper around an Elmo foaf person object.
 * 
 * @see org.openrdf.concepts.foaf.Person
 * 
 */
public class Person extends FoafResource implements IPerson {
	private static final long serialVersionUID = -6234779132155536113L;
	
	/**
	 * Create a new wrapper around an elmo Person object.
	 * 
	 * @param simalTestProject
	 */
	public Person(org.openrdf.concepts.foaf.Person elmoPerson) {
		super(elmoPerson);
	}
	
	/**
	 * Get the home page of this person.
	 */
	public Set<Document> getHomepages() {
		return getPerson().getFoafHomepages();
	}

	private org.openrdf.concepts.foaf.Person getPerson() {
		return (org.openrdf.concepts.foaf.Person)elmoResource;
	}
}

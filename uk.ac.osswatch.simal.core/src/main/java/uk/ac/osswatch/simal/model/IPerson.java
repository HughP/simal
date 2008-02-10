package uk.ac.osswatch.simal.model;

import java.util.Set;

import org.openrdf.concepts.foaf.Document;

/**
 * A wrapper around a repository representation of a foaf Person.
 * 
 * @see org.openrdf.concepts.foaf.Person
 *
 */
public interface IPerson extends IFoafResource {
	
	/**
	 * Get the home page of this person.
	 */
	public Set<Document> getHomepages();
}

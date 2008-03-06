package uk.ac.osswatch.simal.model;

import java.util.Set;

import org.openrdf.concepts.foaf.Document;

import uk.ac.osswatch.simal.model.elmo.Person;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

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

    /**
     * Get a set of people who this person knows. They are deemed to know
     * someone if any of the following are true:
     * 
     * <ul>
     * <li>foaf:knows is set</li>
     * <li>both people are listed in the same project record</li>
     * </ul>
     * 
     * @throws SimalRepositoryException
     */
    public Set<IPerson> getColleagues() throws SimalRepositoryException;

    /**
     * Get a set of people that know this person.
     * 
     * @throws SimalRepositoryException
     */
    public Set<Person> getKnows() throws SimalRepositoryException;
}

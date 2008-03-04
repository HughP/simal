package uk.ac.osswatch.simal.model.elmo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.openrdf.concepts.foaf.Document;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A wrapper around an Elmo foaf person object.
 * 
 * @see org.openrdf.concepts.foaf.Person
 * 
 */
public class Person extends FoafResource implements IPerson {
    private static final long serialVersionUID = -6234779132155536113L;

    protected Person() {
        super();
    }

    /**
     * Create a new wrapper around an elmo Person object.
     * 
     * @param simalTestProject
     */
    public Person(org.openrdf.concepts.foaf.Person elmoPerson,
            SimalRepository repository) {
        super(elmoPerson, repository);
    }

    /**
     * Get the home page of this person.
     */
    public Set<Document> getHomepages() {
        return getPerson().getFoafHomepages();
    }

    private org.openrdf.concepts.foaf.Person getPerson() {
        return (org.openrdf.concepts.foaf.Person) elmoResource;
    }

    /**
     * Get a set of people who this person knows. They are deemed to know
     * someone if any of the following are true:
     * 
     * <ul>
     * <li>foaf:knows is set</li>
     * <li>both people are listed in the same project record</li>
     * </ul>
     * @throws SimalRepositoryException 
     */
    public Set<Person> getColleagues() throws SimalRepositoryException {
        // get all the projects this person is listed in
        Set<Person> colleagues = getKnows();
        // get all the people in the projects
        // return the result
        return colleagues;
    }

    /**
     * Get a set of people that know this person.
     * @throws SimalRepositoryException 
     */
    public Set<Person> getKnows() throws SimalRepositoryException {
        return convertToPersonSet(getPerson().getFoafKnows());
    }

    /**
     * Convert a set of FOAF Person objects to a set of Simal Person objects.
     * 
     * @param foafKnows
     * @return
     * @throws SimalRepositoryException 
     */
    private Set<Person> convertToPersonSet(
            Set<org.openrdf.concepts.foaf.Person> people) throws SimalRepositoryException {
        Iterator<org.openrdf.concepts.foaf.Person> itr = people.iterator();
        Set<Person> result = new HashSet<Person>(people.size());

        Object person;
        while (itr.hasNext()) {
            person = getRepository().getManager().designateEntity(org.openrdf.concepts.foaf.Person.class, itr.next());
            result.add(new Person((org.openrdf.concepts.foaf.Person)person, getRepository()));
        }
        return result;
    }

    /**
     * Get the label for this person. If the person does not have a defined
     * label then the value returned by getGivennames is used. If this is null
     * and fetch label is set to true then attempt to find a label in the
     * repository, otherwise use the supplied default label (if not null) or the
     * resource return value of the toString() method.
     * 
     * @param defaultLabel
     * @param fetchLabel
     * @return
     */
    public String getLabel(String defaultLabel, boolean fetchLabel) {
        if (cachedLabel != null) {
            return cachedLabel;
        }

        cachedLabel = elmoResource.getRdfsLabel();
        if (cachedLabel == null || cachedLabel == "") {
            cachedLabel = getGivennames();
            if (cachedLabel == null || cachedLabel == "") {
                try {
                    cachedLabel = getRepository().getLabel(
                            elmoResource.getQName());
                } catch (SimalRepositoryException e) {
                    // Oh well, that didn't work, it'll be dealt with later in
                    // this method,
                    // but we need to log the problem.
                    e.printStackTrace();
                }
            }
        }

        if (cachedLabel == null || cachedLabel.equals("")) {
            if (defaultLabel != null) {
                cachedLabel = defaultLabel;
            } else {
                cachedLabel = elmoResource.toString();
            }
        }
        return cachedLabel;
    }
}

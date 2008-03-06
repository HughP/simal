package uk.ac.osswatch.simal.model.elmo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.openrdf.concepts.foaf.Document;
import org.openrdf.elmo.ElmoQuery;

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
   * Get a set of people that know this person.
   * 
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
      Set<org.openrdf.concepts.foaf.Person> people)
      throws SimalRepositoryException {
    Iterator<org.openrdf.concepts.foaf.Person> itr = people.iterator();
    Set<Person> result = new HashSet<Person>(people.size());

    Object person;
    while (itr.hasNext()) {
      person = getRepository().getManager().designateEntity(
          org.openrdf.concepts.foaf.Person.class, itr.next());
      result.add(new Person((org.openrdf.concepts.foaf.Person) person,
          getRepository()));
    }
    return result;
  }

  /**
   * Get the label for this person. If the person does not have a defined label
   * then the value returned by getGivennames is used. If this is null and fetch
   * label is set to true then attempt to find a label in the repository,
   * otherwise use the supplied default label (if not null) or the resource
   * return value of the toString() method.
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
          cachedLabel = getRepository().getLabel(elmoResource.getQName());
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

  /**
   * Get all the colleagues of this person. A colleague is defined as anyone who
   * works on the same project as this person.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public Set<IPerson> getColleagues() throws SimalRepositoryException {
    String queryStr = "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
        + "PREFIX doap: <http://usefulinc.com/ns/doap#> "
        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
        + "SELECT DISTINCT ?colleague WHERE { "
        + "?project rdf:type doap:Project . "
        + "?project doap:developer $qname . "
        + "{?project doap:developer ?colleague} UNION "
        + "{?project doap:documentor ?colleague} UNION " 
        + "{?project doap:helper ?colleague} UNION" 
        + "{?project doap:maintainer ?colleague} UNION" 
        + "{?project doap:tester ?colleague} UNION" 
        + "{?project doap:translator ?colleague} "
        + "}";
    ElmoQuery query = getRepository().getManager().createQuery(queryStr);
    query.setParameter("qname", this.getQName());
    Set<IPerson> colleagues = new HashSet<IPerson>();
    for (Object result : query.getResultList()) {
      colleagues.add(new Person((org.openrdf.concepts.foaf.Person) result,
          getRepository()));
    }
    return colleagues;
  }
}

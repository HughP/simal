package uk.ac.osswatch.simal.model.elmo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.openrdf.concepts.foaf.Person;
import org.openrdf.elmo.ElmoManager;
import org.openrdf.elmo.ElmoQuery;
import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.model.IFoafPersonBehaviour;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A behaviour for an Elmo object representing a FOAF Person.
 * 
 */
@rdf("http://xmlns.com/foaf/0.1/Person")
public class FoafPersonBehaviour extends FoafResourceBehaviour implements IFoafPersonBehaviour {
  private static final long serialVersionUID = -6234779132155536113L;

  /**
   * Create a behaviour for an Elmo object representing a FOAF Person.
   * 
   * @param simalTestProject
   */
  public FoafPersonBehaviour(Person elmoPerson) {
    super(elmoPerson);
  }
  
  /**
   * Get all the colleagues of this person. A colleague is defined as anyone who
   * works on the same project as this person.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public Set<IPerson> getColleagues() throws SimalRepositoryException {
    String queryStr = "PREFIX foaf: <" + SimalRepository.FOAF_NAMESPACE_URI + "> "
        + "PREFIX doap: <" + SimalRepository.DOAP_NAMESPACE_URI + "> "
        + "PREFIX rdf: <" + SimalRepository.RDF_NAMESPACE_URI + ">"
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
    ElmoManager elmoManager = elmoEntity.getElmoManager();
    ElmoQuery query = elmoManager.createQuery(queryStr);
    query.setParameter("qname", elmoEntity.getQName());
    Set<IPerson> colleagues = new HashSet<IPerson>();
    for (Object result : query.getResultList()) {
      colleagues.add(elmoManager.designateEntity(IPerson.class, result));
    }
    return colleagues;
  }

  public Set<IPerson> getKnows() {
    Iterator<Person> people = getFoafPerson().getFoafKnows().iterator();
    // TODO: the following code feels clumsy, is there a better way of doing this?
    Set<IPerson>result = new HashSet<IPerson>();
    while(people.hasNext()) {
      result.add((IPerson)people.next());
    }
    return result;
  }
  
  /**
   * Get the entity that this behaviour represents in the
   * form of a FoafPerson.
   * @return
   */
  private Person getFoafPerson() {
    return (Person)elmoEntity;
  }
}

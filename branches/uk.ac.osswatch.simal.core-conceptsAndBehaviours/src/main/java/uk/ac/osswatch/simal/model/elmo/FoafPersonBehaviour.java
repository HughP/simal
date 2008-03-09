package uk.ac.osswatch.simal.model.elmo;

import java.util.HashSet;
import java.util.Set;

import org.openrdf.concepts.foaf.Person;
import org.openrdf.elmo.ElmoManager;
import org.openrdf.elmo.ElmoQuery;

import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A behaviour for an Elmo object representing a FOAF Person.
 * 
 */
public class FoafPersonBehaviour extends FoafResourceBehaviour {
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
  public Set<Person> getColleagues() throws SimalRepositoryException {
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
    Set<Person> colleagues = new HashSet<Person>();
    for (Object result : query.getResultList()) {
      colleagues.add(elmoManager.designateEntity(Person.class, result));
    }
    return colleagues;
  }
}

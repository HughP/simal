package uk.ac.osswatch.simal.rest;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * API functionality for working with Person objects.
 *
 */
public class PersonAPI {
  String qname = "http://foo.org/~developer/#me";
  SimalRepository repo;

  /**
   * Create a PersonAPI that will operate on a given Simal
   * Repository.
   * 
   * @param repo
   */
  public PersonAPI(SimalRepository repo) {
    this.repo = repo;
  }

  public String getAllColleagues(HttpServletRequest req, String cmd)
      throws SimalAPIException {
    String response;
    StringBuffer result = new StringBuffer();
    if (cmd.endsWith(".json")) {
      Iterator<IPerson> colleagues = getAllColleagues(qname);
      while (colleagues.hasNext()) {
        result.append("{ \"items\": [");
        result.append(colleagues.next().toJSON(true));
        result.append("]}");
      }
    } else if (cmd.endsWith(".xml")) {
      Iterator<IPerson> colleagues = getAllColleagues(qname);
      result.append("<container>");
      result.append("<viewer>");
      result.append("<person id=\"john.doe\" name=\"FIXME: John Doe\"></person>");
      result.append("</viewer>");

      result.append("<viewerFriends>");
      IPerson person;
      while (colleagues.hasNext()) {
        person = colleagues.next();
        result.append("<person id=\"" + person.getSimalId() + "\" name=\"" + person.getFoafGivennames() + "\"></person>");
      }
      result.append("</viewerFriends>");
      result.append("</container>");
    } else {
      throw new SimalAPIException("Unkown format requested - "
          + req.getPathInfo());
    }
    response = result.toString();
    return response;
  }

  /**
   * Get all colleagues of a specified person.
   * @param qname
   * @return
   * @throws SimalRepositoryException
   */
  private Iterator<IPerson> getAllColleagues(String qname)
      throws SimalAPIException {
    IPerson person;
    try {
      person = repo.getPerson(new QName(qname));
    if (person == null) {
      throw new SimalAPIException("No person known with the QName " + qname);
    }
    Iterator<IPerson> colleagues = person.getColleagues().iterator();
    return colleagues;
    } catch (SimalRepositoryException e) {
      throw new SimalAPIException("Unable to get colleagues of " + qname, e);
    }
  }

}

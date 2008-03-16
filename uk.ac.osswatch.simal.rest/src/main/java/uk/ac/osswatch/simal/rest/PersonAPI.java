package uk.ac.osswatch.simal.rest;

import java.util.Iterator;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * API functionality for working with Person objects.
 *
 */
public class PersonAPI extends AbstractHandler {
  String qname = "http://foo.org/~developer/#me";

  /**
   * Create a PersonAPI that will operate on a given Simal
   * Repository. Handlers should not be instantiated directly,
   * use HandlerFactory.createHandler(...) instead.
   * 
   * @param repo
   */
  protected PersonAPI(SimalRepository repo) {
    super(repo);
  }


  /**
   * Execute a command.
   * 
   * @param cmd
   * @throws SimalAPIException 
   */
  public String execute(String cmd) throws SimalAPIException {
    if (cmd.contains(RESTServlet.COMMAND_ALL_COLLEAGUES)) {
      return getAllColleagues(cmd);
    } else {
      throw new SimalAPIException("Unkown command: " + cmd);
    }
  }

  /**
   * Get all the colleagues for this 
   * 
   * @param cmd
   * @return
   * @throws SimalAPIException
   */
  public String getAllColleagues(String cmd)
      throws SimalAPIException {
    int paramStart = cmd.indexOf(PARAM_PERSON_ID) + PARAM_PERSON_ID.length();
    String id = cmd.substring(paramStart, cmd.indexOf("/", paramStart));
    
    String response;
    StringBuffer result = new StringBuffer();
    IPerson person;
    Iterator<IPerson> colleagues = null;
    try {
      person = repo.findPersonById(id);
      colleagues = person.getColleagues().iterator();
    } catch (SimalRepositoryException e) {
      throw new SimalAPIException("Unable to get colleagues for person with id " + id, e);
    }
    
    if (cmd.endsWith(RESTServlet.JSON_SUFFIX)) {
      while (colleagues.hasNext()) {
        result.append("{ \"items\": [");
        result.append(colleagues.next().toJSON(true));
        result.append("]}");
      }
    } else if (cmd.endsWith(RESTServlet.XML_SUFFIX)) {
      result.append("<container>");
      result.append("<viewer>");
      result.append("<person id=\"" + person.getSimalId() + "\" name=\"" + person.getFoafGivennames() + "\"></person>");
      result.append("</viewer>");

      result.append("<viewerFriends>");
      IPerson colleague;
      while (colleagues.hasNext()) {
        colleague = colleagues.next();
        result.append("<person id=\"" + colleague.getSimalId() + "\" name=\"" + colleague.getFoafGivennames() + "\"></person>");
      }
      result.append("</viewerFriends>");
      result.append("</container>"); 
    } else {
      throw new SimalAPIException("Unkown format requested - "
          + cmd);
    }
    response = result.toString();
    return response;
  }

}

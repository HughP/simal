package uk.ac.osswatch.simal.rest;

import java.util.Iterator;
import java.util.Set;

import uk.ac.osswatch.simal.AbstractHandler;
import uk.ac.osswatch.simal.SimalAPIException;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * API functionality for working with Person objects.
 *
 */
public class PersonAPI extends AbstractHandler {
  protected static SimalRepository repo;

  /**
   * Create a PersonAPI that will operate on a given Simal
   * Repository. Handlers should not be instantiated directly,
   * use HandlerFactory.createHandler(...) instead.
   * 
   * @param repo
   */
  protected PersonAPI(SimalRepository repo) {
    super();
    this.repo = repo;
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
    Set<IPerson> colleaguesAndFriends;
    Iterator<IPerson> friends = null;
    try {
      person = repo.findPersonById(id);
      
      colleaguesAndFriends = person.getColleagues();
      colleaguesAndFriends.addAll(person.getKnows());
      friends = colleaguesAndFriends.iterator();
    } catch (SimalRepositoryException e) {
      throw new SimalAPIException("Unable to get colleagues for person with id " + id, e);
    }
    
    if (cmd.endsWith(RESTServlet.JSON_SUFFIX)) {
      while (friends.hasNext()) {
        result.append("{ \"items\": [");
        result.append(friends.next().toJSON(true));
        result.append("]}");
      }
    } else if (cmd.endsWith(RESTServlet.XML_SUFFIX)) {
      result.append("<container>");

      result.append("<people>");
      result.append("<person id=\"" + person.getSimalId() + "\" name=\"" + person.getFoafGivennames() + "\">");
      IPerson friend;
      while (friends.hasNext()) {
        friend = friends.next();
        result.append("<friend>");
        result.append(friend.getSimalId());
        result.append("</friend>");
      }
      result.append("</person>");
      
      friends = colleaguesAndFriends.iterator();
      while (friends.hasNext()) {
        friend = friends.next();
        result.append("<person id=\"" + friend.getSimalId() + "\" name=\"" + friend.getFoafGivennames() + "\">");
        result.append("</person>");
      }
      result.append("</people>");
      
      result.append("</container>"); 
    } else {
      throw new SimalAPIException("Unkown format requested - "
          + cmd);
    }
    response = result.toString();
    return response;
  }

}

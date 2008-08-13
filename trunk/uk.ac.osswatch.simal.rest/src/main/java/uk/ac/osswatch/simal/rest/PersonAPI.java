package uk.ac.osswatch.simal.rest;

/*
 * Copyright 2008 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");   *
 * you may not use this file except in compliance with the License.  *
 * You may obtain a copy of the License at                           *
 *                                                                   *
 *   http://www.apache.org/licenses/LICENSE-2.0                      *
 *                                                                   *
 * Unless required by applicable law or agreed to in writing,        *
 * software distributed under the License is distributed on an       *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY            *
 * KIND, either express or implied.  See the License for the         *
 * specific language governing permissions and limitations           *
 * under the License.                                                *
 */

import java.util.Iterator;
import java.util.Set;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * API functionality for working with Person objects.
 * 
 */
public class PersonAPI extends AbstractHandler {

  /**
   * Create a PersonAPI that will operate on a given Simal Repository. Handlers
   * should not be instantiated directly, use HandlerFactory.createHandler(...)
   * instead.
   * 
   * @param repo
   * @throws SimalAPIException
   */
  protected PersonAPI(RESTCommand cmd) {
    super(cmd);
  }

  /**
   * Execute a command.
   * 
   * @param cmd
   * @throws SimalAPIException
   */
  public String execute() throws SimalAPIException {
    if (command.isGetColleagues()) {
      return getAllColleagues(command);
    } else {
      throw new SimalAPIException("Unkown command: " + command);
    }
  }

  /**
   * Get all the colleagues for this
   * 
   * @param cmd
   * @return
   * @throws SimalAPIException
   */
  public String getAllColleagues(RESTCommand cmd)
      throws SimalAPIException {
    String id = cmd.getPersonID();
    
    String response;
    StringBuffer result = new StringBuffer();
    IPerson person;
    Set<IPerson> colleaguesAndFriends;
    Iterator<IPerson> friends = null;
    try {
      person = HandlerFactory.getSimalRepository().findPersonById(id);
      
      colleaguesAndFriends = person.getColleagues();
      colleaguesAndFriends.addAll(person.getKnows());
      friends = colleaguesAndFriends.iterator();
    
      if (cmd.isJSON()) {
        while (friends.hasNext()) {
          result.append("{ \"items\": [");
          result.append(friends.next().toJSON(true));
          result.append("]}");
        }
      } else if (cmd.isXML()) {
        result.append("<container>");
  
        result.append("<people>");
        result.append("<person id=\"" + person.getSimalID() + "\" name=\"" + person.getGivennames() + "\">");
        IPerson friend;
        while (friends.hasNext()) {
          friend = friends.next();
          result.append("<friend>");
          result.append(friend.getSimalID());
          result.append("</friend>");
        }
        result.append("</person>");
        
        friends = colleaguesAndFriends.iterator();
        while (friends.hasNext()) {
          friend = friends.next();
          result.append("<person id=\"" + friend.getSimalID() + "\" name=\"" + friend.getGivennames() + "\">");
          result.append("</person>");
        }
        result.append("</people>");
      
        result.append("</container>"); 
      } else {
        throw new SimalAPIException("Unkown format requested - "
            + cmd);
      }
    } catch (SimalRepositoryException e) {
      throw new SimalAPIException("Unable to get colleagues for person with id " + id, e);
    }
    response = result.toString();
    return response;
  }
}

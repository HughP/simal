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

import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Set;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;

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
   * @throws SimalRepositoryException
   * @throws SimalAPIException
   */
  protected PersonAPI(final RESTCommand cmd) throws SimalRepositoryException {
    super(cmd);
  }

  /**
   * Execute a command.
   * 
   * @param cmd
   * @throws SimalAPIException
   */
  public String execute() throws SimalAPIException {
    if (command.isGetPerson()) {
      return getPerson(command);
    } else if (command.isGetColleagues()) {
      return getAllColleagues(command);
    } else if (command.isGetAllPeople()) {
      return getAllPeople(command);
    } else {
      throw new SimalAPIException("Unkown command: " + command);
    }
  }

  /**
   * Get a person record.
   * 
   * @param cmd
   * @return
   * @throws SimalAPIException
   */
  public String getPerson(RESTCommand cmd) throws SimalAPIException {
    String id = command.getPersonID();
    String email = command.getPersonEMail();
	IPerson person = null;
	  
    try {
  	  if (id != null) {
	        person = SimalRepositoryFactory.getPersonService().findById(
	            getRepository().getUniqueSimalID(id));
	          if (person == null) {
	              throw new SimalAPIException("Person with Simal ID " + id
	           		  + " does not exist");
	            }
  	  } else if (email != null){
  		person = SimalRepositoryFactory.getPersonService().findBySha1Sum(RDFUtils.getSHA1(email));
          if (person == null) {
              throw new SimalAPIException("Person with email " + email
           		  + " does not exist");
            }
  	  } else {
  		  throw new SimalAPIException("Must provide sufficient parameters to identify a person, see REST API documentation for more details");
  	  }
    } catch (SimalRepositoryException e) {
      throw new SimalAPIException(
          "Unable to get XML representation of project from the repository",
          e);
    } catch (NoSuchAlgorithmException e) {
        throw new SimalAPIException(
                "Unable to generate an SHA1 hash of email: " + email,
                e);
	}

    if (command.isXML()) {
      try {
    	return person.toXML();
      } catch (SimalRepositoryException e) {
        throw new SimalAPIException(
            "Unable to get XML representation of project from the repository",
            e);
      }
    } else if (command.isJSON()) {
        return person.toJSON(false);
    } else {
      throw new SimalAPIException("Unkown format requested - " + command);
    }
  }

  /**
   * Get all the colleagues for an identified person.
   * 
   * @param cmd
   * @return
   * @throws SimalAPIException
   */
  public String getAllColleagues(final RESTCommand cmd)
      throws SimalAPIException {
    final String id = cmd.getPersonID();

    String response;
    StringBuffer result = new StringBuffer();
    IPerson person;
    Set<IPerson> colleaguesAndFriends;
    Iterator<IPerson> friends = null;
    try {
      person = SimalRepositoryFactory.getPersonService().findById(
          getRepository().getUniqueSimalID(id));

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
        result.append("<person id=\"" + person.getSimalID() + "\" name=\""
            + person.getGivennames() + "\">");
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
          result.append("<person id=\"" + friend.getSimalID() + "\" name=\""
              + friend.getGivennames() + "\">");
          result.append("</person>");
        }
        result.append("</people>");

        result.append("</container>");
      } else {
        throw new SimalAPIException("Unkown format requested - " + cmd);
      }
    } catch (SimalRepositoryException e) {
      throw new SimalAPIException(
          "Unable to get colleagues for person with id " + id, e);
    }
    response = result.toString();
    return response;
  }

  /**
   * Get all the people in the repository.
   * 
   * @param cmd
   * @return
   * @throws SimalAPIException
   */
  public String getAllPeople(final RESTCommand cmd) throws SimalAPIException {
    final String id = cmd.getPersonID();

    String response;
    try {
      if (cmd.isJSON()) {
        response = SimalRepositoryFactory.getPersonService().getAllAsJSON();
      } else {
        throw new SimalAPIException("Unknown data format: " + cmd.getFormat());
      }
    } catch (SimalRepositoryException e) {
      throw new SimalAPIException("Unable to get a person with id " + id, e);
    }
    return response;
  }
}

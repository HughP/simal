/*
 * Copyright 2007,2010 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.osswatch.simal.model;

import java.util.Set;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A person in the simal space. This records simal specific data about a person
 * over and above what is possible in FOAF.
 * 
 */

public interface IPerson extends IFoafResource {

  /**
   * Get a set of people who this person knows. They are deemed to know someone
   * if any of the following are true:
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
   * Set the Simal ID for this person. This is a unique identifier within the
   * repository from which it was retrieved. The value of the ID is any
   * alphanumeric string that does not contain a ':'.
   * 
   * @param newId
   *          any alphanumeric string that does not contain a ':'.
   * @return
   */
  public void setSimalID(String newId) throws SimalRepositoryException;

  /**
   * Get all the people that this person knows
   */
  public Set<IPerson> getKnows();

  /**
   * Get the default email address for this person.
   * 
   * @return
   */
  public Set<IInternetAddress> getEmail();

  /**
   * Get all given names for this person.
   * 
   * @return
   */
  public Set<String> getGivennames();

  /**
   * Get all the homepages for this person.
   * 
   * @return
   */
  public Set<IDocument> getHomepages();

  /**
   * Get all the projects that involve this person.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public Set<IProject> getProjects() throws SimalRepositoryException;

  /**
   * Get this person as a JSON record.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public String toJSONRecord() throws SimalRepositoryException;

  /**
   * Get the SHA1 sums of this persons known mailboxes.
   * 
   * @return
   */
  public Set<String> getSHA1Sums();

  /**
   * Assign an email address to this person. This method will automatically add
   * an SHA1 hash of the email.
   * 
   * @param email
   */
  public void addEmail(String email);

  /**
   * Add an SHA1 hash of an email address to this person.
   * 
   * @param sha1
   *          the hash to add
   */
  public void addSHA1Sum(String sha1);

}

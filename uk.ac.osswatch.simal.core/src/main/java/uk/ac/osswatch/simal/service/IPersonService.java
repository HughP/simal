package uk.ac.osswatch.simal.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/*
 * Copyright 2007 University of Oxford 
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

public interface IPersonService extends IService {
	

	  /**
	   * Get all the people known in this repository.
	   * 
	   * @return
	   * @throws SimalRepositoryException
	   */
	  public Set<IPerson> getAll() throws SimalRepositoryException;
	  
	  /**
	   * Get all the people in the repository and return them in a single JSON file.
	   * 
	   * @return
	   * @throws SimalRepositoryException
	   * @throws SimalRepositoryException
	   */
	  public String getAllAsJSON() throws SimalRepositoryException;
	  

	  /**
	   * Return all the people with a name that matches the
	   * supplied regular expression. The filter
	   * is not case sensitive.
	   * 
	   * 
	   * @param value
	   * @return
	   */
	  public Set<IPerson> filterByName(String filter);  
	  

	  /**
	   * Create a new person in the repository.
	   * 
	   * @return
	   * @throws SimalRepositoryException
	   *           if an error is thrown whilst communicating with the repository
	   * @throws DuplicateURIException
	   *           if an entity with the given String already exists
	   */
	  public IPerson create(String uri) throws SimalRepositoryException,
	      DuplicateURIException;
	  

	  /**
	   * Create a new person ID and save the next local value in the properties
	   * file.
	   * 
	   * @throws IOException
	   * @throws FileNotFoundException
	   */
	  public String getNewID() throws SimalRepositoryException;
	  

	  /**
	   * Check to see if a person already exists in the repository with the supplied
	   * EMail address. If they exist return the person otherwise return null.
	   * 
	   * @param person
	   * @return the duplicate person or null
	   * @throws SimalRepositoryException
	   */
	  public IPerson getDuplicate(String email) throws SimalRepositoryException;
	  

	  /**
	   * Get a person from the repository. If the person does not yet exist it is created.
	   * 
	   * @param personURI
	   * @return
	   * @throws SimalRepositoryException 
	   */
	   public IPerson getOrCreate(String uri) throws SimalRepositoryException;
	   

	   /**
	    * Get a person from the repository.
	    * 
	    * @param uri
	    *          the String of the repository to retrieve
	    * @return the repository or if no repository item with the given String
	    *         exists Null
	    * @throws SimalRepositoryException
	    */
	   public IPerson get(String uri) throws SimalRepositoryException;
	   
	   /**
	    * Get a person with a given Simal id.
	    * 
	    * @param id -
	    *          world unique Simal ID
	    * @return
	    * @throws SimalRepositoryException
	    *           if the ID is not a world unique one.
	    */
	   public IPerson findById(String id) throws SimalRepositoryException;

	   /**
	    * Get a person with a given MBOX SHA1 SUM.
	    * 
	    * @param sha1sum
	    * @return
	    * @throws SimalRepositoryException
	    */
	   public IPerson findBySha1Sum(String sha1sum)
	       throws SimalRepositoryException;

	   /**
	    * Get a person with a given rdf:seeAlso attribute.
	    * 
	    * @param seeAlso
	    * @return
	    * @throws SimalRepositoryException
	    */
	   public IPerson findBySeeAlso(String seeAlso)
	       throws SimalRepositoryException;

	   /**
	    * Get all the colleagues of this person. A colleague is defined as anyone who
	    * works on the same project as this person.
	    * 
	    * @param person to find colleagues of
	    * @return
	    * @throws SimalRepositoryException
	    */
	   public Set<IPerson> getColleagues(IPerson person) throws SimalRepositoryException;

}

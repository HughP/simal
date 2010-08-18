package uk.ac.osswatch.simal.service;
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

import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;


/**
 * A class for working with projects in the repository.
 * 
 * @refactor appropriate methods in SimalRepository into this class
 *
 */
public interface IHomepageService extends IService {
	   /**
	    * Get a homepage from the repository. If the homepage does not yet exist it is created.
	    * 
	    * @param url of the homepage
	    * @return
	    * @throws SimalRepositoryException 
	    */
	    public IDoapHomepage getOrCreate(String url) throws SimalRepositoryException;
	    
	    /**
	     * Create a new homepage in the repository.
	     * 
	     * @param uri a URI to identify this homepage
	     * @return
	     * @throws SimalRepositoryException
	     *           if an error is thrown whilst communicating with the repository
	     * @throws DuplicateURIException
	     *           if an entity with the given String already exists
	     */
	    public IDoapHomepage create(String uri) throws SimalRepositoryException,
	        DuplicateURIException;

	    /**
	     * Create a new homepage ID and save the next value in the properties file.
	     * 
	     */
	    public String getNewID() throws SimalRepositoryException;
}

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
import java.io.FileNotFoundException;
import java.io.IOException;

import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;


/**
 * A class for working with projects in the repository.
 * 
 * @TODO refactor appropriate methods in SimalRepository into this class
 *
 */
public interface IHomepageService {
	   /**
	    * Get a homepage from the repository. If the homepage does not yet exist it is created.
	    * 
	    * @param url of the homepage
	    * @return
	    * @throws SimalRepositoryException 
	    */
	    public IHomepageService getOrCreateHomepage(String url) throws SimalRepositoryException;
	    
	    /**
	     * Create a new project in the repository.
	     * 
	     * @return
	     * @throws SimalRepositoryException
	     *           if an error is thrown whilst communicating with the repository
	     * @throws DuplicateURIException
	     *           if an entity with the given String already exists
	     */
	    public IDoapHomepage createHomepage(String uri) throws SimalRepositoryException,
	        DuplicateURIException;

	    /**
	     * Create a new homepage ID and save the next value in the properties file.
	     * 
	     * @throws IOException
	     * @throws FileNotFoundException
	     */
	    public String getNewHomepageID() throws SimalRepositoryException;
}

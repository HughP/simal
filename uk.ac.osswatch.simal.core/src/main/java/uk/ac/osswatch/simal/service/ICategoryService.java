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

import java.util.Set;

import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public interface ICategoryService extends IService {

	/**
	 * Create a new organisation entity.
	 * 
	 * @param uri
	 * @return
	 * @throws DuplicateURIException
	 * @throws SimalRepositoryException
	 */
	public IDoapCategory create(String uri) throws DuplicateURIException,
			SimalRepositoryException;

	/**
	 * Get a category from the repository. If the category does not yet exist
	 * return null.
	 * 
	 * @param uri
	 *            the String of the category to retrieve
	 * @return the category, or if no category with the given String exists Null
	 * @throws SimalRepositoryException
	 */
	public IDoapCategory get(String uri) throws SimalRepositoryException;

	/**
	 * Get a project from the repository. If the project does not yet exist it
	 * will be created.
	 * 
	 * @param uri
	 *            the String of the category to retrieve or create
	 * @return the category resource
	 * @throws SimalRepositoryException
	 */
	public IDoapCategory getOrCreate(String uri)
			throws SimalRepositoryException;

	/**
	 * Get a category with a given simal id.
	 * 
	 * @param id
	 * @return
	 * @throws SimalRepositoryException
	 */
	public IDoapCategory findById(String id)
			throws SimalRepositoryException;
	
	  /**
	   * Get all the categories known in this repository.
	   * 
	   * @return
	   * @throws SimalRepositoryException
	   */
	  public Set<IDoapCategory> getAll() throws SimalRepositoryException;
	  
	  /**
	   * Create a new category ID and save the next value in the properties file.
	   * 
	   */
	  public String getNewID() throws SimalRepositoryException;

}

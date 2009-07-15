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

import uk.ac.osswatch.simal.model.IDoapRepository;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A repository service provides methods for working with revision control repository objects.
 * 
 */
public interface IRepositoryService extends IService {

	/**
	 * Create a new Revision Control Repository.
	 * 
	 * @param uri
	 * @return
	 * @throws SimalRepositoryException
	 * @throws DuplicateURIException
	 */
	public IDoapRepository create(String uri) throws SimalRepositoryException,
			DuplicateURIException;

	/**
	 * Get a new ID for a Revision Control repository.
	 * @return
	 * @throws SimalRepositoryException
	 */
	public String getNewID() throws SimalRepositoryException;

	/**
	 * Get a revision control repository by Simal ID
	 *
	 * @param fullID
	 * @return
	 */
	public IDoapRepository getById(String fullID);

}

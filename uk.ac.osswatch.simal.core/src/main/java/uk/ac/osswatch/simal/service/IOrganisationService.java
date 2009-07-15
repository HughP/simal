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

import uk.ac.osswatch.simal.model.IOrganisation;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public interface IOrganisationService extends IService {

	/**
	 * Create a new organisation entity.
	 * 
	 * @param uri
	 * @return
	 * @throws DuplicateURIException
	 * @throws SimalRepositoryException
	 */
	public IOrganisation create(String uri) throws DuplicateURIException,
			SimalRepositoryException;

	/**
	 * Create a new ID for an organisation entitity.
	 * 
	 * @return
	 * @throws SimalRepositoryException
	 */
	public String getNewOrganisationID() throws SimalRepositoryException;

	public IOrganisation getOrganisationById(String fullID);

	/**
	 * Get all the organisations known in this repository.
	 * 
	 * @return
	 * @throws SimalRepositoryException
	 */
	public Set<IOrganisation> getAll() throws SimalRepositoryException;

	/**
	 * Get an organisation from the repository. If the organisation does not
	 * exist then return null.
	 * 
	 * @param uri
	 *            the URI of the organisation to retrieve
	 * @return the organisation, or if no project with the given String exists
	 *         Null
	 * @throws SimalRepositoryException
	 */
	public IOrganisation get(String uri) throws SimalRepositoryException;

}

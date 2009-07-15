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

import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.simal.IReview;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;


/**
 * A class for working with projects in the repository.
 * 
 * @TODO refactor appropriate methods in SimalRepository into this class
 *
 */
public interface IReviewService extends IService {
	
	/**
	 * Get all Reviews in the repository.
	 * 
	 * @return
	 */
	public Set<IReview> getReviews();
	
	/**
	 * Get all reviews associated with a project. A review represents a manual review
	 * of the data assigned to this project.
	 * 
	 * @return
	 * @throws SimalRepositoryException 
	 */
	public Set<IReview> getReviewsForProject(IProject project) throws SimalRepositoryException;

	/**
	 * Get a Simal ID for a new Review model.
	 * @return
	 * @throws SimalRepositoryException 
	 */
	public String getNewReviewID() throws SimalRepositoryException;
	
	/**
	 * Get the review with the supplied ID.
	 * @param id
	 * @return
	 * @throws SimalRepositoryException is thrown if there is no such review or if thre is more
	 * than one review with the ID (which should never happen)
	 */
	public IReview findReviewById(String id) throws SimalRepositoryException;

	/**
	 * Create a new review entity.
	 * 
	 * @param uri
	 * @return
	 * @throws DuplicateURIException 
	 * @throws SimalRepositoryException 
	 */
	public IReview create(String uri) throws DuplicateURIException, SimalRepositoryException;

	/**
	 * Get a specific review. if it does not exist in the reporisitory return null.
	 * @param uri
	 * @return
	 */
	public IReview getReview(String uri);
}

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
import java.util.Set;

import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;


/**
 * A class for working with projects in the repository.
 * 
 * @TODO refactor appropriate methods in SimalRepository into this class
 *
 */
public interface IProjectService {
	
	/**
	 * Get all projects in the repository that have a revision control system recorded.
	 * 
	 * @return
	 * @throws SimalRepositoryException 
	 */
	public Set<IProject> getProjectsWithRCS() throws SimalRepositoryException;

	/**
	 * Get all projects in the repository that have a homepage recorded.
	 * 
	 * @return
	 * @throws SimalRepositoryException 
	 */
	public Set<IProject> getProjectsWithHomepage() throws SimalRepositoryException;

	/**
	 * Get all projects in the repository that have a mailing list recorded.
	 * 
	 * @return
	 * @throws SimalRepositoryException 
	 */
	public Set<IProject> getProjectsWithMailingList() throws SimalRepositoryException;

	/**
	 * Get all projects in the repository that have a Maintainer recorded.
	 * 
	 * @return
	 * @throws SimalRepositoryException 
	 */
	public Set<IProject> getProjectsWithMaintainer() throws SimalRepositoryException;

	/**
	 * Get all projects in the repository that have a release recorded.
	 * 
	 * @return
	 * @throws SimalRepositoryException 
	 */
	public Set<IProject> getProjectsWithRelease() throws SimalRepositoryException;
	
	/**
	 * Get all Bug database entries recorded in the repository.
	 * @return
	 * @throws SimalRepositoryException 
	 */
	public Set<IProject> getProjectsWithBugDatabase() throws SimalRepositoryException;

	/**
	 * Get a project from the repository. Return null if the project does not exist.
	 * @param uri
	 * @return
	 * @throws SimalRepositoryException 
	 */
	public IProject getProject(String uri) throws SimalRepositoryException;
	  
	  /**
	   * Get a project with a given rdf:seeAlso value.
	   * 
	   * @param seeAlso
	   * @return
	   * @throws SimalRepositoryException
	   */
	  public IProject findProjectBySeeAlso(String seeAlso)
	      throws SimalRepositoryException;

	  /**
	   * Find all projects returned using a SPARQL query.
	   * 
	   * @param queryStr
	   * @return
	   */
	  public Set<IProject> findProjectsBySPARQL(String queryStr);

	  /**
	   * Find all projects that have a review assigned to it.
	   * 
	   * @return
	   * @throws SimalRepositoryException 
	   */
  	  public Set<IProject> getProjectsWithReview() throws SimalRepositoryException;

  	  /**
  	   * Tests to see if a project already exists in the repository.
  	   */
	  public boolean containsProject(String uri); 

	   /**
	    * Get a project from the repository. If the project does not yet exist it is created.
	    * 
	    * @param personURI
	    * @return
	    * @throws SimalRepositoryException 
	    */
	    public IProject getOrCreateProject(String uri) throws SimalRepositoryException;
	    
	    /**
	     * Create a new project in the repository.
	     * 
	     * @return
	     * @throws SimalRepositoryException
	     *           if an error is thrown whilst communicating with the repository
	     * @throws DuplicateURIException
	     *           if an entity with the given String already exists
	     */
	    public IProject createProject(String uri) throws SimalRepositoryException,
	        DuplicateURIException;

	    /**
	     * Create a new project ID and save the next value in the properties file.
	     * 
	     * @throws IOException
	     * @throws FileNotFoundException
	     */
	    public String getNewProjectID() throws SimalRepositoryException;

	    /**
	     * Get a project with a given simal id.
	     * 
	     * @param id
	     * @return
	     * @throws SimalRepositoryException
	     */
	    public IProject getProjectById(String id) throws SimalRepositoryException;
}
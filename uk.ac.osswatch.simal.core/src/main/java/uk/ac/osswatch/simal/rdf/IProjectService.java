package uk.ac.osswatch.simal.rdf;
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
	 */
	public Set<IProject> getProjectsWithRCS();

	/**
	 * Get all projects in the repository that have a homepage recorded.
	 * 
	 * @return
	 */
	public Set<IProject> getProjectsWithHomepage();

	/**
	 * Get all projects in the repository that have a mailing list recorded.
	 * 
	 * @return
	 */
	public Set<IProject> getProjectsWithMailingList();

	/**
	 * Get all projects in the repository that have a Maintainer recorded.
	 * 
	 * @return
	 */
	public Set<IProject> getProjectsWithMaintainer();

	/**
	 * Get all projects in the repository that have a release recorded.
	 * 
	 * @return
	 */
	public Set<IProject> getProjectsWithRelease();
	
}

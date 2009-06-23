package uk.ac.osswatch.simal.service.jcr;
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
import uk.ac.osswatch.simal.rdf.IProjectService;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.AbstractService;

public class JcrProjectService extends AbstractService implements
		IProjectService {

	public boolean containsProject(String uri) {
		// TODO Auto-generated method stub
		return false;
	}

	public IProject findProjectBySeeAlso(String seeAlso)
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IProject> findProjectsBySPARQL(String queryStr) {
		// TODO Auto-generated method stub
		return null;
	}

	public IProject getProject(String uri) throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IProject> getProjectsWithBugDatabase()
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IProject> getProjectsWithHomepage()
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IProject> getProjectsWithMailingList()
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IProject> getProjectsWithMaintainer()
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IProject> getProjectsWithRCS() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IProject> getProjectsWithRelease()
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IProject> getProjectsWithReview()
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

}

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
import java.net.URISyntaxException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.jcr.JcrSimalRepository;
import uk.ac.osswatch.simal.model.jcr.Project;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;
import uk.ac.osswatch.simal.service.AbstractService;
import uk.ac.osswatch.simal.service.IProjectService;

public class JcrProjectService extends AbstractService implements
		IProjectService {
  public static final Logger logger = LoggerFactory
      .getLogger(JcrProjectService.class);

	public JcrProjectService(ISimalRepository simalRepository) {
		setRepository(simalRepository);
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

	public boolean containsProject(String uri) {
		// TODO Auto-generated method stub
		return false;
	}

	public IProject createProject(String uri) throws SimalRepositoryException,
			DuplicateURIException {
	    if (containsProject(uri)) {
		      throw new DuplicateURIException(
		          "Attempt to create a second project with the URI " + uri);
		    }
		    String simalProjectURI;
		    if (!uri.startsWith(RDFUtils.PROJECT_NAMESPACE_URI)) {
			    String projectID = getNewProjectID();
			    simalProjectURI = RDFUtils.getDefaultProjectURI(projectID);
			    logger.debug("Creating a new Simal Projectinstance with URI: "
			        + simalProjectURI);
		    } else {
		        simalProjectURI = uri;
		    }

		    Project project;
			try {
				project = new Project(uri);
			} catch (URISyntaxException e) {
				throw new SimalRepositoryException("Unable to create a new project object", e);
			}
		    project.setSimalID(getNewProjectID());
		    ((JcrSimalRepository)SimalRepositoryFactory.getInstance()).getObjectContentManager().insert(project);
		    
		    return project;
	}

	public String getNewProjectID() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IProject getOrCreateProject(String uri)
			throws SimalRepositoryException { 
		if (SimalRepositoryFactory.getInstance().containsResource(uri)) {
			return getProject(uri);
		} else {
			IProject project = findProjectBySeeAlso(uri);
			if (project == null) {
				try {
					return createProject(uri);
				} catch (DuplicateURIException e) {
					logger.error("Threw a DuplicateURIEception when we had already checked for resource existence", e);
					return null;
				}
			} else {
				return project;
			}
		}
	}

	public IProject getProjectById(String id) throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

}

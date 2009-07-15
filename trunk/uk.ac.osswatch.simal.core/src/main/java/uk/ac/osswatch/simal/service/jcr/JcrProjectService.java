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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.query.Filter;
import org.apache.jackrabbit.ocm.query.Query;
import org.apache.jackrabbit.ocm.query.QueryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalProperties;
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
		super(simalRepository);
	}

	public IProject findProjectBySeeAlso(String seeAlso)
			throws SimalRepositoryException {
		ObjectContentManager ocm = ((JcrSimalRepository)SimalRepositoryFactory.getInstance()).getObjectContentManager();
		QueryManager queryManager = ocm.getQueryManager();

		Filter filter = queryManager.createFilter(Project.class);
		filter.addEqualTo("seeAlso", seeAlso); 

		Query query = queryManager.createQuery(filter);
		return (IProject) ocm.getObject(query);
	}

	public IProject getProject(String uri) throws SimalRepositoryException {
		ObjectContentManager ocm = ((JcrSimalRepository)SimalRepositoryFactory.getInstance()).getObjectContentManager();
		QueryManager queryManager = ocm.getQueryManager();

		Filter filter = queryManager.createFilter(Project.class);
		filter.addEqualTo("URI", uri);

		Query query = queryManager.createQuery(filter);
		return (IProject) ocm.getObject(query);
	}

	public Set<IProject> getProjectsWithBugDatabase()
			throws SimalRepositoryException {
		ObjectContentManager ocm = ((JcrSimalRepository)SimalRepositoryFactory.getInstance()).getObjectContentManager();
		QueryManager queryManager = ocm.getQueryManager();

		Filter filter = queryManager.createFilter(Project.class);
		
		Query query = queryManager.createQuery(filter);
		// FIXME: The NotNull filter is not working is there a better way to do this? See http://markmail.org/message/wssnpbeftf6gcuzp
		Iterator<IProject> itr = ((Collection<IProject>)ocm.getObjects(query)).iterator();
		HashSet<IProject> projects = new HashSet<IProject>();
		while (itr.hasNext()) {
			IProject project = itr.next();
			if (project.getIssueTrackers().size() > 0) {
				projects.add(project);
			}
		}
		return projects;
	}

	public Set<IProject> getProjectsWithHomepage()
			throws SimalRepositoryException {
		ObjectContentManager ocm = ((JcrSimalRepository)SimalRepositoryFactory.getInstance()).getObjectContentManager();
		QueryManager queryManager = ocm.getQueryManager();

		Filter filter = queryManager.createFilter(Project.class);
		
		Query query = queryManager.createQuery(filter);
		// FIXME: The NotNull filter is not working is there a better way to do this? See http://markmail.org/message/wssnpbeftf6gcuzp
		Iterator<IProject> itr = ((Collection<IProject>)ocm.getObjects(query)).iterator();
		HashSet<IProject> projects = new HashSet<IProject>();
		while (itr.hasNext()) {
			IProject project = itr.next();
			if (project.getHomepages().size() > 0) {
				projects.add(project);
			}
		}
		return projects;
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
		ObjectContentManager ocm = ((JcrSimalRepository)SimalRepositoryFactory.getInstance()).getObjectContentManager();
		QueryManager queryManager = ocm.getQueryManager();

		Filter filter = queryManager.createFilter(Project.class);
		
		Query query = queryManager.createQuery(filter);
		// FIXME: The NotNull filter is not working is there a better way to do this? See http://markmail.org/message/wssnpbeftf6gcuzp
		Iterator<IProject> itr = ((Collection<IProject>)ocm.getObjects(query)).iterator();
		HashSet<IProject> projects = new HashSet<IProject>();
		while (itr.hasNext()) {
			IProject project = itr.next();
			if (project.getRepositories().size() > 0) {
				projects.add(project);
			}
		}
		return projects;
	}

	public Set<IProject> getProjectsWithRelease()
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IProject> getProjectsWithReview()
			throws SimalRepositoryException {
		ObjectContentManager ocm = ((JcrSimalRepository)SimalRepositoryFactory.getInstance()).getObjectContentManager();
		QueryManager queryManager = ocm.getQueryManager();

		Filter filter = queryManager.createFilter(Project.class);

		// FIXME: The NotNull filter is not working is there a better way to do this? See http://markmail.org/message/wssnpbeftf6gcuzp
		Query query = queryManager.createQuery(filter);
		Collection<IProject> projects = (Collection<IProject>)ocm.getObjects(query);
		return new HashSet<IProject>(projects);
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

		    IProject project = new Project("/project/" + getRepository().getEntityID(getNewProjectID()));
		    project.setURI(uri);
		    ObjectContentManager ocm = ((JcrSimalRepository)SimalRepositoryFactory.getInstance()).getObjectContentManager();
		    ocm.insert(project);
		    ocm.save();
		    
		    return project;
	}

	public String getNewProjectID() throws SimalRepositoryException {
	    String fullID = null;
	    String strEntityID = SimalProperties.getProperty(
	        SimalProperties.PROPERTY_SIMAL_NEXT_PROJECT_ID, "1");
	    long entityID = Long.parseLong(strEntityID);

	    /**
	     * If the properties file is lost for any reason the next ID value will be
	     * lost. We therefore need to perform a sanity check that this is unique.
	     * 
	     * @FIXME: the ID should really be held in the database then there would be
	     *         no need for this time consuming verification See ISSUE 190
	     */
	    boolean validID = false;
	    while (!validID) {
	      fullID = SimalRepositoryFactory.getInstance().getUniqueSimalID("prj" + Long.toString(entityID));
	      logger.debug("Checking to see if project ID of {} is available", fullID);
	      if (getProjectById(fullID) == null) {
	        validID = true;
	      } else {
	        entityID = entityID + 1;
	      }
	    }

	    long newId = entityID + 1;
	    SimalProperties.setProperty(SimalProperties.PROPERTY_SIMAL_NEXT_PROJECT_ID,
	        Long.toString(newId));
	    try {
	      SimalProperties.save();
	    } catch (Exception e) {
	      logger.warn("Unable to save properties file", e);
	      throw new SimalRepositoryException(
	          "Unable to save properties file when creating the next project ID", e);
	    }
	    logger.debug("Generated new project ID {}", fullID);
	    return fullID;
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

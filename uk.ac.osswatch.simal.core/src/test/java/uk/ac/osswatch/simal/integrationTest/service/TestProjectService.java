package uk.ac.osswatch.simal.integrationTest.service;
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
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.integrationTest.model.repository.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.IProjectService;

public class TestProjectService extends BaseRepositoryTest {

    private static final Logger logger = LoggerFactory
	      .getLogger(TestProjectService.class);
	
	@Test
	public void testGetProjectsWithRCS() throws SimalRepositoryException {
		IProjectService service = SimalRepositoryFactory.getProjectService();
		Set<IProject> projects = service.getProjectsWithRCS();
		
		Iterator<IProject> itr = projects.iterator();
		while (itr.hasNext()) {
			IProject project = itr.next();
			System.out.println(project + " has an RCS: " + project.getRepositories());
		}
		
		assertEquals("Got incorrect number of projects with RCS", 2, projects.size());
	}
	
	@Test
	public void testGetProjectsWithoutRCS() throws SimalRepositoryException {
		IProjectService service = SimalRepositoryFactory.getProjectService();
		Set<IProject> projects = service.getProjectsWithoutRCS();
		
		Iterator<IProject> itr = projects.iterator();
		while (itr.hasNext()) {
			IProject project = itr.next();
			System.out.println(project + " has no RCS: " + project.getRepositories());
		}
		
		assertEquals("Got incorrect number of projects without RCS", 7, projects.size());
	}

	@Test
	public void testGetProjectsById() throws SimalRepositoryException {
		IProjectService service = SimalRepositoryFactory.getProjectService();
		IProject project = service.getProjectById(testProjectID);
		assertEquals("Got incorect project by ID","Simal DOAP Test", project.getName());
		
		logger.debug("Project RDF is:\n" + project.toXML());
		
		String shortID = testProjectID.substring(testProjectID.lastIndexOf("-") + 1);
		project = service.getProjectById(shortID);
		assertEquals("Got incorect project by ID","Simal DOAP Test", project.getName());
	}

	@Test
	public void testGetProjectsWithHomepage() throws SimalRepositoryException {
		IProjectService service = SimalRepositoryFactory.getProjectService();
		Set<IProject> projects = service.getProjectsWithHomepage();
		Iterator<IProject> itr = projects.iterator();
		while (itr.hasNext()) {
			IProject project = itr.next();
			System.out.println(project + " has homepages: " + project.getHomepages());
		}
	    assertEquals("Got incorect number of projects with Homepage", 8, projects.size());
	}

	@Test
	public void testGetProjectsWithoutHomepage() throws SimalRepositoryException {
		IProjectService service = SimalRepositoryFactory.getProjectService();
		Set<IProject> projects = service.getProjectsWithoutHomepage();
		Iterator<IProject> itr = projects.iterator();
		while (itr.hasNext()) {
			IProject project = itr.next();
			System.out.println(project + " has no homepages: " + project.getHomepages());
		}
	    assertEquals("Got incorect number of projects with Homepage", 1, projects.size());
	}

	@Test
	public void testGetProjectsWithMaintainer() throws SimalRepositoryException {
		IProjectService service = SimalRepositoryFactory.getProjectService();
		Set<IProject> projects = service.getProjectsWithMaintainer();
		assertEquals("Got incorect number of projects with Mainteiner", 7, projects.size());
	}
 
	@Test
	public void testGetProjectsWithMailingList() throws SimalRepositoryException {
		IProjectService service = SimalRepositoryFactory.getProjectService();
		Set<IProject> projects = service.getProjectsWithMailingList();
		assertEquals("Got incorect number of projects with MailingList", 4, projects.size());
	}

	@Test
	public void testGetProjectsWithBugDatabase() throws SimalRepositoryException {
		IProjectService service = SimalRepositoryFactory.getProjectService();
		Set<IProject> projects = service.getProjectsWithBugDatabase();
		assertEquals("Got incorect number of projects with BugDatabaseList", 4, projects.size());
	}

	@Test
	public void testGetProjectsWithReview() throws SimalRepositoryException {
		IProjectService service = SimalRepositoryFactory.getProjectService();
		Set<IProject> projects = service.getProjectsWithReview();
		Iterator<IProject> itr = projects.iterator();
		while (itr.hasNext()) {
			IProject project = itr.next();
			System.out.println(project + " has at least one review: " + project.getHomepages());
		}
		assertEquals("Got incorect number of projects with a review", 1, projects.size());
	}

	@Test
	public void testGetProjectsWithoutReview() throws SimalRepositoryException {
		IProjectService service = SimalRepositoryFactory.getProjectService();
		Set<IProject> projects = service.getProjectsWithoutReview();
		Iterator<IProject> itr = projects.iterator();
		while (itr.hasNext()) {
			IProject project = itr.next();
			System.out.println(project + " has no reviews: " + project.getHomepages());
		}
		assertEquals("Got incorect number of projects without review", 9, projects.size());
	}
	
	@Test
	public void testGetProject() throws SimalRepositoryException {
		IProjectService service = SimalRepositoryFactory.getProjectService();
		IProject project = service.getProject(testProjectURI);
		assertNotNull("Failed to get the test project from the repository", project);
	}
	

	  @Test
	  public void testFindProject() throws SimalRepositoryException,
	      URISyntaxException {
	    logger.debug("Starting testFindProject()");
	    String uri = "http://foo.org/nonExistent";
	    IProject project = SimalRepositoryFactory.getProjectService().getProject(uri);
	    assertNull(project);

	    // test a known valid file
	    project = SimalRepositoryFactory.getProjectService().findProjectBySeeAlso(TEST_PROJECT_URI);
	    assertEquals("Simal DOAP Test", project.getName());
	    logger.debug("Finished testFindProject()");
	  }
	  
	  @Test
	  public void testFindProjectById() throws SimalRepositoryException {
	    IProject project = SimalRepositoryFactory.getProjectService().getProjectById(testProjectID);
	    assertNotNull("Failed to get project with ID " + testProjectID, project);
	  }

	  @Test
	  public void testFindProjectBySeeAlso() throws SimalRepositoryException {
	    logger.debug("Starting testFindProjectBySeeAlso()");
	    IProject project = SimalRepositoryFactory.getProjectService().findProjectBySeeAlso(project1SeeAlso);
	    assertNotNull(project);
	    logger.debug("Finished testFindProjectBySeeAlso()");
	  }
	  

	  @Test
	  public void testFilterProjectsByName() throws SimalRepositoryException {
	    // Test exact Match
	    Set<IProject> projects = SimalRepositoryFactory.getProjectService().filterByName("Simal Project Catalogue System");
	    assertEquals("Not the right number of projects with the name 'Simal Project Catalogue System'", 1, projects.size());
	    
	    // Test wildcard match
	    projects = SimalRepositoryFactory.getProjectService().filterByName("Simal");
	    assertEquals("Not the right number of projects match the filter 'Simal'", 4, projects.size());
	  }




}

package uk.ac.osswatch.simal.integrationTest.model.repository.jcr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IDoapRepository;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.IProjectService;

public class JcrProjectServiceTest extends JcrRepositoryBaseTests {

	/*
	private String seeAlsoURI = "http://foo.org/seeAlso";
	
	@Test
	public void findBySeeAlso() throws SimalRepositoryException {
		IProjectService service = getService();
		assertEquals("Got incorrect number of projects referencing the see also project", 1, service.findProjectBySeeAlso(seeAlsoURI ));
	}
	*/
	
	@Test
	public void getProject() throws SimalRepositoryException {
		IProjectService service = getService();
		IProject project = service.getProject(detailedProjectURI);
		assertNotNull("Unable to get the detailed project from the repository", project);

		Set<IDoapRepository> repos = project.getRepositories();
		assertEquals("Retrieved project does not have a repository record", 1, repos.size());
		
		Set<IDoapHomepage> pages = project.getHomepages();
		assertEquals("Retrieved project does not have a homepages record", 1, pages.size());
	}
	
	@Test
	public void getProjectsWithRCS() throws SimalRepositoryException {
		IProjectService service = getService();
		assertEquals("Got incorrect number of projects with RCS", 1, service.getProjectsWithRCS().size());
	}
	
	@Test
	public void getProjectsWithHomepages() throws SimalRepositoryException {
		IProjectService service = getService();
		assertEquals("Got incorrect number of projects with homepage", 1, service.getProjectsWithHomepage().size());
	}
	
	@Test
	public void getProjectsWithIssueTrackers() throws SimalRepositoryException {
		IProjectService service = getService();
		assertEquals("Got incorrect number of projects with issueTrackers", 1, service.getProjectsWithBugDatabase().size());
	}

	@Test
	public void getAllProjects() throws SimalRepositoryException {
		Set<IProject> projects = repo.getAllProjects();
		assertEquals("Got an incorrect number of projects", 2, projects.size());
		Iterator<IProject> itr = projects.iterator();
		while (itr.hasNext()) {
			IProject project = itr.next();
			logger.debug("Got a project with uri " + project.getURI());
		}
	}
	
	private IProjectService getService() throws SimalRepositoryException {
		IProjectService service = SimalRepositoryFactory.getProjectService();
		return service;
	}
}

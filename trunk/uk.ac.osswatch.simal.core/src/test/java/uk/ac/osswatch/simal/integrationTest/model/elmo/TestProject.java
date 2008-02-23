package uk.ac.osswatch.simal.integrationTest.model.elmo;

import static org.junit.Assert.*;

import java.util.Set;

import javax.xml.namespace.QName;

import org.junit.Test;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IDoapResource;
import uk.ac.osswatch.simal.model.elmo.Project;
import uk.ac.osswatch.simal.rdf.DuplicateQNameException;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestProject extends BaseRepositoryTest {

	@Test
	public void testGetQName() {
		assertEquals(TEST_SIMAL_PROJECT_QNAME, project1.getQName().getNamespaceURI());
	}

	@Test
	public void testToJSON() throws SimalRepositoryException {
		resetTestData();
		
		String json = project1.toJSONRecord();
		assertTrue(
				"JSON file does not contain correct JSON representation of the Simal test record",
				json.contains("\"name\":\"" + TEST_SIMAL_PROJECT_NAME));
		assertTrue(
				"JSON file does not contain correct JSON representation of the Simal test record",
				json.contains("\",\"shortdesc\":\"" + TEST_SIMAL_PROJECT_SHORT_DESC));
		assertTrue(
				"JSON file does not contain correct JSON representation of the Simal test project",
				!json.startsWith("{ \"items\": ["));
		
		json = project1.toJSON();
		assertTrue(
				"JSON file does not contain correct JSON representation of the Simal test project",
				json.startsWith("{ \"items\": ["));
	}
	
	@Test
	public void testGetIssueTracker() {
		String issueTrackers = project1.getIssueTrackers().toString();
		assertTrue(issueTrackers.contains(TEST_SIMAL_PROJECT_ISSUE_TRACKER));
	}
	
	@Test
	public void testGetCategories() {
		assertTrue(project1.getCategories().toString().contains(TEST_SIMAL_PROJECT_CATEGORY_ONE));
		assertTrue(project1.getCategories().toString().contains(TEST_SIMAL_PROJECT_CATEGORY_TWO));
	}
	
	@Test
	public void testGetDevelopers() throws SimalRepositoryException {
		assertEquals(TEST_SIMAL_PROJECT_DEVELOPERS, project1.getDevelopers().toString());
	}
	
	@Test
	public void testGetDocumentors() throws SimalRepositoryException {
		assertEquals(TEST_SIMAL_PROJECT_DOCUMENTORS, project1.getDocumenters().toString());
	}
	
	@Test
	public void testGetDownloadMirrors() {
		String mirrors = project1.getDownloadMirrors().toString();
		assertTrue(mirrors .contains(TEST_SIMAL_PROJECT_DOWNLOAD_MIRRORS));
	}
	
	@Test
	public void testGetDownloadPages() {
		String downloads = project1.getDownloadPages().toString();
		assertTrue(downloads.contains(TEST_SIMAL_PROJECT_DOWNLOAD_PAGES));
	}
	
	@Test
	public void testGetHelpers() throws SimalRepositoryException {
		assertEquals(TEST_SIMAL_PROJECT_HELPERS, project1.getHelpers().toString());
	}
	
	@Test
	public void testGetHomepages() {
		String homepages = project1.getHomepages().toString();
		assertTrue(homepages.contains(TEST_SIMAL_PROJECT_HOMEPAGE_ONE));
		assertTrue(homepages.contains(TEST_SIMAL_PROJECT_HOMEPAGE_TWO));
	}
	
	@Test
	public void testGetMailingLists() {
		Set<IDoapResource> lists = project1.getMailingLists();
		assertEquals("Got incorrect number of mailing lists", TEST_SIMAL_PROJECT_NUMBER_OF_MAILING_LIST, lists.size());
		assertTrue(lists.toString().contains(TEST_SIMAL_PROJECT_MAILING_LIST_ONE));
		assertTrue(lists.toString().contains(TEST_SIMAL_PROJECT_MAILING_LIST_TWO));
	}
	
	@Test
	public void testGetMaintainers() throws SimalRepositoryException {
		assertTrue(project1.getMaintainers().toString().contains(TEST_SIMAL_PROJECT_MAINTAINER_ONE));
		assertTrue(project1.getMaintainers().toString().contains(TEST_SIMAL_PROJECT_MAINTAINER_TWO));
	}
	
	@Test
	public void testGetOldHomepages() {
		String oldHomes = project1.getOldHomepages().toString();
		assertTrue(oldHomes.contains(TEST_SIMAL_PROJECT_OLD_HOMEPAGES));
	}
	
	@Test
	public void testGetOSes() {
		assertEquals(TEST_SIMAL_PROJECT_OS, project1.getOSes().toString());
	}
	
	@Test
	public void testGetProgrammingLangauges() {
		assertTrue(project1.getProgrammingLangauges().toString().contains(TEST_SIMAL_PROJECT_PROGRAMMING_LANGUAGE_ONE));
		assertTrue(project1.getProgrammingLangauges().toString().contains(TEST_SIMAL_PROJECT_PROGRAMMING_LANGUAGE_TWO));
	}
	
	@Test
	public void testGetReleases() throws SimalRepositoryException {
		assertEquals(TEST_SIMAL_PROJECT_RELEASES, project1.getReleases().toString());
	}
	
	@Test
	public void testGetRepositories() throws SimalRepositoryException {
		assertEquals(TEST_SIMAL_PROJECT_REPOSITORIES, project1.getRepositories().toString());
	}
	
	@Test
	public void testGetScreenshots() {
		String screenshots = project1.getScreenshots().toString();
		assertTrue(screenshots.contains(TEST_SIMAL_PROJECT_SCREENSHOTS));
	}
	
	@Test
	public void testGetTesters() throws SimalRepositoryException {
		assertEquals(TEST_SIMAL_PROJECT_TESTERS, project1.getTesters().toString());
	}
	
	@Test
	public void testGetTranslators() throws SimalRepositoryException {
		assertEquals(TEST_SIMAL_PROJECT_TRANSLATORS, project1.getTranslators().toString());
	}
	
	@Test
	public void testGetWikis() {
		String wikis = project1.getWikis().toString();
		assertTrue(wikis.contains(TEST_SIMAL_PROJECT_WIKIS));
	}
	
	@Test
	public void testProjectFromScratch() throws SimalRepositoryException {
		QName qname = new QName(SimalRepository.DEFAULT_NAMESPACE_URI + "TestingProjectFromScratch");
		Project project;
		try {
			project = repository.createProject(qname);
			project.addName("Testing");
			project.setShortDesc("Just testing adding a manually built project");
			
			project = repository.getProject(qname);
			assertNotNull("Project has not been added to repository", project);
			assertEquals("Project name is incorrectly set", "Testing", project.getName());
		} catch (DuplicateQNameException e) {
			fail(e.getMessage());
		}
		
		try {
			project = repository.createProject(qname);
			fail("We shouldn't have been able to create the second project");
		} catch (DuplicateQNameException e) {
			// that's expected
		}
	}
}

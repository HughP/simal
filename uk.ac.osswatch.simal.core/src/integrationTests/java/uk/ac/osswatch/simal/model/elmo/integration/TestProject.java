package uk.ac.osswatch.simal.model.elmo.integration;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import uk.ac.osswatch.simal.model.elmo.Resource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestProject extends AbstractTestDOAP {

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
		assertEquals(TEST_SIMAL_PROJECT_ISSUE_TRACKER, project1.getIssueTrackers().toString());
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
		assertEquals(TEST_SIMAL_PROJECT_DOWNLOAD_MIRRORS, project1.getDownloadMirrors().toString());
	}
	
	@Test
	public void testGetDownloadPages() {
		assertEquals(TEST_SIMAL_PROJECT_DOWNLOAD_PAGES, project1.getDownloadPages().toString());
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
		Set<Resource> lists = project1.getMailingLists();
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
		assertEquals(TEST_SIMAL_PROJECT_OLD_HOMEPAGES, project1.getOldHomepages().toString());
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
		assertEquals(TEST_SIMAL_PROJECT_SCREENSHOTS, project1.getScreenshots().toString());
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
		assertEquals(TEST_SIMAL_PROJECT_WIKIS, project1.getWikis().toString());
	}
}

package uk.ac.osswatch.simal.model.elmo.integration;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestProject extends AbstractTestDOAP {

	@Test
	public void testGetQName() {
		assertEquals(TEST_SIMAL_PROJECT_QNAME, project1.getQName().getNamespaceURI());
	}

	@Test
	public void testToJSON() throws SimalRepositoryException {
		resetTestData();
		String simalTestJSONRecord = "{\"id\":\"http://simal.oss-watch.ac.uk/simalTest#\",\"label\":\"Simal DOAP Test\"," 
				+ "\"name\":\"" + TEST_SIMAL_PROJECT_NAME
				+ "\",\"shortdesc\":\"" + TEST_SIMAL_PROJECT_SHORT_DESC + "\"}";
		String simalTestJSON = "{ \"items\": [" + simalTestJSONRecord + "]}";
		
		String json = project1.toJSON(true);
		assertTrue(
				"JSON file does not contain correct JSON representation of the Simal test record",
				json.equals(simalTestJSONRecord));
		
		json = project1.toJSON(false);
		assertTrue(
				"JSON file does not contain correct JSON representation of the Simal test project",
				json.equals(simalTestJSON));
	}
	
	@Test
	public void testGetIssueTracker() {
		assertEquals(TEST_SIMAL_PROJECT_ISSUE_TRACKER, project1.getIssueTracker().toString());
	}
	
	@Test
	public void testGetCategories() {
		assertEquals(TEST_SIMAL_PROJECT_CATEGORIES, project1.getCategories().toString());
	}
	
	@Test
	public void testGetDevelopers() {
		assertEquals(TEST_SIMAL_PROJECT_DEVELOPERS, project1.getDevelopers().toString());
	}
	
	@Test
	public void testGetDocumentors() {
		assertEquals(TEST_SIMAL_PROJECT_DOCUMENTORS, project1.getDocumentors().toString());
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
	public void testGetHelpers() {
		assertEquals(TEST_SIMAL_PROJECT_HELPERS, project1.getHelpers().toString());
	}
	
	@Test
	public void testGetHomepages() {
		assertEquals(TEST_SIMAL_PROJECT_HOMEPAGES, project1.getHomepages().toString());
	}
	
	@Test
	public void testGetMailingLists() {
		assertEquals(TEST_SIMAL_PROJECT_MAILING_LISTS, project1.getMailingLists().toString());
	}
	
	@Test
	public void testGetMaintainers() {
		assertEquals(TEST_SIMAL_PROJECT_MAINTAINERS, project1.getMaintainers().toString());
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
		assertEquals(TEST_SIMAL_PROJECT_PROGRAMMING_LANGUAGES, project1.getProgrammingLangauges().toString());
	}
	
	@Test
	public void testGetReleases() {
		assertEquals(TEST_SIMAL_PROJECT_RELEASES, project1.getReleases().toString());
	}
	
	@Test
	public void testGetRepositories() {
		assertEquals(TEST_SIMAL_PROJECT_REPOSITORIES, project1.getRepositories().toString());
	}
	
	@Test
	public void testGetScreenshots() {
		assertEquals(TEST_SIMAL_PROJECT_SCREENSHOTS, project1.getScreenshots().toString());
	}
	
	@Test
	public void testGetTesters() {
		assertEquals(TEST_SIMAL_PROJECT_TESTERS, project1.getTesters().toString());
	}
	
	@Test
	public void testGetTranslators() {
		assertEquals(TEST_SIMAL_PROJECT_TRANSLATORS, project1.getTranslators().toString());
	}
	
	@Test
	public void testGetWikis() {
		assertEquals(TEST_SIMAL_PROJECT_WIKIS, project1.getWikis().toString());
	}
}

package uk.ac.osswatch.simal.model.elmo.integration;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestProject extends AbstractTestDOAP {

	@Test
	public void testGetQName() {
		assertEquals(TEST_SIMAL_QNAME, project1.getQName().getNamespaceURI());
	}

	@Test
	public void testToJSON() throws SimalRepositoryException {
		resetTestData();
		String simalTestJSONRecord = "{\"id\":\"http://simal.oss-watch.ac.uk/simalTest#\",\"label\":\"Simal DOAP Test\"," 
				+ "\"name\":\"" + TEST_SIMAL_NAME
				+ "\",\"shortdesc\":\"" + TEST_SIMAL_SHORT_DESC + "\"}";
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
		fail("Not implemented yet");
	}
	
	@Test
	public void testGetCategories() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testGetDevelopers() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testGetDocumentors() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testGetDownloadMirrors() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testGetDownloadPages() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testGetHelpers() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testGetHomepages() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testGetMailingLists() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testGetMaintainers() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testGetOldHomepages() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testGetOSes() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testGetProgrammingLangauges() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testGetReleases() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testGetRepositories() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testGetScreenshots() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testGetTesters() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testGetTranslators() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testGetWikis() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testSetIssueTracker() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testSetCategories() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testSetDevelopers() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testSetDocumentors() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testSetDownloadMirrors() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testSetDownloadPages() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testSetHelpers() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testSetHomepages() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testSetMailingLists() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testSetMaintainers() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testSetOldHomepages() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testSetOSes() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testSetProgrammingLangauges() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testSetReleases() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testSetRepositories() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testSetScreenshots() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testSetTesters() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testSetTranslators() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testSetWikis() {
		fail("Not implemented yet");
	}

}

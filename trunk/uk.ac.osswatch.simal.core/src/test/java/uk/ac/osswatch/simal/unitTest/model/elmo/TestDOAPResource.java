package uk.ac.osswatch.simal.unitTest.model.elmo;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;

public class TestDOAPResource {

	@Test
	public void testToJSON() {
		MockDOAPResource resource = new MockDOAPResource();

		String json = resource.toJSON();
		assertTrue(
				"JSON file does not contain correct JSON representation of the Simal test project",
				json.startsWith("{ \"items\": ["));				
	}

	@Test
	public void testToJSONRecord() {
		MockDOAPResource resource = new MockDOAPResource();
		
		String json = resource.toJSONRecord();
		assertTrue(
				"JSON file does not contain correct JSON representation of the Simal test record",
				json.contains("\"name\":\"" +  BaseRepositoryTest.TEST_SIMAL_PROJECT_NAME));
		assertTrue(
				"JSON file does not contain correct JSON representation of the Simal test record",
				json.contains("\",\"shortdesc\":\"" + BaseRepositoryTest.TEST_SIMAL_PROJECT_SHORT_DESC));
		assertTrue(
				"JSON file does not contain correct JSON representation of the Simal test project",
				!json.startsWith("{ \"items\": ["));		
	}

}

package uk.ac.osswatch.simal.model.elmo;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.osswatch.simal.rdf.integrationTest.BaseRepositoryTest;

public class TestProject {

	@Test
	public void testToJSON() {
		MockProject project = new MockProject();

		String json = project.toJSON();
		assertTrue(
				"JSON file does not contain correct JSON representation of the Simal test project",
				json.startsWith("{ \"items\": ["));
	}

	@Test
	public void testToJSONRecord() {
		MockProject project = new MockProject();

		String json = project.toJSONRecord();
		assertTrue(
				"JSON file does not contain correct JSON representation of the Simal test record",
				json
						.contains(BaseRepositoryTest.TEST_SIMAL_PROJECT_CATEGORY_ONE));
		assertTrue(
				"JSON file does not contain correct JSON representation of the Simal test record",
				json
						.contains(BaseRepositoryTest.TEST_SIMAL_PROJECT_CATEGORY_TWO));
		
		assertTrue(
				"JSON file does not contain correct JSON representation of the Simal test record",
				!json.contains("[["));
		assertTrue(
				"JSON file does not contain correct JSON representation of the Simal test record",
				json
						.contains(BaseRepositoryTest.TEST_SIMAL_PROJECT_PROGRAMMING_LANGUAGE_ONE));
		assertTrue(
				"JSON file does not contain correct JSON representation of the Simal test record",
				json
						.contains(BaseRepositoryTest.TEST_SIMAL_PROJECT_PROGRAMMING_LANGUAGE_TWO));

	}
}

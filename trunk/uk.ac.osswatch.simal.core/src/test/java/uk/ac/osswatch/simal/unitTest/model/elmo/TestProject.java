package uk.ac.osswatch.simal.unitTest.model.elmo;

import static org.junit.Assert.assertTrue;

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
		assertTrue(json
				.contains(BaseRepositoryTest.TEST_SIMAL_PROJECT_CATEGORY_ONE));
		assertTrue(json
				.contains(BaseRepositoryTest.TEST_SIMAL_PROJECT_CATEGORY_TWO));

		assertTrue(!json.contains("[["));
		assertTrue(json
				.contains(BaseRepositoryTest.TEST_SIMAL_PROJECT_PROGRAMMING_LANGUAGE_ONE));
		assertTrue(json
				.contains(BaseRepositoryTest.TEST_SIMAL_PROJECT_PROGRAMMING_LANGUAGE_TWO));

	}
}

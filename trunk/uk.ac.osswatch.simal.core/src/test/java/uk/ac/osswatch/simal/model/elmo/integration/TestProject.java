package uk.ac.osswatch.simal.model.elmo.integration;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.osswatch.simal.model.elmo.Project;
import uk.ac.osswatch.simal.model.elmo.ProjectException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.integrationTest.BaseRepositoryTest;

public class TestProject extends BaseRepositoryTest {

	private static Project project1;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		resetTestData();
	}

	private static void resetTestData() throws SimalRepositoryException {
		project1 = getSimalTestProject(true);
	}

	@Test
	public void testGetName() throws SimalRepositoryException {
		resetTestData();
		assertEquals(TEST_SIMAL_NAME, project1.getName());
	}

	@Test
	public void testGetShortDesc() {
		assertEquals(TEST_SIMAL_SHORT_DESC, project1.getShortDesc());
	}

	@Test
	public void testSetName() throws ProjectException {
		String newName = "modified name";
		project1.setName(newName);
		assertEquals(newName, project1.getName());
	}

	@Test
	public void testSetShortDesc() {
		String newDesc = "New short description";
		project1.setShortDesc(newDesc);
		assertEquals(newDesc, project1.getShortDesc());
	}
	
	@Test
	public void testGetQName() {
		assertEquals(TEST_SIMAL_QNAME, project1.getQName().toString());
	}

}

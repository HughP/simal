package uk.ac.osswatch.simal.model.elmo.integration;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.osswatch.simal.model.elmo.ProjectException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestDoapResource extends AbstractTestDOAP {

	@Test
	public void testGetName() throws SimalRepositoryException {
		resetTestData();
		assertEquals(TEST_SIMAL_PROJECT_NAME, project1.getName());
	}

	@Test
	public void testGetShortDesc() {
		assertEquals(TEST_SIMAL_PROJECT_SHORT_DESC, project1.getShortDesc());
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
	public void testGetCreated() {
		assertEquals(TEST_SIMAL_PROJECT_CREATED, project1.getCreated());
	}
	
	@Test
	public void testSetCreated() throws ProjectException {
		String newCreated = "2020-20-01";
		project1.setCreated(newCreated);
		assertEquals(newCreated, project1.getCreated());
	}
	
	@Test
	public void testGetDescription() {
		assertEquals(TEST_SIMAL_PROJECT_DESCRIPTION, project1.getDescription());
	}
	
	@Test
	public void testSetDescription() {
		String newDesc = "modified description";
		project1.setDescription(newDesc);
		assertEquals(newDesc, project1.getDescription());
	}
	
	@Test
	public void testGetLicences() {
		assertTrue(project1.getLicences().toString().contains(TEST_SIMAL_PROJECT_LICENCE_ONE));
		assertTrue(project1.getLicences().toString().contains(TEST_SIMAL_PROJECT_LICENCE_TWO));
	}

}

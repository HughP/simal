package uk.ac.osswatch.simal.model.elmo.integration;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.osswatch.simal.model.elmo.ProjectException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestDoapResource extends AbstractTestDOAP {

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
	public void testGetCreated() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testSetCreated() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testGetDescription() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testSetDescription() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testGetLicences() {
		fail("Not implemented yet");
	}
	
	@Test
	public void testSetLicencesd() {
		fail("Not implemented yet");
	}

}

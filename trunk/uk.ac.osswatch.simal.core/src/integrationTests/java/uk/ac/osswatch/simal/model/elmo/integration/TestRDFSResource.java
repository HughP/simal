package uk.ac.osswatch.simal.model.elmo.integration;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.model.elmo.Resource;
import uk.ac.osswatch.simal.rdf.integrationTest.BaseRepositoryTest;

public class TestRDFSResource extends BaseRepositoryTest {

	protected static IResource maillistResource;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Set<Resource> lists = getSimalTestProject(true).getMailingLists();
		maillistResource = (Resource) lists.toArray()[0];
	}
	
	@Test
	public void testLabel() {
		assertTrue("Label is incorrect", maillistResource.getLabel().contains("Mailing List"));
	}
	
	@Test
	public void testComment() {
		assertTrue("Comment is incorrect", maillistResource.getComment().contains("This is the "));
	}
}

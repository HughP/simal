package uk.ac.osswatch.simal.integrationTest.rdf;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;

import org.junit.Test;

import uk.ac.osswatch.simal.model.elmo.Project;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * test common activities relating to Projects.
 * 
 */
public class TestRepository extends BaseRepositoryTest {

	@Test
	public void testCreaterepository() throws SimalRepositoryException {
		SimalRepository.destroy();
		SimalRepository.setIsTest(false);
		SimalRepository.initialise();
		Set<Project> projects = SimalRepository.getAllProjects();
		assertTrue("we seem unable to create a repository without test data",
				projects.size() == 0);

		try {
			SimalRepository.setIsTest(true);
			fail("We should not be able to change the SimalRepository.isTest value afte initialisation of the repository");
		} catch (SimalRepositoryException e){
			// that's fine we want the exception in this case
		}
		SimalRepository.destroy();

		SimalRepository.setIsTest(true);
		SimalRepository.initialise();
		projects = SimalRepository.getAllProjects();
		assertTrue("we seem unable to create a repository without test data",
				projects.size() > 0);
	}

	@Test
	public void testAddProject() throws SimalRepositoryException {
		initialiseRepository(false);
		// The default test repository adds projects when it is instantiated
		assertTrue(SimalRepository.isInitialised());
	}

	@Test
	public void testFindProject() throws SimalRepositoryException {
		initialiseRepository(false);

		QName qname = new QName("http://foo.org/nonExistent");
		Project project = SimalRepository.getProject(qname);
		assertNull(project);

		// test a known valid file
		project = getSimalTestProject(true);
		assertEquals("Simal DOAP Test", project.getName());
	}

	@Test
	public void testGetRdfXml() throws SimalRepositoryException {
		initialiseRepository(false);

		QName qname = new QName(TEST_SIMAL_PROJECT_QNAME);

		StringWriter sw = new StringWriter();
		SimalRepository.writeXML(sw, qname);
		String xml = sw.toString();
		assertTrue("XML does not contain the QName expected", xml
				.contains("rdf:about=\"" + TEST_SIMAL_PROJECT_QNAME + "\""));
		int indexOf = xml.indexOf("<doap:Project");
		int lastIndexOf = xml.lastIndexOf("<doap:Project");
		assertTrue("XML appears to contain more than one project record",
				indexOf == lastIndexOf);
	}

	@Test
	public void testGetAllProjects() throws SimalRepositoryException,
			IOException {
		initialiseRepository(false);

		Set<Project> projects = SimalRepository.getAllProjects();
		assertEquals(4, projects.size());

		Iterator<Project> itrProjects = projects.iterator();
		Project project;
		while (itrProjects.hasNext()) {
			project = itrProjects.next();
			assertNotNull(project.getName());
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testNullQNamehandling() throws SimalRepositoryException {
		initialiseRepository(false);

		Set<Project> projects = SimalRepository.getAllProjects();

		Iterator itrProjects = projects.iterator();
		Project project;
		while (itrProjects.hasNext()) {
			project = (Project) itrProjects.next();
			assertNotNull("All projects must have a QName", project.getQName());
		}
	}

	@Test
	public void testGetAllProjectsAsJSON() throws SimalRepositoryException {
		initialiseRepository(false);

		String json = SimalRepository.getAllProjectsAsJSON();
		assertTrue("JSON file does not appear to be correct", json
				.startsWith("{ \"items\": ["));
		assertTrue("JSON file does not appear to be correct", json
				.endsWith("]}"));
	}
	
	@Test
	public void testGetCategoryLabel() throws SimalRepositoryException {
		initialiseRepository(false);
		
		String uri = "http://simal.oss-watch.ac.uk/category/socialNews";
		String label = SimalRepository.getLabel(uri);
		assertEquals("Category Label is incorrect", "Social News", label);
		
		uri = "http://example.org/does/not/exist";
		label = SimalRepository.getLabel(uri);
		assertNull("Somehow we have a label for a resource that does not exist", label);
	}
}

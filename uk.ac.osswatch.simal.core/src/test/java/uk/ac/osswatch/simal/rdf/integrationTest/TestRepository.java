package uk.ac.osswatch.simal.rdf.integrationTest;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;

import static junit.framework.Assert.*;

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
	public void testAddProject() throws SimalRepositoryException {
		SimalRepository repo = getTestRepo();
		// The default test repository adds projects when it is instantiated
		assertNotNull(repo);
	}

	@Test
	public void testFindProject() throws SimalRepositoryException {
		SimalRepository repo = getTestRepo(true);

		QName qname = new QName("http://foo.org/nonExistent");
		try {
			Project project = repo.getProject(qname);
			assertNull(project);

			// test a known valid file
			project = getSimalTestProject(true);
			assertEquals("Simal DOAP Test", project.getName());
		} finally {
			repo.close();
		}
	}

	@Test
	public void testGetRdfXml() throws SimalRepositoryException {
		SimalRepository repo = getTestRepo();
		QName qname = new QName(TEST_SIMAL_QNAME);

		StringWriter sw = new StringWriter();
		repo.writeXML(sw, qname);
		String xml = sw.toString();
		assertTrue("XML does not contain the QName expected",
					xml.contains("rdf:about=\"http://simal.oss-watch.ac.uk/simalTest#\""));
	}

	@Test
	public void testGetAllProjects() throws SimalRepositoryException {
		SimalRepository repo = getTestRepo();

		Set<Project> projects = repo.getAllProjects();
		assertEquals(4, projects.size());
	}
}

package uk.ac.osswatch.simal.rdf.integrationTest;

import java.io.StringWriter;
import java.util.Set;

import javax.xml.namespace.QName;

import junit.framework.Assert;

import org.junit.Test;
import org.openrdf.concepts.doap.Project;

import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * test common activities relating to Projects.
 * 
 */
public class TestProject {

	@Test
	public void testAddProject() throws SimalRepositoryException {
		SimalRepository repo = getTestRepo();
		Assert.assertNotNull(repo);
	}

	@Test
	public void testFindProject() throws SimalRepositoryException {
		SimalRepository repo = getTestRepo();
		
		QName qname = new QName("http://foo.org/nonExistent");
		try {
		Project project = repo.getProject(qname);
		Assert.assertNull(project);

		qname = new QName("http://simal.oss-watch.ac.uk/simalTest#");
		project = repo.getProject(qname);
		Assert.assertTrue(project.getDoapNames().size() == 1);
		Assert.assertEquals("Simal DOAP Test",
				project.getDoapNames().toArray()[0]);
		} finally {
			repo.close();
		}
		
	}

	@Test
	public void testGetRdfXml() throws SimalRepositoryException {
		SimalRepository repo = getTestRepo();
		QName qname = new QName("http://simal.oss-watch.ac.uk/simalTest#");
		
		StringWriter sw = new StringWriter();
		repo.writeXML(sw, qname);
		String xml = sw.toString();
		Assert.assertTrue("XML does not contain the QName expected", xml.contains("rdf:about=\"http://simal.oss-watch.ac.uk/simalTest#\""));
	}
	
	@Test
	public void testGetAllProjects() throws SimalRepositoryException {
		SimalRepository repo = getTestRepo();
		
		Set<Project> projects = repo.getAllProjects();
		Assert.assertEquals(3, projects.size());
	}

	/*
	 * Create a test repo and populate it with test data.
	 */
	private SimalRepository getTestRepo() throws SimalRepositoryException {
		SimalRepository repo = new SimalRepository();
		return repo;
	}
}

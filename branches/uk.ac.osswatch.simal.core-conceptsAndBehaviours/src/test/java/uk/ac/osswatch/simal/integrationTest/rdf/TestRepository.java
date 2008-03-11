package uk.ac.osswatch.simal.integrationTest.rdf;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.openrdf.concepts.doap.Project;
import uk.ac.osswatch.simal.model.ICategory;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * test common activities relating to Projects.
 * 
 */
public class TestRepository extends BaseRepositoryTest {

  @Test
  public void testAddProject() throws SimalRepositoryException {
    initialiseRepository(false);
    // The default test repository adds projects when it is instantiated
    assertTrue(repository.isInitialised());
  }

  @Test
  public void testFindProject() throws SimalRepositoryException {
    initialiseRepository(false);

    QName qname = new QName("http://foo.org/nonExistent");
    IProject project = repository.getProject(qname);
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
    repository.writeXML(sw, qname);
    String xml = sw.toString();
    assertTrue("XML does not contain the QName expected", xml
        .contains("rdf:about=\"" + TEST_SIMAL_PROJECT_QNAME + "\""));
    int indexOf = xml.indexOf("<doap:Project");
    int lastIndexOf = xml.lastIndexOf("<doap:Project");
    assertTrue("XML appears to contain more than one project record",
        indexOf == lastIndexOf);
  }

  @Test
  public void testGetAllProjects() throws SimalRepositoryException, IOException {
    initialiseRepository(false);

    Set<IProject> projects = repository.getAllProjects();
    assertEquals(4, projects.size());

    Iterator<IProject> itrProjects = projects.iterator();
    IProject project;
    while (itrProjects.hasNext()) {
      project = itrProjects.next();
      assertNotNull(project.getName());
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testNullQNamehandling() throws SimalRepositoryException {
    initialiseRepository(false);

    Set<IProject> projects = repository.getAllProjects();

    Iterator<IProject> itrProjects = projects.iterator();
    IProject project;
    while (itrProjects.hasNext()) {
      project = itrProjects.next();
      assertNotNull("All projects must have a QName", project.getQName());
    }
  }

  @Test
  public void testGetAllProjectsAsJSON() throws SimalRepositoryException {
    initialiseRepository(false);

    String json = repository.getAllProjectsAsJSON();
    assertTrue("JSON file does not appear to be correct", json
        .startsWith("{ \"items\": ["));
    assertTrue("JSON file does not appear to be correct", json.endsWith("]}"));
  }

  @Test
  public void testGetCategoryLabel() throws SimalRepositoryException {
    initialiseRepository(false);

    String uri = "http://simal.oss-watch.ac.uk/category/socialNews";
    ICategory category = repository.findCategory(new QName(uri));
    String label = category.getLabel();
    assertEquals("Category Label is incorrect", "Social News", label);

    uri = "http://example.org/does/not/exist";
    QName qname = new QName(uri);
    category = repository.findCategory(qname);
    label = category.getLabel();
    assertNotSame("Somehow we have a valid label for a resource that does not exist",
        qname, category.getQName());
  }

  @Test
  public void testRemove() throws SimalRepositoryException {
    repository.remove(new QName(TEST_SIMAL_PROJECT_QNAME));
    Project project = getSimalTestProject(true);
    assertNull("Failed to remove the test project", project);
  }
}

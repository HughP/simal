package uk.ac.osswatch.simal.integrationTest.rdf;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;

import org.junit.Test;

import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.elmo.Project;
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
      assertNotNull(project.getNames());
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testNullQNamehandling() throws SimalRepositoryException {
    initialiseRepository(false);

    Set<IProject> projects = repository.getAllProjects();

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

    String json = repository.getAllProjectsAsJSON();
    assertTrue("JSON file does not appear to be correct", json
        .startsWith("{ \"items\": ["));
    assertTrue("JSON file does not appear to be correct", json.endsWith("]}"));
  }

  @Test
  public void testGetCategoryLabel() throws SimalRepositoryException {
    initialiseRepository(false);

    String uri = "http://simal.oss-watch.ac.uk/category/socialNews";
    String label = repository.getLabel(uri);
    assertEquals("Category Label is incorrect", "Social News", label);

    uri = "http://example.org/does/not/exist";
    label = repository.getLabel(uri);
    assertNull("Somehow we have a label for a resource that does not exist",
        label);
  }

  @Test
  public void testRemove() throws SimalRepositoryException {
    repository.remove(new QName(TEST_SIMAL_PROJECT_QNAME));
    IProject project = getSimalTestProject(true);
    assertNull("Failed to remove the test project", project);
  }
}

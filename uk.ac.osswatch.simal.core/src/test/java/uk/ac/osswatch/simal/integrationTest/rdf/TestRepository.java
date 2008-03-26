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
import org.openrdf.concepts.doap.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * test common activities relating to Projects.
 * 
 */
public class TestRepository extends BaseRepositoryTest {
  private static final Logger logger = LoggerFactory
      .getLogger(TestRepository.class);

  @Test
  public void testAddProject() throws SimalRepositoryException {
    logger.debug("Starting testAddProject()");
    initialiseRepository(false);
    // The default test repository adds projects when it is instantiated
    assertTrue(repository.isInitialised());
    logger.debug("Finished testAddProject()");
  }

  @Test
  public void testFindProject() throws SimalRepositoryException {
    logger.debug("Starting testFindProject()");
    initialiseRepository(false);

    QName qname = new QName("http://foo.org/nonExistent");
    IProject project = repository.getProject(qname);
    assertNull(project);

    // test a known valid file
    project = getSimalTestProject(true);
    assertEquals("Simal DOAP Test", project.getName());
    logger.debug("Finished testFindProject()");
  }

  @Test
  public void testGetRdfXml() throws SimalRepositoryException {
    logger.debug("Starting testGetRdfXML()");
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
    logger.debug("Finished testGetRdfXML()");
  }

  @Test
  public void testGetAllProjects() throws SimalRepositoryException, IOException {
    logger.debug("Starting testGetAllProjects()");
    initialiseRepository(false);

    Set<IProject> projects = repository.getAllProjects();
    assertEquals(4, projects.size());

    Iterator<IProject> itrProjects = projects.iterator();
    IProject project;
    while (itrProjects.hasNext()) {
      project = itrProjects.next();
      assertNotNull(project.getName());
    }
    logger.debug("Finished testGetAllProjects()");
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testNullQNameHandling() throws SimalRepositoryException {
    logger.debug("Starting testNullQNameHandling()");
    initialiseRepository(false);

    Set<IProject> projects = repository.getAllProjects();

    Iterator<IProject> itrProjects = projects.iterator();
    IProject project;
    while (itrProjects.hasNext()) {
      project = itrProjects.next();
      assertNotNull("All projects must have a QName", project.getQName());
    }
    logger.debug("Finished testNullQNameHandling()");
  }

  @Test
  public void testGetAllProjectsAsJSON() throws SimalRepositoryException {
    logger.debug("Starting testGetAllProjectsAsJSON()");
    initialiseRepository(false);

    String json = repository.getAllProjectsAsJSON();
    assertTrue("JSON file does not appear to be correct", json
        .startsWith("{ \"items\": ["));
    assertTrue("JSON file does not appear to be correct", json.endsWith("]}"));
    logger.debug("Finished testGetAllProjectsAsJSON()");
  }

  @Test
  public void testRemove() throws SimalRepositoryException {
    logger.debug("Starting testRemove()");
    repository.remove(new QName(TEST_SIMAL_PROJECT_QNAME));
    Project project = getSimalTestProject(true);
    assertNull("Failed to remove the test project", project);
    logger.debug("Finished testRemove()");
  }

  @Test
  public void testFindPersonById() throws SimalRepositoryException {
    logger.debug("Starting testFindPersonByID()");
    initialiseRepository(false);

    IPerson person = repository.findPersonById("15");
    assertNotNull(person);
    assertEquals("developer", person.getFoafGivennames().toString());
    logger.debug("Finished testFindPersonByID()");
  }
}

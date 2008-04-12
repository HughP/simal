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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.DuplicateQNameException;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.TransactionException;

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
    // The default test repository adds projects when it is instantiated
    assertTrue(repository.isInitialised());
    logger.debug("Finished testAddProject()");
  }

  @Test
  public void testFindProject() throws SimalRepositoryException {
    logger.debug("Starting testFindProject()");
    QName qname = new QName("http://foo.org/nonExistent");
    IProject project = repository.getProject(qname);
    assertNull(project);

    // test a known valid file
    project = getSimalTestProject();
    assertEquals("Simal DOAP Test", project.getName());
    logger.debug("Finished testFindProject()");
  }

  @Test
  public void testGetRdfXml() throws SimalRepositoryException {
    logger.debug("Starting testGetRdfXML()");
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
    Set<IProject> projects = repository.getAllProjects();
    assertEquals(5, projects.size());

    Iterator<IProject> itrProjects = projects.iterator();
    IProject project;
    while (itrProjects.hasNext()) {
      project = itrProjects.next();
      assertNotNull(project.getName());
    }
    logger.debug("Finished testGetAllProjects()");
  }

  @Test
  public void testGetAllCategories() throws SimalRepositoryException, IOException {
    logger.debug("Starting testGetAllCategories()");
    
    Set<IDoapCategory> categories = repository.getAllCategories();
    assertEquals(53, categories.size());
    logger.debug("Finished testGetAllCategories()");
  }

  @Test
  public void testGetAllPeople() throws SimalRepositoryException, IOException {
    logger.debug("Starting testGetAllPeople()");
    Set<IPerson> people = repository.getAllPeople();
    assertEquals(17, people.size());

    Iterator<IPerson> itrPeople = people.iterator();
    IPerson person;
    while (itrPeople.hasNext()) {
      person = itrPeople.next();
      assertNotNull(person.getLabel());
    }
    logger.debug("Finished testGetAllPeople()");
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testNullQNameHandling() throws SimalRepositoryException {
    logger.debug("Starting testNullQNameHandling()");
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
    String json = repository.getAllProjectsAsJSON();
    assertTrue("JSON file does not appear to be correct", json
        .startsWith("{ \"items\": ["));
    assertTrue("JSON file does not appear to be correct", json.endsWith("]}"));
    logger.debug("Finished testGetAllProjectsAsJSON()");
  }

  @Test
  public void testFindPersonById() throws SimalRepositoryException {
    logger.debug("Starting testFindPersonByID()");
    IPerson person = repository.findPersonById("15");
    assertNotNull(person);
    assertEquals("developer", person.getFoafGivennames().toString());
    logger.debug("Finished testFindPersonByID()");
  }

  @Test
  public void testFindPersonBySha1Sum() throws SimalRepositoryException {
    logger.debug("Starting testFindPersonBySha1Sum()");
    IPerson person = repository.findPersonBySha1Sum("1dd14fc12fa3ac6ef9e3b29498d16b56f8e716a3");
    assertNotNull(person);
    logger.debug("Finished testFindPersonBySha1Sum()");
  }

  @Test
  public void testFindPersonBySeeAlso() throws SimalRepositoryException {
    logger.debug("Starting testFindPersonBySeeAlso()");
    IPerson person = repository.findPersonBySeeAlso("http://foo.org/~documentor/foaf.rdf.xml");
    assertNotNull(person);
    logger.debug("Finished testFindPersonBySeeAlso()");
  }

  @Test
  public void testFindProjectById() throws SimalRepositoryException {
    logger.debug("Starting testFindProjectByID()");
    IProject project = repository.findProjectById(project1ID);
    assertNotNull(project);
    logger.debug("Finished testFindProjectByID()");
  }

  @Test
  public void testRemove() throws SimalRepositoryException, TransactionException, DuplicateQNameException {
    logger.debug("Starting testRemove()");
    QName qname1 = new QName(SimalRepository.DEFAULT_PROJECT_NAMESPACE_URI
        + "TestingId1");

    IProject project;
    project = repository.createProject(qname1);
    project = repository.getProject(qname1);
    assertNotNull(project);
    
    repository.remove(qname1);
    project = repository.getProject(qname1);
    assertNull("Failed to remove the test project", project);
    logger.debug("Finished testRemove()");
  }

  /**
   * Test for issue 107 - Since the foaf:Person entries do not have an identifier Simal is creating one
   * automatically. However, there is no attempt to ensure that the person does not
   * already exist. Hence a duplicate is being entered.
   * @throws SimalRepositoryException 
   */
  @Test
  public void testDuplicateBlankNodePersons() throws SimalRepositoryException {
    repository.addProject(SimalRepository.class.getResource(SimalRepository.TEST_FILE_URI_WITH_QNAME),
        SimalRepository.TEST_FILE_BASE_URL);
    
    // there is a tester with an email address as a unique ID
    IProject project = getSimalTestProject();
    Set<IPerson> testers = project.getTesters();
    assertEquals(1, testers.size());
    
    // there is a translator with an seeAlso as a unique ID
    Set<IPerson> translators = project.getTranslators();
    assertEquals(1, translators.size());
  }
}

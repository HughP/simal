package uk.ac.osswatch.simal.integrationTest.model.elmo;

import static org.junit.Assert.*;

import java.util.Set;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.openrdf.concepts.doap.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.elmo.DoapProjectBehaviour;
import uk.ac.osswatch.simal.model.elmo.DoapResourceBehaviour;
import uk.ac.osswatch.simal.rdf.DuplicateQNameException;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestProject extends BaseRepositoryTest {
  private static final Logger logger = LoggerFactory
      .getLogger(TestProject.class);

  @Test
  public void testGetQName() {
    assertEquals(TEST_SIMAL_PROJECT_QNAME, project1.getQName()
        .getNamespaceURI());
  }

  @Test
  public void testToJSON() throws SimalRepositoryException {
    resetTestData();

    // FIXME: we shouldn't need knowledge of the model here
    //DoapProjectBehaviour behaviour = new DoapProjectBehaviour(project1);
    String json = "test"; //= behaviour.toJSONRecord();
    assertTrue(
        "JSON file does not contain correct JSON representation of the Simal test record",
        json.contains("\"name\":\"" + TEST_SIMAL_PROJECT_NAME));
    assertTrue(
        "JSON file does not contain correct JSON representation of the Simal test record",
        json.contains("\",\"shortdesc\":\"" + TEST_SIMAL_PROJECT_SHORT_DESC));
    assertTrue(
        "JSON file does not contain correct JSON representation of the Simal test project",
        !json.startsWith("{ \"items\": ["));

    json = "test";//behaviour.toJSON();
    assertTrue(
        "JSON file does not contain correct JSON representation of the Simal test project",
        json.startsWith("{ \"items\": ["));
  }

  @Test
  public void testGetIssueTracker() {
    String issueTrackers = project1.getDoapBugDatabases().toString();
    assertTrue(issueTrackers.contains(TEST_SIMAL_PROJECT_ISSUE_TRACKER));
  }

  @Test
  public void testGetCategories() {
    String categories = project1.getDoapCategories().toString();
    logger.debug("Categories are " + categories);
    assertTrue("Categries are incorrect: " + categories, categories.contains(TEST_SIMAL_PROJECT_CATEGORY_ONE));
    assertTrue("Categries are incorrect: " + categories, categories.contains(TEST_SIMAL_PROJECT_CATEGORY_TWO));
  }

  @Test
  public void testGetDevelopers() throws SimalRepositoryException {
    assertEquals(TEST_SIMAL_PROJECT_DEVELOPERS, project1.getDoapDevelopers()
        .toString());
  }

  @Test
  public void testGetDocumentors() throws SimalRepositoryException {
    assertEquals(TEST_SIMAL_PROJECT_DOCUMENTORS, project1.getDoapDocumenters()
        .toString());
  }

  @Test
  public void testGetDownloadMirrors() {
    String mirrors = project1.getDoapDownloadMirrors().toString();
    assertTrue(mirrors.contains(TEST_SIMAL_PROJECT_DOWNLOAD_MIRRORS));
  }

  @Test
  public void testGetDownloadPages() {
    String downloads = project1.getDoapDownloadPages().toString();
    assertTrue(downloads.contains(TEST_SIMAL_PROJECT_DOWNLOAD_PAGES));
  }

  @Test
  public void testGetHelpers() throws SimalRepositoryException {
    assertEquals(TEST_SIMAL_PROJECT_HELPERS, project1.getDoapHelpers()
        .toString());
  }

  @Test
  public void testGetHomepages() {
    String homepages = project1.getDoapHomepages().toString();
    assertTrue(homepages.contains(TEST_SIMAL_PROJECT_HOMEPAGE_ONE));
    assertTrue(homepages.contains(TEST_SIMAL_PROJECT_HOMEPAGE_TWO));
  }

  @Test
  public void testGetMailingLists() {
    Set<Object> lists = project1.getDoapMailingLists();
    assertEquals("Got incorrect number of mailing lists",
        TEST_SIMAL_PROJECT_NUMBER_OF_MAILING_LIST, lists.size());
    assertTrue(lists.toString().contains(TEST_SIMAL_PROJECT_MAILING_LIST_ONE));
    assertTrue(lists.toString().contains(TEST_SIMAL_PROJECT_MAILING_LIST_TWO));
  }

  @Test
  public void testGetMaintainers() throws SimalRepositoryException {
    assertTrue(project1.getDoapMaintainers().toString().contains(
        TEST_SIMAL_PROJECT_MAINTAINER_ONE));
    assertTrue(project1.getDoapMaintainers().toString().contains(
        TEST_SIMAL_PROJECT_MAINTAINER_TWO));
  }

  @Test
  public void testGetOldHomepages() {
    String oldHomes = project1.getDoapOldHomepages().toString();
    assertTrue(oldHomes.contains(TEST_SIMAL_PROJECT_OLD_HOMEPAGES));
  }

  @Test
  public void testGetOSes() {
    assertEquals(TEST_SIMAL_PROJECT_OS, project1.getDoapOses().toString());
  }

  @Test
  public void testGetProgrammingLangauges() {
    assertTrue(project1.getDoapProgrammingLanguages().toString().contains(
        TEST_SIMAL_PROJECT_PROGRAMMING_LANGUAGE_ONE));
    assertTrue(project1.getDoapProgrammingLanguages().toString().contains(
        TEST_SIMAL_PROJECT_PROGRAMMING_LANGUAGE_TWO));
  }

  @Test
  public void testGetReleases() throws SimalRepositoryException {
    assertEquals(TEST_SIMAL_PROJECT_RELEASES, project1.getDoapReleases()
        .toString());
  }

  @Test
  public void testGetRepositories() throws SimalRepositoryException {
    assertEquals(TEST_SIMAL_PROJECT_REPOSITORIES, project1
        .getDoapRepositories().toString());
  }

  @Test
  public void testGetScreenshots() {
    String screenshots = project1.getDoapScreenshots().toString();
    assertTrue(screenshots.contains(TEST_SIMAL_PROJECT_SCREENSHOTS));
  }

  @Test
  public void testGetTesters() throws SimalRepositoryException {
    assertEquals(TEST_SIMAL_PROJECT_TESTERS, project1.getDoapTesters()
        .toString());
  }

  @Test
  public void testGetTranslators() throws SimalRepositoryException {
    assertEquals(TEST_SIMAL_PROJECT_TRANSLATORS, project1.getDoapTranslators()
        .toString());
  }

  @Test
  public void testGetWikis() {
    String wikis = project1.getDoapWikis().toString();
    assertTrue(wikis.contains(TEST_SIMAL_PROJECT_WIKIS));
  }

  @Test
  public void testProjectFromScratch() throws SimalRepositoryException {
    QName qname = new QName(SimalRepository.DEFAULT_PROJECT_NAMESPACE_URI
        + "TestingProjectFromScratch");
    Project project;
    try {
      project = repository.createProject(qname);
      // FIXME: project.addName("Testing");
      project.setDoapShortdesc("Just testing adding a manually built project");

      project = repository.getProject(qname);
      assertNotNull("Project has not been added to repository", project);

      // FIXME: we shouldn't ned knowledge of the model here.
      DoapResourceBehaviour resource = new DoapResourceBehaviour(project1);
      assertEquals("Project name is incorrectly set", "Testing", resource
          .getName());
    } catch (DuplicateQNameException e) {
      fail(e.getMessage());
    }
  }

  /**
   * Test to ensure that project ids are being correctly generated.
   * 
   * @throws SimalRepositoryException
   * @throws DuplicateQNameException
   */
  @Test
  public void testId() throws SimalRepositoryException, DuplicateQNameException {
    QName qname1 = new QName(SimalRepository.DEFAULT_PROJECT_NAMESPACE_URI
        + "TestingId1");
    QName qname2 = new QName(SimalRepository.DEFAULT_PROJECT_NAMESPACE_URI
        + "TestingId2");

    Project project;
    project = repository.createProject(qname1);
    project = repository.getProject(qname1);
    //DoapProjectBehaviour behaviour = new DoapProjectBehaviour(project);
    String id1 = "test"; //behaviour.getID();

    project = repository.createProject(qname2);
    project = repository.getProject(qname2);
    //behaviour = new DoapProjectBehaviour(project);
    String id2 = "test"; // behaviour.getID();

    assertFalse("Project IDs are not unique: " + id1 + " == " + id2, id1.equals(id2));

    // check IDs are being written to the repository
    project = repository.getProject(qname1);
    // String id3 = project.getID();
    // FIXME (ISSUE 88): projectIDs are not currently written to the repository
    // assertTrue("Project IDs don't appear to be written to the repo",
    // id1.equals(id3));
  }
}

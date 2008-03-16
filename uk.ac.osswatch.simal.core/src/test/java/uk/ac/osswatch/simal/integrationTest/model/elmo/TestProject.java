package uk.ac.osswatch.simal.integrationTest.model.elmo;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.concepts.foaf.Person;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IDoapHomepageBehaviour;
import uk.ac.osswatch.simal.model.IDoapMailingList;
import uk.ac.osswatch.simal.model.IDoapRelease;
import uk.ac.osswatch.simal.model.IDoapRepository;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.DuplicateQNameException;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestProject extends BaseRepositoryTest {

  private static Set<Person> maintainers;
  private static Set<Person> developers;
  private static Set<Person> documenters;
  private static Set<Person> helpers;
  private static Set<Person> translators;
  private static Set<Person> testers;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    createRepository();

    project1 = getSimalTestProject(false);
    maintainers = project1.getDoapMaintainers();
    developers = project1.getDoapDevelopers();
    helpers = project1.getDoapHelpers();
    documenters = project1.getDoapHelpers();
    translators = project1.getDoapTranslators();
    testers = project1.getDoapTesters();
  }

  @Test
  public void testPersonsLoaded() {
    assertEquals("Should have one maintainer", 2, maintainers.size());
    assertEquals("Should have one developers", 1, developers.size());
    assertEquals("Should have one helpers", 1, helpers.size());
    assertEquals("Should have one documenters", 1, documenters.size());
    assertEquals("Should have one translators", 1, translators.size());
    assertEquals("Should have one testers", 1, testers.size());
  }

  @Test
  public void testGetQName() {
    assertEquals(TEST_SIMAL_PROJECT_QNAME, project1.getQName()
        .getNamespaceURI());
  }

  @Test
  public void testToJSON() throws SimalRepositoryException {
    resetTestData();

    String json = project1.toJSONRecord();
    assertTrue(
        "JSON file does not contain correct JSON representation of the Simal test record",
        json.contains("\"name\":\"" + TEST_SIMAL_PROJECT_NAME));
    assertTrue(
        "JSON file does not contain correct JSON representation of the Simal test record",
        json.contains("\",\"shortdesc\":\"" + TEST_SIMAL_PROJECT_SHORT_DESC));
    assertTrue(
        "JSON file does not contain correct JSON representation of the Simal test project",
        !json.startsWith("{ \"items\": ["));

    json = project1.toJSON();
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
    boolean hasCategoryOne = false;
    boolean hasCategoryTwo = false;
    Iterator<IDoapCategory> categories = project1.getCategories().iterator();
    String label;
    while (categories.hasNext()) {
      label = categories.next().getLabel();
      if (label.equals(TEST_SIMAL_PROJECT_CATEGORY_ONE)) {
        hasCategoryOne = true;
      } else if (label.equals(TEST_SIMAL_PROJECT_CATEGORY_TWO)) {
        hasCategoryTwo = true;
      }
    }
    assertTrue("Categories do not include " + TEST_SIMAL_PROJECT_CATEGORY_ONE,
        hasCategoryOne);

    assertTrue("Categories do not include " + TEST_SIMAL_PROJECT_CATEGORY_TWO,
        hasCategoryTwo);
  }

  @Test
  public void testGetDevelopers() throws SimalRepositoryException {
    boolean hasDeveloper = false;
    Iterator<IPerson> people = project1.getDevelopers().iterator();
    IPerson person;
    while (people.hasNext()) {
      person = people.next();
      if (person.getLabel().equals(TEST_SIMAL_PROJECT_DEVELOPERS)) {
        hasDeveloper = true;
      }
      assertNotNull("No person should have a null ID (see " + person.getQName().toString() + ")", person.getSimalId());
    }
    assertTrue("Project does not appear to have developer "
        + TEST_SIMAL_PROJECT_DEVELOPERS, hasDeveloper);
  }

  @Test
  public void testGetDocumentors() throws SimalRepositoryException {
    boolean hasDocumentor = false;
    Iterator<IPerson> people = project1.getDocumenters().iterator();
    IPerson person;
    while (people.hasNext()) {
      person = people.next();
      if (person.getLabel().equals(TEST_SIMAL_PROJECT_DOCUMENTORS)) {
        hasDocumentor = true;
      }
    }
    assertTrue("Project does not appear to have documentor "
        + TEST_SIMAL_PROJECT_DOCUMENTORS, hasDocumentor);
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
    boolean hasHelper = false;
    Iterator<IPerson> people = project1.getHelpers().iterator();
    IPerson person;
    while (people.hasNext()) {
      person = people.next();
      if (person.getLabel().equals(TEST_SIMAL_PROJECT_HELPERS)) {
        hasHelper = true;
      }
    }
    assertTrue("Project does not appear to have helper "
        + TEST_SIMAL_PROJECT_HELPERS, hasHelper);
  }

  @Test
  public void testGetHomepages() {
    boolean hasHomepageOne = false;
    boolean hasHomepageTwo = false;
    Iterator<IDoapHomepage> homepages = project1.getHomepages().iterator();
    String label;
    while (homepages.hasNext()) {
      IDoapHomepageBehaviour homepage = (IDoapHomepageBehaviour) homepages
          .next();
      label = homepage.getLabel();
      if (label.equals(TEST_SIMAL_PROJECT_HOMEPAGE_ONE)) {
        hasHomepageOne = true;
      } else if (label.equals(TEST_SIMAL_PROJECT_HOMEPAGE_TWO)) {
        hasHomepageTwo = true;
      }
    }
    assertTrue("Homepages do not include " + TEST_SIMAL_PROJECT_HOMEPAGE_ONE,
        hasHomepageOne);
    assertTrue("Homepages do not include " + TEST_SIMAL_PROJECT_HOMEPAGE_TWO,
        hasHomepageTwo);
  }

  @Test
  public void testGetMailingLists() {
    Set<IDoapMailingList> lists = project1.getMailingLists();
    assertEquals("Got incorrect number of mailing lists",
        TEST_SIMAL_PROJECT_NUMBER_OF_MAILING_LIST, lists.size());
    
    boolean hasListOne = false;
    boolean hasListTwo = false;
    Iterator<IDoapMailingList> itrLists = lists.iterator();
    IDoapMailingList list;
    while (itrLists.hasNext()) {
      list = itrLists.next();
      if (list.getLabel().equals(TEST_SIMAL_PROJECT_MAILING_LIST_ONE)) {
        hasListOne = true;
      } else if (list.getLabel().equals(TEST_SIMAL_PROJECT_MAILING_LIST_TWO)) {
        hasListTwo = true;
      }
    }
    assertTrue("Project does not appear to have list "
        + TEST_SIMAL_PROJECT_MAILING_LIST_ONE, hasListOne);
    assertTrue("Project does not appear to have maintainer "
        + TEST_SIMAL_PROJECT_MAILING_LIST_TWO, hasListTwo);
  }

  @Test
  public void testGetMaintainers() throws SimalRepositoryException {
    boolean hasMaintainerOne = false;
    boolean hasMaintainerTwo = false;
    Iterator<IPerson> people = project1.getMaintainers().iterator();
    IPerson person;
    while (people.hasNext()) {
      person = people.next();
      if (person.getLabel().equals(TEST_SIMAL_PROJECT_MAINTAINER_ONE)) {
        hasMaintainerOne = true;
      } else if (person.getLabel().equals(TEST_SIMAL_PROJECT_MAINTAINER_TWO)) {
        hasMaintainerTwo = true;
      }
    }
    assertTrue("Project does not appear to have maintainer "
        + TEST_SIMAL_PROJECT_MAINTAINER_ONE, hasMaintainerOne);
    assertTrue("Project does not appear to have maintainer "
        + TEST_SIMAL_PROJECT_MAINTAINER_TWO, hasMaintainerTwo);
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
    Set<IDoapRelease> releases = project1.getReleases();
    assertEquals("We don't have the right number of releases", 1, releases
        .size());

    boolean hasRelease = false;
    Iterator<IDoapRelease> itrReleases = releases.iterator();
    IDoapRelease release;
    while (itrReleases.hasNext()) {
      release = itrReleases.next();
      if (release.getLabel().equals(TEST_SIMAL_PROJECT_RELEASES)) {
        hasRelease = true;
      }
    }
    assertTrue("We don't seem to have the release " + TEST_SIMAL_PROJECT_RELEASES, hasRelease);
  }

  @Test
  public void testGetRepositories() throws SimalRepositoryException {
    Set<IDoapRepository> repos = project1.getRepositories();
    IDoapRepository repo = (IDoapRepository) repos.toArray()[0];
    assertEquals(TEST_SIMAL_PROJECT_REPOSITORIES, repo.getLabel());
  }

  @Test
  public void testGetScreenshots() {
    String screenshots = project1.getDoapScreenshots().toString();
    assertTrue(screenshots.contains(TEST_SIMAL_PROJECT_SCREENSHOTS));
  }

  @Test
  public void testGetTesters() throws SimalRepositoryException {
    boolean hasTesterOne = false;
    Iterator<IPerson> people = project1.getTesters().iterator();
    IPerson person;
    while (people.hasNext()) {
      person = people.next();
      if (person.getLabel().equals(TEST_SIMAL_PROJECT_TESTERS)) {
        hasTesterOne = true;
      }
    }
    assertTrue("Project does not appear to have tester "
        + TEST_SIMAL_PROJECT_TESTERS, hasTesterOne);
  }

  @Test
  public void testGetTranslators() throws SimalRepositoryException {
    boolean hasTranslatorOne = false;
    Iterator<IPerson> people = project1.getTranslators().iterator();
    IPerson person;
    while (people.hasNext()) {
      person = people.next();
      if (person.getLabel().equals(TEST_SIMAL_PROJECT_TRANSLATORS)) {
        hasTranslatorOne = true;
      }
    }
    assertTrue("Project does not appear to have tester "
        + TEST_SIMAL_PROJECT_TRANSLATORS, hasTranslatorOne);
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
    IProject project;
    try {
      project = repository.createProject(qname);
      project.addName("Testing");
      project.setDoapShortdesc("Just testing adding a manually built project");

      project = repository.getProject(qname);
      assertNotNull("Project has not been added to repository", project);

      assertEquals("Project name is incorrectly set", "Testing", project
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
    assertEquals("Test project ID incorrect", "1", project1.getID());
    
    QName qname1 = new QName(SimalRepository.DEFAULT_PROJECT_NAMESPACE_URI
        + "TestingId1");
    QName qname2 = new QName(SimalRepository.DEFAULT_PROJECT_NAMESPACE_URI
        + "TestingId2");

    IProject project;
    project = repository.createProject(qname1);
    project = repository.getProject(qname1);
    String id1 = project.getID();

    project = repository.createProject(qname2);
    project = repository.getProject(qname2);
    String id2 = project.getID();

    assertFalse("Project IDs are not unique: " + id1 + " == " + id2, id1
        .equals(id2));

    // check IDs are being written to the repository
    project = repository.getProject(qname1);
    // String id3 = project.getID();
    // FIXME (ISSUE 88): projectIDs are not currently written to the repository
    // assertTrue("Project IDs don't appear to be written to the repo",
    // id1.equals(id3));
  }
}

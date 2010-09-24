/*
 * Copyright 2007 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.osswatch.simal.integrationTest.model;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.integrationTest.model.repository.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IDoapMailingList;
import uk.ac.osswatch.simal.model.IDoapRelease;
import uk.ac.osswatch.simal.model.IDoapRepository;
import uk.ac.osswatch.simal.model.IDocument;
import uk.ac.osswatch.simal.model.IFeed;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.ModelSupport;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;

public class TestProject extends BaseRepositoryTest {
  private static final Logger logger = LoggerFactory
      .getLogger(TestProject.class);

  private static Set<IPerson> maintainers;
  private static Set<IPerson> developers;
  private static Set<IPerson> documenters;
  private static Set<IPerson> helpers;
  private static Set<IPerson> translators;
  private static Set<IPerson> testers;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    initRepository();

    maintainers = project1.getMaintainers();
    Iterator<IPerson> itr = maintainers.iterator();
    while (itr.hasNext()) {
      IPerson person = itr.next();
      logger.debug("Got person: " + person + " : " + person.getURI());
    }
    developers = project1.getDevelopers();
    helpers = project1.getHelpers();
    documenters = project1.getHelpers();
    translators = project1.getTranslators();
    testers = project1.getTesters();
  }

  @Test
  public void testAddRemoveName() throws SimalRepositoryException { 
    String name = "Unit Test Name";
    project1.addName(name);
    String names = project1.getNames().toString();
	assertTrue("We haven't set the name succesfully, names array does not contain the new name", names.contains(name));
	assertEquals("We haven't set the name succesfully", name, project1.getName());
	project1.removeName(name);
	names = project1.getNames().toString();
	assertFalse("We haven't removed the name succesfully, names array still contains the new name", names.contains(name));
  }

  @Test
  public void testPersonsLoaded() {
    assertEquals("Have got incorrect number of maintainers",
        BaseRepositoryTest.TEST_SIMAL_PROJECT_NUMBER_OF_MAINTAINERS,
        maintainers.size());
    assertEquals("Have got incorrect number of developers",
        TEST_SIMAL_PROJECT_NUMBER_OF_DEVELOPERS, developers.size());
    assertEquals("Have got incorrect number of helpers",
        TEST_SIMAL_PROJECT_NUMBER_OF_HELPERS, helpers.size());
    assertEquals("Have got incorrect number of documenters",
        TEST_SIMAL_PROJECT_NUMBER_OF_DOCUMENTERS, documenters.size());
    assertEquals("Have got incorrect number of translators",
        TEST_SIMAL_PROJECT_NUMBER_OF_TRANSLATORS, translators.size());
    assertEquals("Have got incorrect number of testers",
        TEST_SIMAL_PROJECT_NUMBER_OF_TESTERS, testers.size());
  }

  @Test
  public void testURI() throws SimalRepositoryException {
    assertEquals("Project URI is incorrect", testProjectURI, project1.getURI());
  }

  @Test
  public void testToJSON() throws SimalRepositoryException {
    String json = project1.toJSONRecord();
    logger.debug("Project JSON record is :\n" + json);

    assertTrue("JSON file has incorrect project name: " + json, json
        .contains("\"name\":\"" + TEST_SIMAL_PROJECT_NAME));

    assertTrue("JSON file does not contain simalID", json
        .contains("\"simalID\":\""));

    assertTrue("JSON file has incorrect short description: " + json, json
        .contains("\",\"shortdesc\":\"" + TEST_SIMAL_PROJECT_SHORT_DESC));

    assertTrue("JSON record (incorrectly) has items structure: : " + json,
        !json.startsWith("{ \"items\": ["));

    json = project1.toJSON();
    logger.debug("Project JSON file is :\n" + json);

    assertTrue("JSON file does not have items structure: " + json, json
        .startsWith("{ \"items\": ["));
  }

  @Test
  public void testGetIssueTracker() {
    String issueTrackers = project1.getIssueTrackers().toString();
    assertTrue(issueTrackers.contains(TEST_SIMAL_PROJECT_ISSUE_TRACKER));
  }

  @Test
  public void testGetCategories() {
    boolean hasCategoryTwo = false;
    Iterator<IDoapCategory> categories = project1.getCategories().iterator();
    String label;
    while (categories.hasNext()) {
      label = categories.next().getLabel();
      if (label.equals(TEST_SIMAL_PROJECT_CATEGORY_TWO)) {
        hasCategoryTwo = true;
      }
    }
    assertTrue("Categories do not include " + TEST_SIMAL_PROJECT_CATEGORY_TWO,
        hasCategoryTwo);
  }

  @Test
  public void testGetDevelopers() throws SimalRepositoryException {
    logger.info("Starting testGetDevelopers");
    boolean hasDeveloper = false;
    Iterator<IPerson> people = project1.getDevelopers().iterator();
    IPerson person;
    while (!hasDeveloper && people.hasNext()) {
      person = people.next();
      if (person.getNames().contains(TEST_SIMAL_PROJECT_DEVELOPERS)) {
        hasDeveloper = true;
      }
      logger.debug("Got developer called {}", person);
      assertNotNull("No person should have a null ID (see " + person.getURI()
          + ")", person.getSimalID());
    }
    assertTrue("Project does not appear to have developer called "
        + TEST_SIMAL_PROJECT_DEVELOPERS, hasDeveloper);
    logger.info("Finished testGetDevelopers");
  }

  @Test
  public void testGetDocumenters() throws SimalRepositoryException {
    boolean hasDocumenter = false;
    Iterator<IPerson> people = project1.getDocumenters().iterator();
    IPerson person;
    while (!hasDocumenter && people.hasNext()) {
      person = people.next();
      if (person.getLabel().equals(TEST_SIMAL_PROJECT_DOCUMENTERS)) {
        hasDocumenter = true;
      }
    }
    assertTrue("Project does not appear to have documenter "
        + TEST_SIMAL_PROJECT_DOCUMENTERS, hasDocumenter);
  }

  @Test
  public void testGetDownloadMirrors() {
    String mirrors = project1.getDownloadMirrors().toString();
    assertTrue(mirrors.contains(TEST_SIMAL_PROJECT_DOWNLOAD_MIRRORS));
  }

  @Test
  public void testGetDownloadPages() {
    String downloads = project1.getDownloadPages().toString();
    assertTrue(downloads.contains(TEST_SIMAL_PROJECT_DOWNLOAD_PAGES));
  }

  @Test
  public void testGetHelpers() throws SimalRepositoryException {
    boolean hasHelper = false;
    Iterator<IPerson> people = project1.getHelpers().iterator();
    IPerson person;
    while (!hasHelper && people.hasNext()) {
      person = people.next();
      if (person.getLabel().equals(TEST_SIMAL_PROJECT_HELPERS)) {
        hasHelper = true;
      }
    }
    assertTrue("Project does not appear to have helper "
        + TEST_SIMAL_PROJECT_HELPERS, hasHelper);
  }
  
  @Test
  public void testAddandRemoveHomepage() throws SimalRepositoryException, DuplicateURIException {
	    Set<IDocument> prePages = project1.getHomepages();

	    String uri = "http://www.foo.org/homepageTest";
	    IDocument page = SimalRepositoryFactory.getHomepageService().create(uri);

	    project1.addHomepage(page);
	    Set<IDocument> postPages = project1.getHomepages();
	    assertTrue("Failed to add a homepage to the project",
	        prePages.size() < postPages.size());

	    project1.removeHomepage(page);
	    postPages = project1.getHomepages();
	    assertTrue("Failed to remove a homepage from the project", prePages
	        .size() == postPages.size());

	    page.delete();
  }

  @Test
  public void testGetHomepages() {
    Set<IDocument> homepages = project1.getHomepages();

    assertEquals("Incorrect number of home pages",
        TEST_SIMAL_PROJECT_NUMBER_OF_HOMEPAGES, homepages.size());
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
    while ((!hasMaintainerOne || !hasMaintainerTwo) && people.hasNext()) {
      person = people.next();
      Set<String> names = person.getNames();
      logger.debug("Got a maintainer with the names {}", names);
      if (names.contains(TEST_SIMAL_PROJECT_MAINTAINER_ONE)) {
        hasMaintainerOne = true;
      } else if (names.contains(TEST_SIMAL_PROJECT_MAINTAINER_TWO)) {
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
    String oldHomes = project1.getOldHomepages().toString();
    assertTrue(oldHomes.contains(TEST_SIMAL_PROJECT_OLD_HOMEPAGES));
  }

  @Test
  public void testGetOSes() {
    assertEquals(TEST_SIMAL_PROJECT_OS, project1.getOSes().toArray()[0]
        .toString());
  }

  @Test
  public void testGetProgrammingLangauges() {
    assertTrue(project1.getProgrammingLanguages().toString().contains(
        TEST_SIMAL_PROJECT_PROGRAMMING_LANGUAGE_ONE));
    assertTrue(project1.getProgrammingLanguages().toString().contains(
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
    assertTrue("We don't seem to have the release "
        + TEST_SIMAL_PROJECT_RELEASES, hasRelease);
  }

  @Test
  public void testGetRepositories() throws SimalRepositoryException {
    Set<IDoapRepository> repositories = project1.getRepositories();
    assertNotNull("Failed to get the repositories", repositories);
  }

  @Test
  public void testGetScreenshots() {
    String screenshots = project1.getScreenshots().toString();
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
    String wikis = project1.getWikis().toString();
    assertTrue(wikis.contains(TEST_SIMAL_PROJECT_WIKIS));
  }

  @Test
  public void testAddProjectFromScratch() throws SimalRepositoryException,
      URISyntaxException {
    String uri = RDFUtils.PROJECT_NAMESPACE_URI + "TestingProjectFromScratch";
    IProject project;
    try {
      project = SimalRepositoryFactory.getProjectService().createProject(uri);
      project.addName("Testing");
      project.setShortDesc("Just testing adding a manually built project");

      project = SimalRepositoryFactory.getProjectService().getProject(uri);
      assertNotNull("Project has not been added to repository", project);
      assertEquals("Project name is incorrectly set", "Testing", project
          .getName());

      project.delete();
    } catch (DuplicateURIException e) {
      fail(e.getMessage());
    }
  }

  /**
   * Test to ensure that project ids are being correctly generated.
   * 
   * @throws SimalRepositoryException
   * @throws DuplicateURIException
   * @throws URISyntaxException
   */
  @Test
  public void testId() throws SimalRepositoryException, DuplicateURIException,
      URISyntaxException {
    assertEquals("Test project ID incorrect", testProjectID, project1
        .getUniqueSimalID());

    String uri1 = RDFUtils.PROJECT_NAMESPACE_URI + "TestingId1";
    String uri2 = RDFUtils.PROJECT_NAMESPACE_URI + "TestingId2";

    IProject project;
    project = SimalRepositoryFactory.getProjectService().createProject(uri1);
    project = SimalRepositoryFactory.getProjectService().getProject(uri1);
    String id1 = project.getSimalID();

    project = SimalRepositoryFactory.getProjectService().createProject(uri2);
    project = SimalRepositoryFactory.getProjectService().getProject(uri2);
    String id2 = project.getSimalID();

    assertFalse("Project IDs are not unique: " + id1 + " == " + id2, id1
        .equals(id2));

    // check IDs are being written to the repository
    project = SimalRepositoryFactory.getProjectService().getProject(uri1);
    String id3 = project.getSimalID();
    assertTrue("Project IDs don't appear to be written to the repo", id1
        .equals(id3));
    
    // check we are retrieving projects by ID correctly
    project = SimalRepositoryFactory.getProjectService().getProjectById(id1);
    assertNotNull(project);
    
    project.delete();
    project = SimalRepositoryFactory.getProjectService().getProject(uri2);
    project.delete();
  }

  @Test
  public void testToXML() throws SimalRepositoryException {
    String xml = project1.toXML();
    assertTrue("XML does not contain rdf:RDF", xml.contains("rdf:RDF"));
    // assertTrue("XML does not contain doap:Project",
    // xml.contains("doap:Project"));
  }

  @Test
  public void testGetAllPeople() throws SimalRepositoryException {
    HashSet<IPerson> people = project1.getAllPeople();
    assertNotNull(people);
    assertEquals("Got the wrong number of people", BaseRepositoryTest
        .getNumberOfParticipants(), people.size());
  }

  @Test
  public void testAddAndRemoveDeveloper() throws SimalRepositoryException,
      DuplicateURIException {
    Set<IPerson> prePeople = project1.getDevelopers();

    String uri = RDFUtils.PERSON_NAMESPACE_URI + "TestingPersonFromScratch";
    IPerson person = SimalRepositoryFactory.getPersonService().create(uri);

    project1.addDeveloper(person);
    Set<IPerson> postPeople = project1.getDevelopers();
    assertTrue("Failed to add a developer to the project",
        prePeople.size() < postPeople.size());

    project1.removeDeveloper(person);
    postPeople = project1.getDevelopers();
    assertTrue("Failed to remove a developer from the project", prePeople
        .size() == postPeople.size());

    person.delete();
  }
  
  @Test
  public void testGetFeeds() {
    Set<IFeed> feeds = project1.getFeeds();
    assertNotNull(feeds);
  }
  
  @Test
  public void testOpennessRating() throws SimalRepositoryException {
	  int rating = project1.getOpennessRating();
	  assertEquals("Got an incorrect openness rating for the test project", 100, rating);
  }
  
  @Test
  public void testAddExisting() throws SimalRepositoryException {
	  int preCount = project1.getHomepages().size();
      repository.addProject(ISimalRepository.class.getClassLoader().getResource(
          ModelSupport.TEST_FILE_URI_NO_QNAME), ModelSupport.TEST_FILE_BASE_URL);
      int postCount = project1.getHomepages().size();
      
      assertEquals("When adding an existing project we should not get extra homepages", preCount, postCount);
  }
}

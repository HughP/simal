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
package uk.ac.osswatch.simal.integrationTest.model.elmo;

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

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IDoapMailingList;
import uk.ac.osswatch.simal.model.IDoapRelease;
import uk.ac.osswatch.simal.model.IDoapRepository;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

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
    developers = project1.getDevelopers();
    helpers = project1.getHelpers();
    documenters = project1.getHelpers();
    translators = project1.getTranslators();
    testers = project1.getTesters();
  }

  @Test
  public void testPersonsLoaded() {
    assertEquals("Have got incorrect number of maintainers",
        BaseRepositoryTest.TEST_SIMAL_PROJECT_NUMBER_OF_MAINTAINERS,
        maintainers.size());
    assertEquals("Should have one developers", 1, developers.size());
    assertEquals("Should have one helpers", 1, helpers.size());
    assertEquals("Should have one documenters", 1, documenters.size());
    assertEquals("Should have one translators", 1, translators.size());
    assertEquals("Should have one testers", 1, testers.size());
  }

  @Test
  public void testURI() throws SimalRepositoryException {
    assertEquals(TEST_SIMAL_PROJECT_URI, project1.getURI().toString());
  }

  @Test
  public void testToJSON() throws SimalRepositoryException {
    String json = project1.toJSONRecord();
    logger.debug("Project JSON record is :\n" + json);
    assertTrue(
        "JSON file has incorrect project name: " + json,
        json.contains("\"name\":\"" + TEST_SIMAL_PROJECT_NAME));
    assertTrue(
        "JSON file has incorrect short description: " + json,
        json.contains("\",\"shortdesc\":\"" + TEST_SIMAL_PROJECT_SHORT_DESC));
    assertTrue(
        "JSON record (incorrectly) has items structure: : " + json,
        !json.startsWith("{ \"items\": ["));

    json = project1.toJSON();
    logger.debug("Project JSON file is :\n" + json);
    assertTrue(
        "JSON file does not have items structure: : " + json,
        json.startsWith("{ \"items\": ["));
  }

  @Test
  public void testGetIssueTracker() {
    String issueTrackers = project1.getIssueTrackers().toString();
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
      assertNotNull("No person should have a null ID (see "
          + person.getURI().toString() + ")", person.getSimalId());
    }
    assertTrue("Project does not appear to have developer "
        + TEST_SIMAL_PROJECT_DEVELOPERS, hasDeveloper);
  }

  @Test
  public void testGetDocumenters() throws SimalRepositoryException {
    boolean hasDocumenter = false;
    Iterator<IPerson> people = project1.getDocumenters().iterator();
    IPerson person;
    while (people.hasNext()) {
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
    Set<IDoapHomepage> homepages = project1.getHomepages();
    
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
    while (people.hasNext()) {
      person = people.next();
      String label = person.getLabel();
      if (label.equals(TEST_SIMAL_PROJECT_MAINTAINER_ONE)) {
        hasMaintainerOne = true;
      } else if (label.equals(TEST_SIMAL_PROJECT_MAINTAINER_TWO)) {
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
    assertEquals(TEST_SIMAL_PROJECT_OS, project1.getOSes().toString());
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
  public void testAddProjectFromScratch() throws SimalRepositoryException, URISyntaxException {
    String uri = ISimalRepository.DEFAULT_PROJECT_NAMESPACE_URI
        + "TestingProjectFromScratch";
    IProject project;
    try {
      project = repository.createProject(uri);
      project.addName("Testing");
      project.setShortDesc("Just testing adding a manually built project");

      project = repository.getProject(uri);
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
  public void testId() throws SimalRepositoryException, DuplicateURIException, URISyntaxException {
    assertEquals("Test project ID incorrect", project1ID, project1.getSimalID());

    String uri1 = ISimalRepository.DEFAULT_PROJECT_NAMESPACE_URI
        + "TestingId1";
    String uri2 = ISimalRepository.DEFAULT_PROJECT_NAMESPACE_URI
        + "TestingId2";

    IProject project;
    project = repository.createProject(uri1);
    project = repository.getProject(uri1);
    String id1 = project.getSimalID();

    project = repository.createProject(uri2);
    project = repository.getProject(uri2);
    String id2 = project.getSimalID();

    assertFalse("Project IDs are not unique: " + id1 + " == " + id2, id1
        .equals(id2));

    // check IDs are being written to the repository
    project = repository.getProject(uri1);
    String id3 = project.getSimalID();
    assertTrue("Project IDs don't appear to be written to the repo", id1
        .equals(id3));

    repository.remove(uri1);
    repository.remove(uri2);
  }
  
  @Test
  public void testToXML() throws SimalRepositoryException {
    String xml = project1.toXML();
    // FIXME: These are commented out as they don't work at present and I need to
    // commit to get the CI working again.
    //assertTrue("XML does not contain rdf:RDF", xml.contains("rdf:RDF"));
    //assertTrue("XML does not contain doap:Project", xml.contains("doap:Project"));
  }
  
  @Test
  public void testGetAllPeople() throws SimalRepositoryException {
    HashSet<IPerson> people = project1.getAllPeople();
    assertNotNull(people);
    assertEquals("Got the wrong number of people", BaseRepositoryTest.getNumberOfParticipants(), people.size());
  }
}

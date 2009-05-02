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
package uk.ac.osswatch.simal.integrationTest.rdf;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.ModelSupport;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;

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
    assertTrue(getRepository().isInitialised());
    logger.debug("Finished testAddProject()");
  }

  @Test
  public void testFilterProjectsByName() {
    // Test exact Match
    Set<IProject> projects = getRepository().filterProjectsByName("Simal Project Catalogue System");
    assertEquals("Not the right number of projects with the name 'Simal Project Catalogue System'", 1, projects.size());
    
    // Test wildcard match
    projects = getRepository().filterProjectsByName("Simal");
    assertEquals("Not the right number of projects match the filter 'Simal'", 4, projects.size());
  }

  @Test
  public void testFilterPeopleByName() {
    // Test exact Match
    Set<IPerson> people = getRepository().filterPeopleByName("Ross Gardler");
    assertEquals("Not the right number of projects with the name 'Ross Gardler'", 1, people.size());
    
    // Test wildcard match
    people = getRepository().filterPeopleByName("Ro");
    assertEquals("Not the right number of projects match the filter 'Ro'", 3, people.size());
  }

  @Test
  public void testAddCategories() throws SimalRepositoryException,
      URISyntaxException {
    Set<IDoapCategory> cats = getRepository().getAllCategories();
    assertTrue("Not managed to get any categories from the repo",
        cats.size() > 0);

    Iterator<IDoapCategory> catsIt = cats.iterator();
    while (catsIt.hasNext()) {
      IDoapCategory cat = catsIt.next();
      logger.debug("Got category: " + cat.getLabel());
    }
  }

  @Test
  public void testFindProject() throws SimalRepositoryException,
      URISyntaxException {
    logger.debug("Starting testFindProject()");
    String uri = "http://foo.org/nonExistent";
    IProject project = getRepository().getProject(uri);
    assertNull(project);

    // test a known valid file
    project = getRepository().findProjectBySeeAlso(TEST_PROJECT_URI);
    assertEquals("Simal DOAP Test", project.getName());
    logger.debug("Finished testFindProject()");
  }

  @Test
  public void testGetRdfXml() throws SimalRepositoryException,
      URISyntaxException {
    logger.debug("Starting testGetRdfXML()");
    String xml = project1.toXML();
    assertTrue("XML does not contain the URI expected", xml
        .contains("rdf:about=\"" + testProjectURI + "\""));
    int indexOf = xml.indexOf("<doap:Project");
    int lastIndexOf = xml.lastIndexOf("<doap:Project");
    assertTrue("XML appears to contain more than one project record",
        indexOf == lastIndexOf);
    logger.debug("Finished testGetRdfXML()");
  }

  @Test
  public void testGetAllCategories() throws SimalRepositoryException,
      IOException {
    Set<IDoapCategory> cats = getRepository().getAllCategories();

    Iterator<IDoapCategory> itrCats = cats.iterator();
    IDoapCategory cat;
    while (itrCats.hasNext()) {
      cat = itrCats.next();
      logger.debug("Got category: " + cat.getSimalID() + " : " + cat.getURI());
    }

    assertTrue("Got too few categories", 50 < cats.size());
  }

  @Test
  public void testGetAllProjects() throws SimalRepositoryException, IOException {
    logger.debug("Starting testGetAllProjects()");
    Set<IProject> projects = getRepository().getAllProjects();

    Iterator<IProject> itrProjects = projects.iterator();
    IProject project;
    while (itrProjects.hasNext()) {
      project = itrProjects.next();
      assertNotNull(project.getName());
      logger.debug("Got project: " + project.getName() + " with URI " + project.getURI());
    }

    assertEquals(6, projects.size());
    logger.debug("Finished testGetAllProjects()");
  }

  @Test
  public void testGetAllPeople() throws SimalRepositoryException, IOException {
    Set<IPerson> people = getRepository().getAllPeople();

    Iterator<IPerson> itrPeople = people.iterator();
    IPerson person;
    while (itrPeople.hasNext()) {
      person = itrPeople.next();
      assertNotNull(person.getLabel());
      logger.debug("Got person: " + person + " : " + person.getURI());
    }

    assertEquals(18, people.size());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testNullQNameHandling() throws SimalRepositoryException {
    logger.debug("Starting testNullQNameHandling()");
    Set<IProject> projects = getRepository().getAllProjects();

    Iterator<IProject> itrProjects = projects.iterator();
    IProject project;
    while (itrProjects.hasNext()) {
      project = itrProjects.next();
      assertNotNull("All projects must have a QName", project.getURI());
    }
    logger.debug("Finished testNullQNameHandling()");
  }

  @Test
  public void testGetAllProjectsAsJSON() throws SimalRepositoryException {
    logger.debug("Starting testGetAllProjectsAsJSON()");
    String json = getRepository().getAllProjectsAsJSON();
    assertTrue("JSON file does not appear to be correct", json
        .startsWith("{ \"items\": ["));
    assertTrue("JSON file does not appear to be correct", json.endsWith("]}"));
    logger.debug("Finished testGetAllProjectsAsJSON()");
  }

  @Test
  public void testGetAllPeopleAsJSON() throws SimalRepositoryException {
    logger.debug("Starting testGetAllPeopleAsJSON()");
    Long targetTime = Long.valueOf(75);
    Long startTime = System.currentTimeMillis();
    String json = getRepository().getAllPeopleAsJSON();
    Long endTime = System.currentTimeMillis();
    Long actualTime = endTime - startTime;
    assertTrue("Time to create JSON for all people is longer than "
        + targetTime + " took " + actualTime, actualTime <= targetTime);
    assertTrue("JSON file does not appear to be correct", json
        .startsWith("{ \"items\": ["));
    assertTrue("JSON file does not appear to be correct", json.endsWith("]}"));
    logger.debug("Finished testGetAllPeoplesAsJSON()");
  }

  @Test
  public void testFindCategoryById() throws SimalRepositoryException {
    IDoapCategory cat = getRepository().findCategoryById("1");
    assertNotNull(cat);
    assertEquals("Category name is incorrect", "Simal ID Test", cat.getName());
  }

  @Test
  public void testFindPersonById() throws SimalRepositoryException {
    IPerson person = getRepository().findPersonById(testDeveloperID);
    assertNotNull("Can't find a person with the ID " + testDeveloperID, person);
    assertEquals("Developer URI is not as expected ", RDFUtils
        .getDefaultPersonURI(testDeveloperID), person.getURI());
  }

  @Test
  public void testGetPerson() throws SimalRepositoryException {
    IPerson person = getRepository().getPerson(testDeveloperURI);
    assertNotNull("Can't find a person with the URI " + testDeveloperURI,
        person);
    assertEquals("Developer URI is not as expected ", RDFUtils
        .getDefaultPersonURI(testDeveloperID), person.getURI());
  }

  @Test
  public void testFindProjectById() throws SimalRepositoryException {
    IProject project = getRepository().findProjectById(testProjectID);
    assertNotNull("Failed to get project with ID " + testProjectID, project);
  }

  @Test
  public void testFindProjectBySeeAlso() throws SimalRepositoryException {
    logger.debug("Starting testFindProjectBySeeAlso()");
    IProject project = getRepository().findProjectBySeeAlso(project1SeeAlso);
    assertNotNull(project);
    logger.debug("Finished testFindProjectBySeeAlso()");
  }

  @Test
  public void testAdd() throws SimalRepositoryException, URISyntaxException,
      IOException {
    logger.debug("Starting testAdd(data)");
    project1.delete();
    project1 = getRepository().findProjectBySeeAlso(TEST_PROJECT_URI);
    assertNull("Project has not been deleted as expected", project1);
    
    int peopleBefore = getRepository().getAllPeople().size();
    File testFile = new File(ISimalRepository.class.getClassLoader()
        .getResource(ModelSupport.TEST_FILE_URI_WITH_QNAME).toURI());
    FileInputStream fis = new FileInputStream(testFile);
    int x = fis.available();
    byte b[] = new byte[x];
    fis.read(b);
    String data = new String(b);
    getRepository().add(data);
    int peopleAfter = getRepository().getAllPeople().size();
    
    
    Iterator<IPerson> people = getRepository().getAllPeople().iterator();
    while (people.hasNext()) {
      IPerson person = people.next();
      logger.debug("We have got a person labelled " + person.getLabel() + " from " + person.getSeeAlso().toString());
    }    
    assertEquals("We have more people after adding a duplicate than we did before", peopleBefore, peopleAfter);
    
    project1 = getRepository().findProjectBySeeAlso(TEST_PROJECT_URI);
    assertNotNull("We don't seem to have added the test data as expected",
        project1);
    logger.debug("Starting testAdd(data)");
  }

  @Test
  public void testDuplicatePeople() throws SimalRepositoryException {
    int beforeSize = getRepository().getAllPeople().size();
    Iterator<IPerson> itr = getRepository().getAllPeople().iterator();
    logger.debug("People before second data addition:");
    while (itr.hasNext()) {
      IPerson person = itr.next();
      logger.debug(person.toString());
    }

    ModelSupport.addSimalData(getRepository());

    int afterSize = getRepository().getAllPeople().size();
    itr = getRepository().getAllPeople().iterator();
    logger.debug("People after second data addition are:");
    while (itr.hasNext()) {
      IPerson person = itr.next();
      logger.debug(person.toString());
    }
    assertEquals(
        "Adding the test data a second time has resulted in duplicate people entries",
        beforeSize, afterSize);
  }

  @Test
  public void testDuplicateProjects() throws SimalRepositoryException {
    Set<IProject> before = getRepository().getAllProjects();
    ModelSupport.addSimalData(getRepository());
    Set<IProject> after = getRepository().getAllProjects();
    assertEquals(
        "Adding the test data a second time has resulted in duplicate project entries",
        before.size(), after.size());
  }

  @Test
  public void testIsValidSimalID() throws SimalRepositoryException {
    String id = null;
    try {
      getRepository().findPersonById(id);
      fail("Null Simal IDs should not be valid");
    } catch (SimalRepositoryException e) {
      // This is expected
    }

    id = "inValidID";
    try {
      getRepository().findPersonById(id);
      fail("Simal IDs should have an instance ID + ':' + an entity ID");
    } catch (SimalRepositoryException e) {
      // This is expected
    }

    id = "a:inValidID";
    try {
      getRepository().findPersonById(id);
      fail("Simal instance IDs should be at least 5 characters long");
    } catch (SimalRepositoryException e) {
      // This is expected
    }

    id = "ThisIsA-validID";
    getRepository().findPersonById(id);
  }

  @Test
  public void testIsUniqueSimalID() throws SimalRepositoryException {
    String id = null;
    assertFalse("Null Simal IDs should not be unqiue", getRepository()
        .isUniqueSimalID(id));

    id = "nonUniqueID";
    assertFalse("Non-Unique Simal IDs identified as unqiue", getRepository()
        .isUniqueSimalID(id));

    id = "a:inValidID";
    assertFalse("Invalid Simal IDs should not be considered unqiue", getRepository()
        .isUniqueSimalID(id));

    id = "ThisIsA-uniqueID";
    assertTrue("Valid Simal IDs should be considered unqiue", getRepository()
        .isUniqueSimalID(id));
  }
  
  @Test
  public void testGetBackup() throws SimalRepositoryException {
    ISimalRepository repo = getRepository();
    StringWriter sw = new StringWriter();
    repo.writeBackup(sw);
    String backup = sw.toString();
    assertNotNull(backup);
  }

  @Test
  public void testSimalEntityExists() throws SimalRepositoryException {
    URL url = TestRepository.class
        .getResource("/testData/testIngest.xml");    
    Long beforeID = new Long(SimalProperties.getProperty(
        SimalProperties.PROPERTY_SIMAL_NEXT_PROJECT_ID, "1"));
    getRepository().addProject(url, "");    
    Long afterID = new Long(SimalProperties.getProperty(
        SimalProperties.PROPERTY_SIMAL_NEXT_PROJECT_ID, "1"));
    assertFalse("A new ingest should create a new Simal Project",
        beforeID.equals(afterID));
    
    beforeID = new Long(SimalProperties.getProperty(
        SimalProperties.PROPERTY_SIMAL_NEXT_PROJECT_ID, "1"));
    getRepository().addProject(url, "");
    afterID = new Long(SimalProperties.getProperty(
        SimalProperties.PROPERTY_SIMAL_NEXT_PROJECT_ID, "1"));
    assertTrue("A repeated ingest should not create a new Simal Project",
        beforeID.equals(afterID));    
  }
}

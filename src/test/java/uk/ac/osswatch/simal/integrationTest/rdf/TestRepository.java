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
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    project = getRepository().findProjectBySeeAlso(TEST_PROJECT_SEEALSO);
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
      logger.debug("Got project: " + project.getName());
    }

    assertEquals(6, projects.size());
    logger.debug("Finished testGetAllProjects()");
  }

  @Test
  public void testGetAllPeople() throws SimalRepositoryException, IOException {
    logger.debug("Starting testGetAllPeople()");
    Set<IPerson> people = getRepository().getAllPeople();

    Iterator<IPerson> itrPeople = people.iterator();
    IPerson person;
    while (itrPeople.hasNext()) {
      person = itrPeople.next();
      assertNotNull(person.getLabel());
      logger.debug("Got person: " + person + " : " + person.getURI());
    }

    assertEquals(18, people.size());

    logger.debug("Finished testGetAllPeople()");
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
    Long targetTime = Long.valueOf(50);
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
    String uniqueID = getRepository().getUniqueSimalID(testDeveloperID);
    IPerson person = getRepository().findPersonById(uniqueID);
    assertNotNull("Can't find a person with the ID " + uniqueID, person);
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
    String uniqueID = getRepository().getUniqueSimalID(testProjectID);
    IProject project = getRepository().findProjectById(uniqueID);
    assertNotNull("Failed to get projectwith ID " + uniqueID, project);
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
    project1 = getRepository().findProjectBySeeAlso(TEST_PROJECT_SEEALSO);
    assertNull("Project has not been deleted as expected", project1);

    File testFile = new File(ISimalRepository.class.getClassLoader()
        .getResource(ModelSupport.TEST_FILE_URI_WITH_QNAME).toURI());
    FileInputStream fis = new FileInputStream(testFile);
    int x = fis.available();
    byte b[] = new byte[x];
    fis.read(b);
    String data = new String(b);
    getRepository().add(data);

    project1 = getRepository().findProjectBySeeAlso(TEST_PROJECT_SEEALSO);
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

    id = "ThisIsA:validID";
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
    assertFalse("Invalid Simal IDs should not be unqiue", getRepository()
        .isUniqueSimalID(id));

    id = "ThisIsA:uniqueID";
    assertTrue("Null Simal IDs should not be unqiue", getRepository()
        .isUniqueSimalID(id));
  }
  
  @Test
  public void testGetBackup() throws SimalRepositoryException {
    ISimalRepository repo = getRepository();
    StringWriter sw = new StringWriter();
    repo.writeBackup(sw);
    String backup = sw.toString();
    assertNotNull(backup);
    
    int numProjects = count(backup, "doap:Project");
    assertEquals("Number of projects in the backup is incorrect", repo.getAllProjects().size(), numProjects);
    
    int numPeople = count(backup, "foaf:Person");
    assertEquals("Number of people in the backup is incorrect", repo.getAllPeople().size(), numPeople);
  }
  
  private int count(String base, String searchFor) {
    int len   = searchFor.length();
    int result = 0;
  
    if (len > 0) {
        int start = base.indexOf(searchFor);
        while (start != -1) {
            result++;
            start = base.indexOf(searchFor, start+len);
        }
    }
    return result;
  }

}
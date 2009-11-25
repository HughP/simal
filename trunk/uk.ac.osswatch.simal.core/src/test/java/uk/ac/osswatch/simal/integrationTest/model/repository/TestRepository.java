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
package uk.ac.osswatch.simal.integrationTest.model.repository;

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
import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.ModelSupport;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
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
  public void testAddCategories() throws SimalRepositoryException,
      URISyntaxException {
    Set<IDoapCategory> cats = SimalRepositoryFactory.getCategoryService().getAll();
    assertTrue("Not managed to get any categories from the repo",
        cats.size() > 0);

    Iterator<IDoapCategory> catsIt = cats.iterator();
    while (catsIt.hasNext()) {
      IDoapCategory cat = catsIt.next();
      logger.debug("Got category: " + cat.getLabel());
    }
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
    Set<IDoapCategory> cats = SimalRepositoryFactory.getCategoryService().getAll();

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

    assertEquals(9, projects.size());
    logger.debug("Finished testGetAllProjects()");
  }

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
  public void testFindCategoryById() throws SimalRepositoryException {
    IDoapCategory cat = SimalRepositoryFactory.getCategoryService().findById("1");
    assertNotNull(cat);
    assertEquals("Category name is incorrect", "Simal ID Test", cat.getName());
  }

  @Test
  public void testAdd() throws SimalRepositoryException, URISyntaxException,
      IOException {
    logger.debug("Starting testAdd(data)");
    project1.delete();
    project1 = SimalRepositoryFactory.getProjectService().findProjectBySeeAlso(TEST_PROJECT_URI);
    assertNull("Project has not been deleted as expected", project1);
    
    int peopleBefore = SimalRepositoryFactory.getPersonService().getAll().size();
    File testFile = new File(ISimalRepository.class.getClassLoader()
        .getResource(ModelSupport.TEST_FILE_URI_WITH_QNAME).toURI());
    FileInputStream fis = new FileInputStream(testFile);
    int x = fis.available();
    byte b[] = new byte[x];
    fis.read(b);
    String data = new String(b);
    getRepository().add(data);
    int peopleAfter = SimalRepositoryFactory.getPersonService().getAll().size();
    
    
    Iterator<IPerson> people = SimalRepositoryFactory.getPersonService().getAll().iterator();
    while (people.hasNext()) {
      IPerson person = people.next();
      logger.debug("We have got a person labelled " + person.getLabel() + " from " + person.getSeeAlso().toString());
    }    
    assertEquals("We have more people after adding a duplicate than we did before", peopleBefore, peopleAfter);
    
    project1 = SimalRepositoryFactory.getProjectService().findProjectBySeeAlso(TEST_PROJECT_URI);
    assertNotNull("We don't seem to have added the test data as expected",
        project1);
    logger.debug("Starting testAdd(data)");
  }

  @Test
  public void testDuplicatePeople() throws SimalRepositoryException {
    int beforeSize = SimalRepositoryFactory.getPersonService().getAll().size();
    Iterator<IPerson> itr = SimalRepositoryFactory.getPersonService().getAll().iterator();
    logger.debug("People before second data addition:");
    while (itr.hasNext()) {
      IPerson person = itr.next();
      logger.debug(person.toString());
    }

    ModelSupport.addSimalData(getRepository());

    int afterSize = SimalRepositoryFactory.getPersonService().getAll().size();
    itr = SimalRepositoryFactory.getPersonService().getAll().iterator();
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
    	SimalRepositoryFactory.getPersonService().findById(id);
      fail("Null Simal IDs should not be valid");
    } catch (SimalRepositoryException e) {
      // This is expected
    }

    id = "inValidID";
    try {
    	SimalRepositoryFactory.getPersonService().findById(id);
      fail("Simal IDs should have an instance ID + ':' + an entity ID");
    } catch (SimalRepositoryException e) {
      // This is expected
    }

    id = "a:inValidID";
    try {
    	SimalRepositoryFactory.getPersonService().findById(id);
      fail("Simal instance IDs should be at least 5 characters long");
    } catch (SimalRepositoryException e) {
      // This is expected
    }

    id = "ThisIsA-validID";
    SimalRepositoryFactory.getPersonService().findById(id);
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
  
  @Test
  public void testGetOrCreateCategory() throws SimalRepositoryException {
	 boolean exists =getRepository().containsResource(TEST_SIMAL_PROJECT_CATEGORY_TWO);
	 assertTrue("Test category does not exist", exists);
	 
	 IDoapCategory gotCat = SimalRepositoryFactory.getCategoryService().get(TEST_SIMAL_PROJECT_CATEGORY_TWO);
	 IDoapCategory gotOrCreateCat = SimalRepositoryFactory.getCategoryService().getOrCreate(TEST_SIMAL_PROJECT_CATEGORY_TWO);
	 assertEquals("Retrieved categories are different depending on method of retrieval", gotCat.getURI(), gotOrCreateCat.getURI());
  
	 String uri = "http://test.com/category";
	 gotCat = SimalRepositoryFactory.getCategoryService().get(uri);
	 assertNull("Retrieved a category we should not have been able to get", gotCat);
	 
	 gotOrCreateCat = SimalRepositoryFactory.getCategoryService().getOrCreate(uri);
	 assertNotNull("Failed to create a category using getOrCreateCategory", gotOrCreateCat);
	 
	 gotCat = SimalRepositoryFactory.getCategoryService().get(uri);
	 assertNotNull("Failed to retrieve a recently created category", gotCat);
	 
	 gotOrCreateCat.delete();
	 gotCat = SimalRepositoryFactory.getCategoryService().get(uri);
	 assertNull("Retrieved a category we should have deleted", gotCat);
	 
  }
}

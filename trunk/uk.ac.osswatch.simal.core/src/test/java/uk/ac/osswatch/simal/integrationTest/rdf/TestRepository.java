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
  public void testAddCategories() throws SimalRepositoryException, URISyntaxException {
    Set<IDoapCategory> cats = repository.getAllCategories();
    assertTrue("Not managed to get any categories from the repo", cats.size() > 0);
    
    Iterator<IDoapCategory> catsIt = cats.iterator();
    while(catsIt.hasNext()) {
      IDoapCategory cat = catsIt.next();
      logger.debug("Got category: " + cat.getLabel());
    }
  }

  @Test
  public void testFindProject() throws SimalRepositoryException, URISyntaxException {
    logger.debug("Starting testFindProject()");
    String uri = "http://foo.org/nonExistent";
    IProject project = repository.getProject(uri);
    assertNull(project);

    // test a known valid file
    project = getSimalTestProject();
    assertEquals("Simal DOAP Test", project.getName());
    logger.debug("Finished testFindProject()");
  }

  @Test
  public void testGetRdfXml() throws SimalRepositoryException, URISyntaxException {
    logger.debug("Starting testGetRdfXML()");
    String uri = TEST_SIMAL_PROJECT_URI;

    StringWriter sw = new StringWriter();
    repository.writeXML(sw, uri);
    String xml = sw.toString();
    assertTrue("XML does not contain the QName expected", xml
        .contains("rdf:about=\"" + TEST_SIMAL_PROJECT_URI + "\""));
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

    Iterator<IProject> itrProjects = projects.iterator();
    IProject project;
    while (itrProjects.hasNext()) {
      project = itrProjects.next();
      assertNotNull(project.getName());
      logger.debug("Got project: " + project.getName());
    }
    
    assertEquals(5, projects.size());
    logger.debug("Finished testGetAllProjects()");
  }

  @Test
  public void testGetAllPeople() throws SimalRepositoryException, IOException {
    logger.debug("Starting testGetAllPeople()");
    Set<IPerson> people = repository.getAllPeople();

    Iterator<IPerson> itrPeople = people.iterator();
    IPerson person;
    while (itrPeople.hasNext()) {
      person = itrPeople.next();
      assertNotNull(person.getLabel());
      logger.debug("Got person: " + person + " : " + person.getURI());
    }

    assertEquals(16, people.size());
    
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
      assertNotNull("All projects must have a QName", project.getURI());
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
  public void testGetAllPeopleAsJSON() throws SimalRepositoryException {
    logger.debug("Starting testGetAllPeopleAsJSON()");
    String json = repository.getAllPeopleAsJSON();
    assertTrue("JSON file does not appear to be correct", json
        .startsWith("{ \"items\": ["));
    assertTrue("JSON file does not appear to be correct", json.endsWith("]}"));
    logger.debug("Finished testGetAllPeoplesAsJSON()");
  }

  /* 
  @Test
  public void testFindPersonById() throws SimalRepositoryException {
    logger.debug("Starting testFindPersonByID()");
    IPerson person = repository.findPersonById("15");
    assertNotNull(person);
    assertEquals("developer", person.getFoafGivennames().toString());
    logger.debug("Finished testFindPersonByID()");
  }
  */

  @Test
  public void testFindProjectById() throws SimalRepositoryException {
    logger.debug("Starting testFindProjectByID()");
    IProject project = repository.findProjectById(project1ID);
    assertNotNull(project);
    logger.debug("Finished testFindProjectByID()");
  }

  @Test
  public void testFindProjectBySeeAlso() throws SimalRepositoryException {
    logger.debug("Starting testFindProjectBySeeAlso()");
    IProject project = repository.findProjectBySeeAlso(project1SeeAlso);
    assertNotNull(project);
    logger.debug("Finished testFindProjectBySeeAlso()");
  }
    
  @Test
  public void testAdd() throws SimalRepositoryException, URISyntaxException, IOException {
    logger.debug("Starting testAdd(data)");
    project1.delete();
    project1 = getSimalTestProject();
    assertNull("Project has not been deleted as expected", project1);
    
    File testFile = new File(ISimalRepository.class.getResource("/testData/" + ModelSupport.TEST_FILE_URI_WITH_QNAME).toURI());
    FileInputStream fis = new FileInputStream(testFile);
    int x= fis.available();
    byte b[]= new byte[x];
    fis.read(b);
    String data = new String(b);
    repository.add(data);
    
    project1 = getSimalTestProject();
    assertNotNull("We don't seem to have added the test data as expected", project1);
    logger.debug("Starting testAdd(data)");
  }

}

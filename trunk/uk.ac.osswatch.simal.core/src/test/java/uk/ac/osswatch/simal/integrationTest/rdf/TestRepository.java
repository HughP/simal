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

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    assertEquals(4, projects.size());

    Iterator<IProject> itrProjects = projects.iterator();
    IProject project;
    while (itrProjects.hasNext()) {
      project = itrProjects.next();
      assertNotNull(project.getName());
    }
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
      logger.debug("Got person: " + person + " : " + person.getLabel());
    }

    assertEquals(15, people.size());
    
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

}

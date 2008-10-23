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
package uk.ac.osswatch.simal.integrationTest.model.jena;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IInternetAddress;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.ModelSupport;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;

public class TestPerson extends BaseRepositoryTest {
  private static final Logger logger = LoggerFactory
      .getLogger(TestPerson.class);

  private static IPerson developer;
  private static IPerson documentor;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    initRepository();

    developer = getRepository().getPerson(testDeveloperURI);
    assertNotNull(developer);
    documentor = getRepository().getPerson(testDocumentorURI);
    assertNotNull(documentor);
  }
  
  @Test
  public void testDeveloperInitialised() {
    assertEquals("Developer name is incorrect", "developer", developer.getLabel());
  }

  @Test
  public void testAddPersonFromScratch() throws SimalRepositoryException,
      DuplicateURIException, URISyntaxException {
    String uri = RDFUtils.PERSON_NAMESPACE_URI + "TestingPersonFromScratch";
    IPerson person;
    person = getRepository().createPerson(uri);

    person = getRepository().getPerson(uri);
    assertNotNull("Person has not been added to repository", person);
    assertNotNull("Person simalID should not be null", person.getSimalID());
    assertNotNull("Person simalID is invalid", getRepository().isUniqueSimalID(
        person.getSimalID()));

    person.delete();
  }

  @Test
  public void testNames() {
    assertEquals("developer", developer.getNames().toArray()[0].toString());
  }

  @Test
  public void testHomePage() {
    assertTrue("developer home page is missing", developer.getHomepages()
        .toString().contains("http://example.org/person/developer"));
  }

  @Test
  public void testEmail() {
    Set<IInternetAddress> emails = developer.getEmail();
    assertTrue("Emails are incorrect", emails.toString().contains(
        "mailto:developer@foo.org"));
  }

  @Test
  public void testToJSON() {
    String json = developer.toJSON(false);
    assertTrue("JSON does not include person name: JSON = " + json, json
        .contains("\"label\":\"" + developer.getLabel() + "\""));
  }

  @Test
  public void testKnows() throws SimalRepositoryException {
    Set<IPerson> knows = developer.getKnows();
    assertNotNull("Should know some people", knows);
    IPerson person = knows.iterator().next();
    String name = (String) person.getNames().toArray()[0];
    assertTrue("Should know a person", name.contains("Known Person"));
  }

  @Test
  public void testGetColleagues() throws SimalRepositoryException {
    Set<IPerson> colleagues = developer.getColleagues();
    assertNotNull(colleagues);
    Iterator<IPerson> people = colleagues.iterator();
    while (people.hasNext()) {
      IPerson person = people.next();
      logger.debug("Got colleague: " + person);
      assertNotNull("No person should have a null ID (see " + person.getURI()
          + ")", person.getSimalID());
    }
    assertEquals("Got an incorrect number of colleagues", BaseRepositoryTest
        .getNumberOfParticipants() - 1, colleagues.size());
  }

  @Test
  public void testGetURI() throws SimalRepositoryException {
    assertEquals("Person URI is incorrect", testDeveloperURI, developer
        .getURI());
  }

  @Test
  public void testSuppliedSimalId() throws SimalRepositoryException {
    String id = developer.getSimalID();
    assertNotNull("Test developer ID incorrect", id);

  }

  @Test
  public void testGeneratedSimalId() throws SimalRepositoryException {
    String id = developer.getColleagues().iterator().next().getSimalID();
    assertNotNull("Test developer ID null", id);
  }

  @Test
  public void testSeeAlso() {
    Set<URI> seeAlso = developer.getSeeAlso();
    assertEquals("Incorrect number of see also values", 2, seeAlso.size());
  }

  @Test
  public void testGetProjects() throws SimalRepositoryException {
    Set<IProject> projects = developer.getProjects();
    assertEquals("Developer is not in the correct number of projects", 1,
        projects.size());
  }

  @Test
  public void testEmptyResources() throws SimalRepositoryException {
    Set<IDoapHomepage> homepages = documentor.getHomepages();
    assertEquals(
        "Documentor homepage should not contain any resources as it has an empty resource attribute",
        0, homepages.size());
  }

  @Test
  public void testAddName() { 
    String name = "Unit Test Name";
    developer.addName(name);
    assertEquals("We haven't set the name succesfully", name, developer
        .getNames().toArray()[0]);
  }

  @Test
  public void testGetSimalID() throws SimalRepositoryException {
    String id = developer.getSimalID();
    assertEquals("Simal ID of person is incorrect", testDeveloperID, id);
  }

}

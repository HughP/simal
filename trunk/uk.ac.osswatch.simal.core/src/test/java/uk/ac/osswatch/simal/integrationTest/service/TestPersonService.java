package uk.ac.osswatch.simal.integrationTest.service;
/*
 * Copyright 2007 University of Oxford 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.integrationTest.model.repository.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;
import uk.ac.osswatch.simal.service.IPersonService;

public class TestPersonService extends BaseRepositoryTest {

    private static final Logger logger = LoggerFactory
	      .getLogger(TestPersonService.class);
    
    private static IPersonService service;
    
    @BeforeClass
    public static void getService() throws SimalRepositoryException {
    	service = SimalRepositoryFactory.getPersonService();
    	logger.debug("Created PersonService: " + service);
    }
    
    @Test
    public void testGetAll() throws SimalRepositoryException {
    	Set<IPerson> people = service.getAll();
        
        Iterator<IPerson> itrPeople = people.iterator();
        IPerson person;
        while (itrPeople.hasNext()) {
          person = itrPeople.next();
          assertNotNull(person.getLabel());
          logger.debug("Got person: " + person + " : " + person.getURI());
        }

        assertEquals(22, people.size());
    }

    @Test
    public void testFilterPeopleByName() throws SimalRepositoryException {
      // Test exact Match
      Set<IPerson> people = service.filterByName("Ross Gardler");
      assertEquals("Not the right number of projects with the name 'Ross Gardler'", 1, people.size());
      
    }

    @Test
    public void testFilterPersonByNameByWildcard() {
        Set<IPerson> people = service.filterByName("R*s");
        Iterator<IPerson> itr = people.iterator();
        while (itr.hasNext()) {
        	logger.info("R*s matches: " + itr.next().toString());
        }
        assertEquals("Not the right number of projects match the filter 'R*s'", 11, people.size());
        
        people = service.filterByName("^Ro");
        itr = people.iterator();
        while (itr.hasNext()) {
        	logger.info("^Ro matches: " + itr.next().toString());
        }
        assertEquals("Not the right number of projects match the filter '^Ro'", 2, people.size());

        people = service.filterByName("A$");
        itr = people.iterator();
        while (itr.hasNext()) {
        	logger.info("A$ matches: " + itr.next().toString());
        }
        assertEquals("Not the right number of projects match the filter 'A$'", 1, people.size());
    }
    
    @Test
    public void testGetAllPeopleAsJSON() throws SimalRepositoryException {
      logger.debug("Starting testGetAllPeopleAsJSON()");
      Long targetTime = Long.valueOf(75);
      Long startTime = System.currentTimeMillis();
      String json = service.getAllAsJSON();
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
    public void testFindPersonById() throws SimalRepositoryException {
      IPerson person = service.findById(testDeveloperID);
      assertNotNull("Can't find a person with the ID " + testDeveloperID, person);
      assertEquals("Developer URI is not as expected ", RDFUtils
          .getDefaultPersonURI(testDeveloperID), person.getURI());
    }

    @Test
    public void testFindPersonByEMail() throws SimalRepositoryException, NoSuchAlgorithmException {
      IPerson person = service.findBySha1Sum(RDFUtils.getSHA1(testDeveloperEMail));
      assertNotNull("Can't find a person with the EMail " + testDeveloperEMail, person);
    }

    @Test
    public void testGetPerson() throws SimalRepositoryException {
      IPerson person = service.get(testDeveloperURI);
      assertNotNull("Can't find a person with the URI " + testDeveloperURI,
          person);
      assertEquals("Developer URI is not as expected ", RDFUtils
          .getDefaultPersonURI(testDeveloperID), person.getURI());
    }
    
    @Test
    public void testGetByUsername() {
      IPerson person = service.findByUsername("nobodybythisname");
      assertNull("Shouldn't have a person with the username nobodybythisname", person);
      
      person = service.findByUsername("author");
      assertNotNull("Should have a person with the username 'author'", person);
    }
}

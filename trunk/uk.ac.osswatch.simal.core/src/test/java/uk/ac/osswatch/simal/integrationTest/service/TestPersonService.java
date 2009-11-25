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
import static org.junit.Assert.assertEquals;

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
}

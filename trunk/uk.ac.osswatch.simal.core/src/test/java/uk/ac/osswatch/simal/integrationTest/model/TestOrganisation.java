package uk.ac.osswatch.simal.integrationTest.model;
/*
 * 
 Copyright 2007 University of Oxford * 
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uk.ac.osswatch.simal.integrationTest.model.repository.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IOrganisation;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestOrganisation extends BaseRepositoryTest {

	  @Test
	  public void testGetName() throws SimalRepositoryException {
		  IOrganisation org = getRepository().getOrganisation("http://www.test.com/Organization");
		  assertNotNull(org);
		  assertEquals("Name is not as expected", TEST_SIMAL_ORGANISATION_NAME, org.getName());
	  }
	  
	  @Test
	  public void testAddName() throws SimalRepositoryException {
		  IOrganisation org = getRepository().getOrganisation("http://www.test.com/Organization");
		  String name = "Test Name";
		  org.addName(name);
		  assertEquals("Name is not as expected after adding new name", name, org.getName());
	  }
	
	  @Test 
	  public void testAddCurrentProject() throws SimalRepositoryException {
		  IOrganisation org = getRepository().getOrganisation("http://www.test.com/Organization");
		  int countBefore = org.getCurrentProjects().size();
		  org.addCurrentProject("http://test.com/project#1");
		  int countAfter =  org.getCurrentProjects().size();;
		  assertTrue("We don't seem to have added the current project", countBefore < countAfter);
	  }
}

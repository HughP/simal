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
package uk.ac.osswatch.simal.integrationTest.model;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.integrationTest.model.repository.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestCategory extends BaseRepositoryTest {
  private static final Logger logger = LoggerFactory
      .getLogger(TestCategory.class);
  
  @Test
  public void testGetCategoryLabel() throws SimalRepositoryException,
      URISyntaxException {
    IDoapCategory category = SimalRepositoryFactory.getCategoryService().get(
        "http://simal.oss-watch.ac.uk/category/socialNews");
    String label = category.getLabel();
    assertEquals("Category Label is incorrect", "Social News", label);

    String uri = "http://example.org/does/not/exist";
    category = SimalRepositoryFactory.getCategoryService().get(uri);
    assertNull("Somehow we have a category that should not exist", category);
  }

  @Test
  public void testGetProjects() {
    IDoapCategory category = (IDoapCategory) project1.getCategories().toArray()[0];
    Set<IProject> projects = category.getProjects();

    String strProject = project1.toString();
    assertTrue(
        "We haven't got the project we expected from category.getProjects()",
        projects.toString().contains(strProject));
  }

  @Test
  public void testSetId() throws SimalRepositoryException {
    IDoapCategory category = (IDoapCategory) project1.getCategories().toArray()[0];
    category.setSimalID("testing");
    String id = category.getSimalID();
    assertEquals("Simal ID has not been changed correctly", id, "testing");
  }

  @Test
  public void testGetId() throws SimalRepositoryException {
    IDoapCategory category = (IDoapCategory) project1.getCategories().toArray()[0];
    String id = category.getSimalID();
    assertNotNull(id);
  }

  @Test
  public void testGetPeople() throws SimalRepositoryException {
    IDoapCategory category = (IDoapCategory) project1.getCategories().toArray()[0];
    Set<IPerson> people = category.getPeople();
    Iterator<IPerson>itr = people.iterator();
    while (itr.hasNext()) {
    	logger.debug("Got person: " + itr.next().toString());
    }
    assertEquals("Not got the right number of people for the test category", 9, people.size());
  }
}

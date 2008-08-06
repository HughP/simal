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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;
import java.util.Set;

import org.junit.Test;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestCategory extends BaseRepositoryTest {
  @Test
  public void testGetCategoryLabel() throws SimalRepositoryException, URISyntaxException {
    IDoapCategory category = repository.getCategory("http://simal.oss-watch.ac.uk/category/socialNews");
    String label = category.getLabel();
    assertEquals("Category Label is incorrect", "Social News", label);

    String uri = "http://example.org/does/not/exist";
    category = repository.getCategory(uri);
    assertNull("Somehow we have a category that should not exist",
        category);
  }
  
  @Test
  public void testGetProjects() {
    IDoapCategory category = (IDoapCategory) project1.getCategories().toArray()[0];
    Set<IProject> projects = category.getProjects();
    
    assertTrue("We haven't got the project we expected from category.getPRojects()",
        projects.toString().contains(project1.toString()));
  }
  
  @Test
  public void testSetId() {
    IDoapCategory category = (IDoapCategory) project1.getCategories().toArray()[0];
    category.setSimalID("testing");
    String id = category.getSimalID();
    assertEquals("Simal ID has not been changed correctly", id, "testing");
  }
  
  @Test
  public void testGetId() {
    IDoapCategory category = (IDoapCategory) project1.getCategories().toArray()[0];
    String id = category.getSimalID();
    assertNotNull(id);
  }
  
  @Test
  public void testGetPeople() throws SimalRepositoryException {
    IDoapCategory category = (IDoapCategory) project1.getCategories().toArray()[0];
    Set<IPerson> people = category.getPeople();
    assertTrue("Not got engouh people for the test category", people.size() >= 10);
    
  }
}

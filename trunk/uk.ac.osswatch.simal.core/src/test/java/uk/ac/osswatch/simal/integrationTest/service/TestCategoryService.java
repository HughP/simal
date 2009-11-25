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
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.integrationTest.model.repository.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestCategoryService extends BaseRepositoryTest {

    private static final Logger logger = LoggerFactory
	      .getLogger(TestCategoryService.class);

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
	  public void testFindCategoryById() throws SimalRepositoryException {
	    IDoapCategory cat = SimalRepositoryFactory.getCategoryService().findById("1");
	    assertNotNull(cat);
	    assertEquals("Category name is incorrect", "Simal ID Test", cat.getName());
	  }
	  
	  @Test
	  public void testGetOrCreateCategory() throws SimalRepositoryException {
		 boolean exists = getRepository().containsResource(TEST_SIMAL_PROJECT_CATEGORY_TWO);
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

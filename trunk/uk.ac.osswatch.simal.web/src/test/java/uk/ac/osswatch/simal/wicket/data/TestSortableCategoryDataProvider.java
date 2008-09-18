package uk.ac.osswatch.simal.wicket.data;

/*
 * Copyright 2008 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");   *
 * you may not use this file except in compliance with the License.  *
 * You may obtain a copy of the License at                           *
 *                                                                   *
 *   http://www.apache.org/licenses/LICENSE-2.0                      *
 *                                                                   *
 * Unless required by applicable law or agreed to in writing,        *
 * software distributed under the License is distributed on an       *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY            *
 * KIND, either express or implied.  See the License for the         *
 * specific language governing permissions and limitations           *
 * under the License.                                                *
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IDoapResource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.TransactionException;
import uk.ac.osswatch.simal.wicket.TestBase;

public class TestSortableCategoryDataProvider extends TestBase {
  private static final Logger logger = LoggerFactory
      .getLogger(TestSortableCategoryDataProvider.class);

  @Test
  public void testSize() throws SimalRepositoryException, TransactionException {
    SortableCategoryDataProvider provider = new SortableCategoryDataProvider();
    assertEquals(NUMBER_OF_TEST_CATEGORIES, provider.size());
  }

  @Test
  public void testModel() throws SimalRepositoryException {
    SortableCategoryDataProvider provider = new SortableCategoryDataProvider();
    assertTrue("The default sortable category data provider has no projects",
        provider.size() > 0);
  }

  @Test
  public void testSortByName() throws SimalRepositoryException {
    SortableCategoryDataProvider provider = new SortableCategoryDataProvider();
    int pageSize = NUMBER_OF_TEST_CATEGORIES - 1;
    Iterator<IDoapResource> iterator = provider.iterator(0, pageSize);
    IDoapResource category;
    String prev = null;
    String current;
    int count = 0;
    logger.debug("Category order is:");
    while (iterator.hasNext()) {
      category = iterator.next();
      current = (String) category.getName();
      logger.debug(current);
      if (prev != null) {
        assertTrue("Incorrect sort order: " + prev + " preceeds " + current,
            current.compareTo(prev) >= 0);
      }
      prev = current;
      count = count + 1;
    }

    assertEquals(
        "not returning the right number of elements for the given start point and pageSize",
        pageSize, count);
  }

  @Test
  public void testSortByProjects() throws SimalRepositoryException {
    SortableCategoryDataProvider provider = new SortableCategoryDataProvider();
    int pageSize = NUMBER_OF_TEST_CATEGORIES - 1;

    int count = 0;
    provider.setSort(SortableCategoryDataProvider.SORT_PROPERTY_PROJECTS, true);
    Iterator<IDoapResource> iterator = provider.iterator(0, pageSize);
    int prev = -1;
    count = 0;
    while (iterator.hasNext()) {
      IDoapCategory category = (IDoapCategory) iterator.next();
      int current = category.getProjects().size();
      if (prev >= 0) {
        assertTrue("Incorrect sort order: " + prev + " preceeds " + current,
            current >= prev);
      }
      prev = current;
      count = count + 1;
    }
  }

  @Test
  public void testSortByPeople() throws SimalRepositoryException {
    SortableCategoryDataProvider provider = new SortableCategoryDataProvider();
    int pageSize = NUMBER_OF_TEST_CATEGORIES - 1;

    int count = 0;
    provider.setSort(SortableCategoryDataProvider.SORT_PROPERTY_PEOPLE, true);
    Iterator<IDoapResource> iterator = provider.iterator(0, pageSize);
    int prev = 0;
    count = 0;
    while (iterator.hasNext()) {
      IDoapCategory category = (IDoapCategory) iterator.next();
      int current = category.getPeople().size();
      if (prev >= 0) {
        assertTrue("Incorrect sort order: " + prev + " preceeds " + current,
            current >= prev);
      }
      prev = current;
      count = count + 1;
    }
  }

  @Test
  public void testUnkownSortParamater() throws SimalRepositoryException {
    SortableCategoryDataProvider provider = new SortableCategoryDataProvider();
    boolean threwRuntime = false;
    try {
      provider.setSort("Unknown property", true);
    } catch (RuntimeException e) {
      threwRuntime = true;
    }
    assertTrue("Didn't throw a RuntimeException with an illegal sort property",
        threwRuntime);

  }
}

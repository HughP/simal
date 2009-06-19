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

import org.junit.Test;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.TransactionException;
import uk.ac.osswatch.simal.wicket.TestBase;

public class TestSortableReviewDataProvider extends TestBase {

  @Test
  public void testSize() throws SimalRepositoryException, TransactionException {
    SortableReviewDataProvider provider = new SortableReviewDataProvider();
    assertEquals(1, provider.size());
  }

  @Test
  public void testModel() throws SimalRepositoryException {
    SortableCategoryDataProvider provider = new SortableCategoryDataProvider();
    assertTrue("The default sortable review data provider has no reviews",
        provider.size() > 0);
  }
  
}

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
package uk.ac.osswatch.simal.integrationTest.model.elmo;

import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestHomepage extends BaseRepositoryTest {

  @Test
  public void TestURL() throws SimalRepositoryException {
    Iterator<IDoapHomepage> homepages = project1.getHomepages().iterator();
    boolean hasHomepageOne = false;
    boolean hasHomepageTwo = false;

    while (homepages.hasNext()) {
      IDoapHomepage homepage = homepages.next();
      if (homepage.getURI().toString().equals(TEST_SIMAL_PROJECT_HOMEPAGE_URL_ONE)) {
        hasHomepageOne = true;
      } else if (homepage.getURI().toString().equals(TEST_SIMAL_PROJECT_HOMEPAGE_URL_TWO)) {
        hasHomepageTwo = true;
      }
    }

    assertTrue("Homepage ONE is missing", hasHomepageOne);
    assertTrue("Homepage TWO is missing", hasHomepageTwo);
  }
  

  @Test
  public void testNames() {
    boolean hasHomepageOne = false;
    boolean hasHomepageTwo = false;
    Iterator<IDoapHomepage> homepages = project1.getHomepages().iterator();
    String label;
    while (homepages.hasNext()) {
      IDoapHomepage homepage = (IDoapHomepage) homepages
          .next();
      label = homepage.getLabel();
      if (label.equals(TEST_SIMAL_PROJECT_HOMEPAGE_NAME_ONE)) {
        hasHomepageOne = true;
      } else if (label.equals(TEST_SIMAL_PROJECT_HOMEPAGE_NAME_TWO)) {
        hasHomepageTwo = true;
      }
    }
    assertTrue("Homepages do not include " + TEST_SIMAL_PROJECT_HOMEPAGE_NAME_ONE,
        hasHomepageOne);
    assertTrue("Homepages do not include " + TEST_SIMAL_PROJECT_HOMEPAGE_NAME_TWO,
        hasHomepageTwo);
  }

}

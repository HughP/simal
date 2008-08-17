package uk.ac.osswatch.simal.wicket.foaf;
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

import org.apache.wicket.Page;
import org.apache.wicket.util.tester.ITestPageSource;
import org.junit.Test;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.TestBase;
import uk.ac.osswatch.simal.wicket.UserApplication;

public class TestPersonDetailsPage extends TestBase{

  @Test
  public void testPageRender() {
    tester.startPage(new ITestPageSource() {
      public Page getTestPage() {
        try {
          return new PersonDetailPage(UserApplication.getRepository()
              .getPerson(TEST_SIMAL_DEVELOPER_URI));
        } catch (SimalRepositoryException e) {
          System.err.println("Can't find the test project");
          System.exit(1);
          return null;
        }
      }
    });
    tester.assertRenderedPage(PersonDetailPage.class);
    tester.assertVisible("summary");
  }
}

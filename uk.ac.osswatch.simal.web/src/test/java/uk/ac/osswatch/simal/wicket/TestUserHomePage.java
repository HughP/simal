package uk.ac.osswatch.simal.wicket;

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

import org.apache.wicket.util.tester.FormTester;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * Simple test using the WicketTester
 */
public class TestUserHomePage extends TestBase {

  /**
   * Delete the repository to ensure that subsequent tests have clean data.
   * 
   * @throws SimalRepositoryException
   */
  @AfterClass
  public static void deleteRepostiroy() throws SimalRepositoryException {
    UserApplication.destroyRepository();
  }

  @Before
  public void initTester() throws SimalRepositoryException {
    logProjectData("before");
    tester = Tester.get();
    tester.startPage(UserHomePage.class);
    tester.assertRenderedPage(UserHomePage.class);
  }

  @Test
  public void testRenderPage() {
    tester.assertVisible("projectList");
    tester.assertVisible("featuredProject");
    tester.assertVisible("footer");
  }
  
  @Test
  public void testFilterProjects() {
    tester.assertVisible("projectList:projectFilterForm");
    
    FormTester formTester = tester.newFormTester("projectList:projectFilterForm");
    formTester.setValue("nameFilter", "Simal");
    formTester.submit();
  }
}

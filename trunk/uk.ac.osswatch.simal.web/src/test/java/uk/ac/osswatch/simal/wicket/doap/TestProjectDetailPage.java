package uk.ac.osswatch.simal.wicket.doap;

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
import java.util.Set;

import org.apache.wicket.Page;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.ITestPageSource;
import org.junit.Before;
import org.junit.Test;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.TestBase;
import uk.ac.osswatch.simal.wicket.UserApplication;

/**
 * Simple test using the WicketTester
 */
public class TestProjectDetailPage extends TestBase {

  @SuppressWarnings("serial")
  @Before
  public void initTester() throws SimalRepositoryException {
    tester.startPage(new ITestPageSource() {
      public Page getTestPage() {
        try {
          return new ProjectDetailPage(UserApplication.getRepository()
              .getProject(UserApplication.DEFAULT_PROJECT_URI));
        } catch (SimalRepositoryException e) {
          System.err.println("Can't find the test project");
          System.exit(1);
          return null;
        }
      }
    });
    tester.assertRenderedPage(ProjectDetailPage.class);
  }
  
  @Test
  public void testRenderPage() {
    tester.assertVisible("projectName");
    tester.assertVisible("shortDesc");
    tester.assertVisible("created");
    tester.assertVisible("description");

    tester.assertVisible("mailingLists");

    tester.assertVisible("developers");

    tester.assertVisible("categoryList");

    tester.assertVisible("footer");
  }

  /**
   * Check the add Maintainer form is working OK.
   * @throws SimalRepositoryException 
   */
  @Test
  public void testAddMaintainer() throws SimalRepositoryException {
    Set<IPerson> peopleBefore = UserApplication.getRepository().getProject(UserApplication.DEFAULT_PROJECT_URI).getMaintainers();
    
    tester.assertVisible("addMaintainer");
    tester.assertVisible("addMaintainer:newLink");
    
    tester.assertInvisible("addMaintainer:personForm");
    tester.assertVisible("addMaintainer:newLink");
    tester.clickLink("addMaintainer:newLink");
    
    tester.assertVisible("addMaintainer:personForm");
    tester.assertInvisible("addMaintainer:newLink");
    
    FormTester formTester = tester.newFormTester("addMaintainer:personForm");
    tester.clickLink("addMaintainer:personForm:cancelLink");
    tester.assertInvisible("addMaintainer:personForm");
    
    /**
     * Commented out as the submit does not seem to work with an Ajax form
     * 
    tester.clickLink("addMaintainer:newLink");
    formTester = tester.newFormTester("addMaintainer:personForm");
    formTester.setValue("name", "New Person");
    formTester.submit();
    tester.assertInvisible("addMaintainer:personForm");
    
    Set<IPerson> peopleAfter = UserApplication.getRepository().getProject(UserApplication.DEFAULT_PROJECT_URI).getMaintainers();
    assertEquals("We should have one more person after sumbitting the form", peopleBefore.size() + 1, peopleAfter.size());
    Iterator<IPerson> itr = peopleAfter.iterator();
    boolean hasNewPerson = false;
    while(itr.hasNext() && !hasNewPerson ) {
      IPerson person = itr.next();
      hasNewPerson = person.getNames().toString().contains("New Person");
    }
    assertTrue("We haven't succesfully added the person to the repository", hasNewPerson);
    */
    
    // FIXME: ensure that the new person has been displayed in the list
    
  }

  /**
   * Check maintainers are rendered as expected
   */
  @Test
  public void testMaintainers() {
    tester.assertVisible("maintainers");
    tester.assertVisible("maintainers:1:maintainer");
    tester.assertVisible("maintainers:2:maintainer");
  }
}

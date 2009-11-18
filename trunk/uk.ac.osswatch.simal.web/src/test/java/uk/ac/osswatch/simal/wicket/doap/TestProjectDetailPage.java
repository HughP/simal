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

import java.util.Set;

import org.apache.wicket.Page;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.ITestPageSource;
import org.junit.Before;
import org.junit.Test;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.TestBase;

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
          return new ProjectDetailPage(SimalRepositoryFactory.getProjectService().getProject(projectURI));
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
  
  @Test
  public void testRDFLink() {
	  tester.assertVisible("doapLink");
	  // TODO: we need to test "clicking" this link
  }
  
  @Test
  public void testReviewList() {
	  tester.assertVisible("reviews");
	  tester.assertVisible("reviews:dataTable:rows:1");
	  tester.assertLabel("reviews:dataTable:rows:1:cells:1:cell", "Simal DOAP Test");
	  tester.assertLabel("reviews:dataTable:rows:1:cells:3:cell", "22/05/09");
  }
  
  @Test
  public void testShowHideReviewForm() {
	    tester.assertVisible("addReviewPanel");
	    tester.assertVisible("addReviewPanel:newLink");

	    tester.assertInvisible("addReviewPanel:reviewForm");
	    tester.clickLink("addReviewPanel:newLink");

	    tester.assertVisible("addReviewPanel:reviewForm");
	    tester.assertInvisible("addReviewPanel:newLink");

	    tester.clickLink("addReviewPanel:reviewForm:cancelLink");
	    tester.assertInvisible("addReviewPanel:reviewForm");
  }

  @Test
  public void testAddReview() {
	    tester.clickLink("addReviewPanel:newLink");
	    FormTester formTester = tester.newFormTester("addReviewPanel:reviewForm");
	    
	    formTester.submit();
  }
  
  /**
   * Check the add Maintainer form is working OK.
   * 
   * @throws SimalRepositoryException
   */
  @Test
  public void testAddMaintainer() throws SimalRepositoryException {
    Set<IPerson> peopleBefore = SimalRepositoryFactory.getProjectService().getProject(
        projectURI).getMaintainers();

    tester.assertVisible("addMaintainerPanel");
    tester.assertVisible("addMaintainerPanel:newLink");

    tester.assertInvisible("addMaintainerPanel:personForm");
    tester.assertVisible("addMaintainerPanel:newLink");
    tester.clickLink("addMaintainerPanel:newLink");

    tester.assertVisible("addMaintainerPanel:personForm");
    tester.assertInvisible("addMaintainerPanel:newLink");

    tester.clickLink("addMaintainerPanel:personForm:cancelLink");
    tester.assertInvisible("addMaintainerPanel:personForm");

    /**
     * Commented out as the submit does not seem to work with an Ajax form
     * 
     * FormTester formTester = tester
     *   .newFormTester("addMaintainerPanel:personForm");
     * 
     * tester.clickLink("addMaintainerPanel:newLink"); formTester =
     * tester.newFormTester("addMaintainerPanel:personForm");
     * formTester.setValue("name", "New Person"); formTester.submit();
     * tester.assertInvisible("addMaintainerPanel:personForm");
     * 
     * Set<IPerson> peopleAfter =
     * UserApplication.getRepository().getProject(projectURI).getMaintainers();
     * assertEquals("We should have one more person after submitting the form",
     * peopleBefore.size() + 1, peopleAfter.size()); Iterator<IPerson> itr =
     * peopleAfter.iterator(); boolean hasNewPerson = false; while(itr.hasNext() &&
     * !hasNewPerson ) { IPerson person = itr.next(); hasNewPerson =
     * person.getNames().toString().contains("New Person"); } assertTrue("We
     * haven't successfully added the person to the repository", hasNewPerson);
     */

    // FIXME: ensure that the new person has been displayed in the list
  }

  /**
   * Check maintainers are rendered as expected
   */
  @Test
  public void testMaintainers() {
    tester.assertVisible("maintainers");
  }

  @Test
  public void testOpennessRating() {
    tester.assertVisible("opennessRating");
    tester.assertLabel("opennessRating", "100%");
  }
}

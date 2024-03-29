package uk.ac.osswatch.simal.wicket.panel;

/*
 * Copyright 2008-2010 University of Oxford
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

import static org.junit.Assert.fail;

import java.util.Set;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Before;
import org.junit.Test;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IDocument;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.TestBase;
import uk.ac.osswatch.simal.wicket.panel.project.DocumentSetPanel;

/**
 * Simple test using the WicketTester
 */
public class TestDocumentSetPanel extends TestBase {

  private DocumentSetPanel testPanel;
  
  @Before
  public void setUpPanel() throws SimalRepositoryException {
    setUpPanel(false);
  }
  
  @SuppressWarnings("serial")
  public void setUpPanel(boolean isLoggedIn) throws SimalRepositoryException {
    if (isLoggedIn) {
      tester.login();
    } else {
      tester.logout();
    }
    
    Panel panel = tester.startPanel(new TestPanelSource() {
      public Panel getTestPanel(String panelId) {
        try {
          IProject project = SimalRepositoryFactory.getProjectService().findProjectBySeeAlso(TEST_PROJECT_SEEALSO);
          Set<IDocument> homepages = project.getHomepages();
          return new DocumentSetPanel(panelId, "Homepage List", homepages, project) {

            public void addToModel(IDocument document) throws SimalException {
              getProject().addHomepage(document);
            }

            public void removeFromModel(IDocument document)
                throws SimalRepositoryException {
              getProject().removeHomepage(document);
            }
          };
        } catch (SimalRepositoryException e) {
          fail();
          return null;
        }
      }
    });

    if (panel instanceof DocumentSetPanel) {
      this.testPanel = (DocumentSetPanel) panel;
    } else {
      fail("Panel returned is of wrong type.");
    }
  }

  @Test
  public void testHomepageListPanel() {
    tester.assertVisible("panel:dataTable:rows:1");
    tester.assertVisible("panel:dataTable:rows:2");
    tester.assertLabel("panel:dataTable:rows:1:cells:1:cell:link:label",
        "Developer Home Page");
  }
  
  @Test
  public void testAddHomepage() throws SimalRepositoryException {
    tester.assertInvisible("panel:addDocumentPanel:newLink");
    tester.assertInvisible("panel:addDocumentPanel");
    setUpPanel(true);
    tester.assertVisible("panel:addDocumentPanel");
    tester.assertVisible("panel:addDocumentPanel:newLink");
    tester.assertVisible("panel:dataTable:rows:1");
    tester.assertVisible("panel:dataTable:rows:2");
    tester.clickLink("panel:addDocumentPanel:newLink", true);
    // Rows repopulated with new ids
    tester.assertVisible("panel:dataTable:rows:3");
    tester.assertVisible("panel:dataTable:rows:4");

    try {
      tester.assertVisible("panel:dataTable:rows:5");
      fail("Should not have three rows in the homepages table");
    } catch (WicketRuntimeException e) {
      // ignore as we expect this exception
    }
    
    FormTester formTester = tester.newFormTester("panel:addDocumentPanel:doapResourceForm");
    formTester.setValue("name", "Test Webpage");
    formTester.setValue("url", "http://test.foo.org");
    // submit should work but nothing is actually added
    formTester.submit("addDoapResourceButton");
    

    // Rows repopulated with new ids
    tester.assertVisible("panel:dataTable:rows:5");
    tester.assertVisible("panel:dataTable:rows:6");
    
    // Third item added 
    tester.assertVisible("panel:dataTable:rows:7");
    try {
      tester.assertVisible("panel:dataTable:rows:8");
      fail("Should not have more than three rows in the homepages table");
    } catch (WicketRuntimeException e) {
      // ignore as we expect this exception
    }
  }
}

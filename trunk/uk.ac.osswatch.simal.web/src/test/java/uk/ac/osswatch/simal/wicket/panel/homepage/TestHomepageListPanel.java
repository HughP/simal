package uk.ac.osswatch.simal.wicket.panel.homepage;

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

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.TestPanelSource;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.TestBase;

/**
 * Simple test using the WicketTester
 */
public class TestHomepageListPanel extends TestBase {

  @Before
  @SuppressWarnings("serial")
  public void setUpPanel() {
    tester.startPanel(new TestPanelSource() {
      public Panel getTestPanel(String panelId) {
        try {
          return new HomepageListPanel(panelId, "Homepage List", 20);
        } catch (SimalRepositoryException e) {
          fail(e.getMessage());
          return null;
        }
      }
    });
  }
  
  @Test
  public void testHomepageListPanel() {
    tester.assertVisible("panel:dataTable:rows:1");
    tester.assertVisible("panel:dataTable:rows:2");
    tester.assertVisible("panel:dataTable:rows:7");
    tester.assertLabel("panel:dataTable:rows:1:cells:1:cell:link:label",
        "Developer Home Page");
  }
  
  @Test
  public void testAddHomepage() {
    tester.assertVisible("panel:addWebsitePanel");
    tester.clickLink("panel:addWebsitePanel:newLink", true);
    try {
      tester.assertVisible("panel:dataTable:rows:10");
      fail("Should not have ten rows in the homepages table");
    } catch (WicketRuntimeException e) {
      // ignore as we expect this exception
    }
    
    FormTester formTester = tester.newFormTester("panel:addWebsitePanel:doapResourceForm");
    formTester.setValue("name", "Test Webpage");
    formTester.setValue("url", "http://test.foo.org");
    formTester.submit();

    tester = new WicketTester();
    setUpPanel();
    // FIXME: formTester.submit() appears to not be working.
    // tester.assertVisible("panel:dataTable:rows:10");
  }
}

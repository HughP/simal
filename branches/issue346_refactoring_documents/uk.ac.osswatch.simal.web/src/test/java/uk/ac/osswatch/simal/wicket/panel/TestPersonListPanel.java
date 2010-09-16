package uk.ac.osswatch.simal.wicket.panel;

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

import static org.junit.Assert.fail;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Before;
import org.junit.Test;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.TestBase;
import uk.ac.osswatch.simal.wicket.foaf.PersonDetailPage;

/**
 * Simple test using the WicketTester
 */
public class TestPersonListPanel extends TestBase {

  @Before
  @SuppressWarnings("serial")
  public void setUpPanel() {
    tester.startPanel(new TestPanelSource() {
      public Panel getTestPanel(String panelId) {
        try {
          return new PersonListPanel(panelId, "Person List", 7);
        } catch (SimalRepositoryException e) {
          fail(e.getMessage());
          return null;
        }
      }
    });
  }
  
  @Test
  public void testPersonListPanel() {
    tester.assertVisible("panel:dataTable:rows:1");
    tester.assertVisible("panel:dataTable:rows:2");
    tester.assertVisible("panel:dataTable:rows:7");
    tester.assertLabel("panel:dataTable:rows:1:cells:1:cell:link:label",
        "Elena Blanco");

    tester.clickLink("panel:dataTable:rows:1:cells:1:cell:link");
    tester.assertRenderedPage(PersonDetailPage.class);
  }
  
  @Test
  public void testFiltering() {
    tester.assertVisible("panel:personFilterForm");
    tester.assertVisible("panel:dataTable:rows:1");
    
    FormTester formTester = tester.newFormTester("panel:personFilterForm");
    formTester.setValue("nameFilter", "Gardler");
    formTester.submit();

    tester.assertVisible("panel:dataTable");
    tester.assertVisible("panel:dataTable:rows");
    // TODO: need to test if the rows are filtered
  }
}

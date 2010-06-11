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

import static org.junit.Assert.assertTrue;

import org.apache.wicket.markup.html.basic.Label;
import org.junit.Test;

/**
 * Simple test using the WicketTester
 */
public class TestErrorReportPage extends TestBase {

  @Test
  public void testRenderPage() {
    tester.startPage(ErrorReportPage.class);
    tester.assertRenderedPage(ErrorReportPage.class);
    tester.assertVisible("errorDetails");
    tester.assertVisible("footer");

    Label details = (Label) tester
        .getComponentFromLastRenderedPage("errorDetails");
    String strDetails = (String) details.getDefaultModel().getObject();
    assertTrue("Error report does not appear to contain the error message",
        strDetails.contains("Just testing"));
    assertTrue(
        "Error report does not appear to contain the reporting class details",
        strDetails.contains("ErrorReportPage"));
    assertTrue("Error report does not appear to contain the cause report",
        strDetails.contains("IllegalArgumentException"));
    assertTrue("Error report does not appear to contain the stacktrace",
        strDetails.contains("at uk.ac.osswatch.simal.wicket.ErrorReportPage"));
  }
}

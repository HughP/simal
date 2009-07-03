package uk.ac.osswatch.simal.wicket.report;
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
import org.junit.Before;
import org.junit.Test;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.TestBase;

public class TestProjectsSummaryReport extends TestBase {

	  @SuppressWarnings("serial")
	  @Before
	  public void initTester() throws SimalRepositoryException {
	    tester.startPage(new ITestPageSource() {
	      public Page getTestPage() {
	          return new ProjectsSummaryReportPage();
	      }
	    });
	    tester.assertRenderedPage(ProjectsSummaryReportPage.class);
	  }
	  
	  @Test
	  public void testProjectFigures() {
	    tester.assertVisible("numOfProjects");
		tester.assertLabel("numOfProjects", "10");
	  }
	  
	  @Test
	  public void testRepositoryFigures() {
		tester.assertVisible("numOfProjectsWithRCS");
		tester.assertLabel("numOfProjectsWithRCS", "2");

		tester.assertVisible("numOfProjectsWithoutRCS");
		tester.assertLabel("numOfProjectsWithoutRCS", "8");

		tester.assertVisible("percentProjectsWithRCS");
		tester.assertLabel("percentProjectsWithRCS", "20%");
	  }
	  
	  @Test
	  public void testHomepageFigures() {
		tester.assertVisible("numOfProjectsWithHomepage");
		tester.assertLabel("numOfProjectsWithHomepage", "8");

		tester.assertVisible("numOfProjectsWithoutHomepage");
		tester.assertLabel("numOfProjectsWithoutHomepage", "2");

		tester.assertVisible("percentProjectsWithHomepage");
		tester.assertLabel("percentProjectsWithHomepage", "80%");
	  }
	  
	  @Test
	  public void testMailingListFigures() {
		tester.assertVisible("numOfProjectsWithMailingList");
		tester.assertLabel("numOfProjectsWithMailingList", "4");

		tester.assertVisible("numOfProjectsWithoutMailingList");
		tester.assertLabel("numOfProjectsWithoutMailingList", "6");

		tester.assertVisible("percentProjectsWithMailingList");
		tester.assertLabel("percentProjectsWithMailingList", "40%");
	  }
	  
	  @Test
	  public void testMaintainerFigures() {
		tester.assertVisible("numOfProjectsWithMaintainer");
		tester.assertLabel("numOfProjectsWithMaintainer", "7");

		tester.assertVisible("numOfProjectsWithoutMaintainer");
		tester.assertLabel("numOfProjectsWithoutMaintainer", "3");

		tester.assertVisible("percentProjectsWithMaintainer");
		tester.assertLabel("percentProjectsWithMaintainer", "70%");
	  }

	  @Test
	  public void testBugDatabaseFigures() {
		tester.assertVisible("numOfProjectsWithBugDatabase");
		tester.assertLabel("numOfProjectsWithBugDatabase", "4");

		tester.assertVisible("numOfProjectsWithoutBugDatabase");
		tester.assertLabel("numOfProjectsWithoutBugDatabase", "6");

		tester.assertVisible("percentProjectsWithBugDatabase");
		tester.assertLabel("percentProjectsWithBugDatabase", "40%");
	  }
}
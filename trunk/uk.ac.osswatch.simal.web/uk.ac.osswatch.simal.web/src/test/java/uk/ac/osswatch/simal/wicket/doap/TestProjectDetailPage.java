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


import org.junit.Test;

import uk.ac.osswatch.simal.wicket.TestBase;
import uk.ac.osswatch.simal.wicket.doap.ProjectDetailPage;

/**
 * Simple test using the WicketTester
 */
public class TestProjectDetailPage extends TestBase {

	@Test
	public void testRenderPage() {
		tester.startPage(ProjectDetailPage.class);
		tester.assertRenderedPage(ProjectDetailPage.class);
		
		tester.assertVisible("projectName");
		tester.assertVisible("shortDesc");
		tester.assertVisible("created");
		tester.assertVisible("description");
		
		tester.assertVisible("mailingLists");
		
		tester.assertVisible("maintainers");
		tester.assertVisible("maintainers:1:maintainer");
		tester.assertVisible("maintainers:2:maintainer");
		
		tester.assertVisible("developers");
		
		tester.assertVisible("categories");
		tester.assertVisible("categories:1:category");
		tester.assertLabel("categories:1:category:label", "DOAP Test");
		
		tester.assertVisible("footer");
	}
}

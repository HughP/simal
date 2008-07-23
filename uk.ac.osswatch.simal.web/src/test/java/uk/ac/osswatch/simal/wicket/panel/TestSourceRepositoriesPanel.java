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
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Test;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.TestBase;
import uk.ac.osswatch.simal.wicket.UserApplication;

/**
 * Simple test using the WicketTester
 */
public class TestSourceRepositoriesPanel extends TestBase {

	@Test
	@SuppressWarnings("serial")
	public void testRenderPanel() {
		 tester.startPanel(new TestPanelSource() {
		        public Panel getTestPanel(String panelId)
		        {
		                try {
							return new SourceRepositoriesPanel(panelId, UserApplication.getRepository().getProject(UserApplication.DEFAULT_PROJECT_URI).getRepositories());
						} catch (SimalRepositoryException e) {
							fail(e.getMessage());
							return null;
						}
		        }
		 });
		tester.assertVisible("panel:sourceRepositories");
		tester.assertVisible("panel:sourceRepositories:1:anonRoots");
		tester.assertVisible("panel:sourceRepositories:1:devLocations:1:devLink");
		tester.assertVisible("panel:sourceRepositories:1:browseRoots:1:browseLink");
	}
}

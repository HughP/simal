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
public class TestReleasesPanel extends TestBase {

	@Test
	@SuppressWarnings("serial")
	public void testRenderPanel() {
		 tester.startPanel(new TestPanelSource() {
		        public Panel getTestPanel(String panelId)
		        {
		                try {
							return new ReleasesPanel(panelId, UserApplication.getRepository().getProject(projectURI).getReleases());
						} catch (SimalRepositoryException e) {
							fail(e.getMessage());
							return null;
						}
		        }
		 });
		tester.assertVisible("panel:releases");
		tester.assertVisible("panel:releases:1:name");
		// FIXME: we can't predict which release will be first
		//tester.assertLabel("panel:releases:1:name", "Simal Project Registry");
		
		tester.assertVisible("panel:releases:1:revisions");
    // FIXME: we can't predict which release will be first
    // tester.assertLabel("panel:releases:1:revisions:1:revision", "0.1");
		
		tester.assertVisible("panel:releases:1:created");
    // FIXME: we can't predict which release will be first
    //tester.assertLabel("panel:releases:1:created", "2007-08-30");
	}
}

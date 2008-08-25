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

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.TestBase;
import uk.ac.osswatch.simal.wicket.UserApplication;

/**
 * Simple test using the WicketTester
 */
public class TestPersonSummaryPanel extends TestBase {

	@Test
	@SuppressWarnings("serial")
	public void testRenderUserHomePage() {
		 tester.startPanel(new TestPanelSource() {
		        public Panel getTestPanel(String panelId)
		        {
		                try {
							return new PersonSummaryPanel(panelId, (IPerson)UserApplication.getRepository().getProject(UserApplication.DEFAULT_PROJECT_URI).getDevelopers().toArray()[0]);
						} catch (SimalRepositoryException e) {
							fail(e.getMessage());
							return null;
						}
		        }
		 });
		tester.assertVisible("panel:detailLink");		
		tester.assertVisible("panel:homepages");
	}
}

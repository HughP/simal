/*
 * Copyright 2007 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.osswatch.simal.model.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import uk.ac.osswatch.simal.model.Contributor;
import uk.ac.osswatch.simal.model.Event;
import uk.ac.osswatch.simal.model.Project;
import uk.ac.osswatch.simal.service.derby.ManagedProjectBean;

public class TestProject {

	private Project createProject() {
		Contributor contributor = new Contributor("Contributor 1",
				"cont1@test.com");
		Event event = new Event("Event 1", "The first event",
				new java.util.Date());
		Project project = new Project("Test1", "Test 1", "First Test Project",
				contributor);
		project.addEvent(event);
		try {
			project.setHomepageURL(new URL("http://www.test.com"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return project;
	}

	@Test
	public void testCreateProjectFromDOAPURL() throws ParserConfigurationException, SAXException, IOException {
		URL simalURL = null;
		try {
			simalURL = new URL(
					"http://simal.googlecode.com/svn/trunk/simal/src/documentation/content/rdf/simal.xml");
		} catch (MalformedURLException e1) {
			fail("Unable to create DOAP URL");
		}
		Project project = null;
		project = new Project(simalURL);
		assertEquals("Project name is incorrect",
				"Simal Project Catalogue System", project.getName());
	}

}

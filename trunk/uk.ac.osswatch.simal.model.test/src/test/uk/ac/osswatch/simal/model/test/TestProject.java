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

import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.junit.Test;
import org.xml.sax.SAXException;

import uk.ac.osswatch.simal.model.Category;
import uk.ac.osswatch.simal.model.Contributor;
import uk.ac.osswatch.simal.model.Event;
import uk.ac.osswatch.simal.model.MailingList;
import uk.ac.osswatch.simal.model.Project;

public class TestProject {
	
	String doapNS = "http://usefulinc.com/ns/doap#";

	private Project createProject() {
		Contributor contributor = new Contributor("Contributor 1",
				"cont1@test.com");
		Event event = new Event("Event 1", "The first event",
				new java.util.Date());
		Project project = new Project("Test1", "Test 1", "First Test Project",
				contributor);
		project.addEvent(event);
		try {
			project.addCategory(new Category(new URL("http://simal.oss-watch.ac.uk/category/doapTest#")));
			project.addCategory(new Category(new URL("http://simal.oss-watch.ac.uk/category/supplementaryDOAPTest#")));
			project.addMailingList(new MailingList("Mailing List 1", "http://foo.org/mailingList1"));
			project.setHomepageURL(new URL("http://www.test.com"));
		} catch (MalformedURLException e) {
			throw new RuntimeException("Malformed URL in creating a project in the tests - thought to be impossible as the URL is hard coded", e);
		}
		return project;
	}

	@Test
	public void testCreateProjectFromDOAPURL() throws ParserConfigurationException, SAXException, IOException, MarshalException, ValidationException, MappingException {
		URL doapURL = this.getClass().getResource("testDOAP.xml");
		Project project = null;
		project = Project.readProject(doapURL);
		assertEquals("Project name is incorrect",
				"Simal DOAP Test", project.getName());
		assertEquals("Project short name is incorrect",
						"undefined", project.getShortName());
		assertEquals("Homepage URL is incorrect", "http://simal.oss-watch.ac.uk", project.getHomepageURL());
		assertEquals("Categories are not corect, we should have 2", 2, project.getCategories().size());
		assertEquals("Mailing lists are not corect, we should have 2", 2, project.getMailingLists().size());
	}
	
	@Test
	public void testToXML() throws MarshalException, ValidationException, MappingException {
		Project project = createProject();
		String xml = project.toXML();
		System.out.println(xml);
		assertTrue("Project XML does not contain DOAP namespace", xml.contains(doapNS));
		assertTrue("Project XML does not contain shortname element", xml.contains("<doap:shortname"));
		assertTrue("Project XML does not contain the relevant categories", xml.contains("http://simal.oss-watch.ac.uk/category/doapTest#"));
		assertTrue("Project XML does not contain the relevant categories", xml.contains("http://simal.oss-watch.ac.uk/category/supplementaryDOAPTest#"));
		assertTrue("Project XML does not contain the right mailing lists", xml.contains("http://foo.org/mailingList1"));
		assertTrue("Project XML does not contain the right mailing list title", xml.contains("dc:title=\"Mailing List 1\""));
	}

}

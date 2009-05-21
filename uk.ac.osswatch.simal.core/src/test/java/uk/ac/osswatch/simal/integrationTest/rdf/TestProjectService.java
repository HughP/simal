package uk.ac.osswatch.simal.integrationTest.rdf;
/*
 * Copyright 2007 University of Oxford 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */
import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.jena.ProjectService;

public class TestProjectService extends BaseRepositoryTest {

	
	@Test
	public void testGetProjectsWithRCS() {
		ProjectService service = getRepository().getProjectService();
		Set<IProject> projects = service.getProjectsWithRCS();
		assertEquals("Got incorect number of projects with RCS", 2, projects.size());
	}

	@Test
	public void testGetProjectsWithHomepage() {
		ProjectService service = getRepository().getProjectService();
		Set<IProject> projects = service.getProjectsWithHomepage();
		assertEquals("Got incorect number of projects with Homepage", 8, projects.size());
	}

	@Test
	public void testGetProjectsWithMaintainer() {
		ProjectService service = getRepository().getProjectService();
		Set<IProject> projects = service.getProjectsWithMaintainer();
		assertEquals("Got incorect number of projects with Mainteiner", 7, projects.size());
	}

	@Test
	public void testGetProjectsWithMailingList() {
		ProjectService service = getRepository().getProjectService();
		Set<IProject> projects = service.getProjectsWithMailingList();
		assertEquals("Got incorect number of projects with MailingList", 4, projects.size());
	}

	@Test
	public void testGetProjectsWithBugDatabase() {
		ProjectService service = getRepository().getProjectService();
		Set<IProject> projects = service.getProjectsWithBugDatabase();
		assertEquals("Got incorect number of projects with BugDatabaseList", 4, projects.size());
	}
}

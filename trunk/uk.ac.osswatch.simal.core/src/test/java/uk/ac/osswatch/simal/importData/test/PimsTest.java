/*
 * Copyright 2009 University of Oxford
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
package uk.ac.osswatch.simal.importData.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.osswatch.simal.importData.Pims;
import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IInternetAddress;
import uk.ac.osswatch.simal.model.IOrganisation;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.jena.SimalRepository;

public class PimsTest extends BaseRepositoryTest {
	
	private static ISimalRepository repo;

	@BeforeClass
	public static void importTestData() throws FileNotFoundException, SimalRepositoryException, IOException, DuplicateURIException {
		repo = SimalRepository.getInstance();
	}
	
	@AfterClass
	public static void deleteImportedData() throws SimalRepositoryException {
		repo.destroy();
	}
	
	
	@Test
	public void testProjectImport() throws FileNotFoundException, IOException, SimalRepositoryException, DuplicateURIException {
		Iterator<IProject> projects = repo.getAllProjects().iterator();
		IProject project;
		
		boolean projectAIsValid = false;
		while (projects.hasNext()) {
			project = projects.next();
			String name = project.getName();
			Set<IDoapHomepage> homepages = project.getHomepages();
			if (name.equals("Project A")) {
				if (!homepages.toString().contains("Homepage")) {
					break;
				}
				projectAIsValid = true;
			}
		}
		assertTrue("Project A has not been correctly imported", projectAIsValid);
	}
	
	@Test
	public void testInstitutionImport() throws SimalRepositoryException {
		Iterator<IOrganisation> orgs = repo.getAllOrganisations().iterator();
		boolean orgIsValid = false;
		while (orgs.hasNext()) {
			IOrganisation org = orgs.next();
			if (org.getName().equals("Institution A")) {
				Set<IProject> currentProjects = org.getCurrentProjects();
				assertEquals("Don't have the right number of current projects for Institution A", 2, currentProjects.size());
				orgIsValid = true;
				break;
			}
		}
		assertTrue("Intitution A is not been properly imported", orgIsValid);
	}
	
	@Test
	public void testImportProgrammes() throws SimalRepositoryException {
		Iterator<IDoapCategory> cats = repo.getAllCategories().iterator();
		boolean catIsValid = false;
		while (cats.hasNext()) {
			IDoapCategory cat = cats.next();
			String name = cat.getName();
			if (name.equals("Programme A")) {
				Set<IProject> projects = cat.getProjects();
				
				IProject proj = repo.getProject(Pims.PIMS_PROJECT_URI);
				Set<IDoapCategory> pimsCats = proj.getCategories();
				
				assertEquals("Don't have the right number of current projects for Programme A",3, projects.size());
				catIsValid = true;
				break;
			}
		}
		assertTrue("Programme A is not been properly imported", catIsValid);	
	}
	
	@Test
	public void testImportProjectContacts() throws SimalRepositoryException {
		Iterator<IPerson> people = repo.getAllPeople().iterator();
		boolean personIsValid = false;
		while (people.hasNext()) {
			IPerson person = people.next();
			Set<String> name = person.getNames();
			if (name.contains("Person A")) {
				personIsValid = true;
				
				Set<IProject> projects = person.getProjects();
				assertEquals("Don't have the right number of projects for Person A",2, projects.size());
				
				Set<IInternetAddress> emails = person.getEmail();
				assertTrue("Persons A has an invalid email setting", emails.toString().contains("persona@institutiona.ac.uk"));
				
				break;
			}
		}
		assertTrue("Person is not been properly imported", personIsValid);	
	}
	
	@Test
	public void testProjectCategories() throws SimalRepositoryException {
		IProject project = repo.getProject("http://jisc.ac.uk/project#10");
		Set<IDoapCategory> cats = project.getCategories();
		assertEquals("Project A has an incorrect number of categories", 1, cats.size());
	}
	
	@Test
	public void testProjectHomepages() throws SimalRepositoryException {
		IProject project = repo.getProject("http://jisc.ac.uk/project#10");
		Set<IDoapHomepage> pages = project.getHomepages();
		assertEquals("Project A has an incorrect number of homepages", 1, pages.size());
	}

}

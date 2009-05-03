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
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.osswatch.simal.importData.Pims;
import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IOrganisation;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryFactory;

public class PimsTest extends BaseRepositoryTest {
	
	private static ISimalRepository repo;

	@BeforeClass
	public static void importTestData() throws FileNotFoundException, SimalRepositoryException, IOException, DuplicateURIException {
		repo = SimalRepositoryFactory.getInstance();
		
		URL resource = PimsTest.class.getResource("data/QryProjectsForSimal.xls");
		String filename = resource.getFile();
		Pims.importProjects(filename);
		
		resource = PimsTest.class.getResource("data/QryProjectInstitutionsForSimal.xls");
		filename = resource.getFile();
		Pims.importInstitutions(filename);
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
				project.delete();
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

}

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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.importData.Ohloh;
import uk.ac.osswatch.simal.integrationTest.model.repository.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestOhlohImport extends BaseRepositoryTest {
	  private static final Logger logger = LoggerFactory.getLogger(TestOhlohImport.class);
	
	private static ISimalRepository repo;
	public final static String OHLOH_TEST_DATA = "testData/ohlohTestData.txt";

	@BeforeClass
	public static void importTestData() throws SimalRepositoryException {
		repo = SimalRepositoryFactory.getInstance();
	}
	
	@AfterClass
	public static void deleteImportedData() throws SimalRepositoryException {
		repo.destroy();
	}
	
	
	@Test
	public void testProjectImport() throws FileNotFoundException, IOException, SimalRepositoryException, DuplicateURIException, URISyntaxException {
		File file = new File(ISimalRepository.class.getClassLoader().getResource(OHLOH_TEST_DATA).toURI());
		Ohloh ohloh = new Ohloh();
		ohloh.importProjects(file);
		
		Set<IProject> allProjects = repo.getAllProjects();
		int numProjectsbefore = allProjects.size();
		Iterator<IProject> projects = allProjects.iterator();
		IProject project;
		
		boolean projectAIsValid = false;
		while (projects.hasNext()) {
			project = projects.next();
			String name = project.getName();
			//Set<IDoapHomepage> homepages = project.getHomepages();
			if (name.equals("Apache HTTP Server")) {
				projectAIsValid = true;
			}
		}
		if (allProjects.size() <= numProjectsbefore) {
			// @refactor if we have no projects and no exception was thrown it means that there has been
			// a problem importing projects. This is probably because no PAI key has been provided.
			// At present we swallow this and assume.
			logger.error("Not retrieved any projects from Ohloh, please see log for more information (probably a missing API key). To add an API key add 'ohloh.api.key=[YOUR API KEY]' to local.simal.properties. You can get a key from http://www.ohloh.net/api/getting_started");
		} else {
			assertTrue("Apache HTTPD project has not been correctly imported", projectAIsValid);
		}
	}
}

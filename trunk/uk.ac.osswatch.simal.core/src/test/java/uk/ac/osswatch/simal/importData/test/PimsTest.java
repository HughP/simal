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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import  uk.ac.osswatch.simal.importData.Pims;
import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryFactory;

public class PimsTest extends BaseRepositoryTest {
	
	@Test
	public void testDumpData() throws FileNotFoundException, IOException, SimalRepositoryException, DuplicateURIException {
		Pims pims = new Pims();
		ISimalRepository repo = SimalRepositoryFactory.getInstance();
		Iterator<IProject> projects = repo.getAllProjects().iterator();
		
		boolean projectAIsValid = false;
		while (projects.hasNext()) {
			IProject project = projects.next();
			String name = project.getName();
			Set<IDoapHomepage> homepages = project.getHomepages();
			if (name.equals("Project A")) {
				if (!homepages.toString().contains("Homepage")) {
					break;
				}
				projectAIsValid = true;
			}
		}
		assertTrue("Project A is not correctly configured", projectAIsValid);
	}

}

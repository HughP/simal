package uk.ac.osswatch.simal.integrationTest.model.repository.jcr;
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IOrganisation;
import uk.ac.osswatch.simal.model.ModelSupport;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.IOrganisationService;

public class JcrRepositoryTest extends JcrRepositoryBaseTests {
	@Test
	public void containsResource() throws SimalRepositoryException {
		assertTrue("We should have a resource with URI " + detailedProjectURI, repo.containsResource(detailedProjectURI));
		assertFalse("We should not have a resource with URI " + detailedProjectURI + "/noResource", repo.containsResource(detailedProjectURI + "/noResource"));
	}
	
	@Test
	public void addRDFXML() throws SimalRepositoryException, ParserConfigurationException, SAXException, IOException {
		IOrganisationService service = SimalRepositoryFactory.getOrganisationService();
		IOrganisation org = service.get("http://www.test.com/Organization");
		assertNull("Organisation has already been added", org);
		
		
		repo.addRDFXML(ISimalRepository.class.getClassLoader().getResource(
	              ModelSupport.ORGANISATION_RDF), ModelSupport.TEST_FILE_BASE_URL);
		org = service.get("http://www.test.com/Organization");
		assertNotNull("Have not added an organisation using the addRDFXML method", org);
	}
}

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

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.IPersonService;

public class JcrPersonServiceTest extends JcrRepositoryBaseTests {
	@Test
	public void getNewID() throws SimalRepositoryException {
		IPersonService service = getService();
		int firstID = Integer.parseInt(service.getNewID());
		int secondID = Integer.parseInt(service.getNewID());
		assertTrue("Id has not been incremented", firstID + 1 == secondID);
	}
	
	private IPersonService getService() throws SimalRepositoryException {
		IPersonService service = SimalRepositoryFactory.getPersonService();
		return service;
	}
}

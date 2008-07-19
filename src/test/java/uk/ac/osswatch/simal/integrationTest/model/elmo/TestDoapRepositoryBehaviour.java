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
package uk.ac.osswatch.simal.integrationTest.model.elmo;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IDoapRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestDoapRepositoryBehaviour extends BaseRepositoryTest {
  @Test
  public void testGetRepositories() throws SimalRepositoryException {
    Set<IDoapRepository> repos = project1.getRepositories();
    assertEquals("Should have two repository objects", 2, repos.size());
    
    assertTrue("Does not contain the expected repositories", repos.toString().contains(TEST_SIMAL_PROJECT_REPOSITORIES));
  }
}

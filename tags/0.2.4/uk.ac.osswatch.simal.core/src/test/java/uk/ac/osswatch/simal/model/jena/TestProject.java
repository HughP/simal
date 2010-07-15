/*
 * Copyright 2010 University of Oxford 
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

package uk.ac.osswatch.simal.model.jena;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 *
 */
public class TestProject extends AbstractJenaModelTest {

  private static final String TEST_PROJECT_SEE_ALSO = "http://www.oss-watch.ac.uk/ossWatch#";

  @Test
  public void testGenerateURL() {
    try {
      IProject project = SimalRepositoryFactory.getProjectService().findProjectBySeeAlso(TEST_PROJECT_SEE_ALSO);
      if(project != null && project instanceof Project) {
        String generatedURL = ((Project) project).generateURL();
        assertEquals("http://localhost:8080/project/simalID/" + project.getSimalID(),generatedURL);
        return;
      }
      fail("Project with seeAlso '" + TEST_PROJECT_SEE_ALSO + "'that was just inserted seems null");
    } catch (SimalRepositoryException e) {
      fail("Couldn't find project just inserted.");
    }
  }
}

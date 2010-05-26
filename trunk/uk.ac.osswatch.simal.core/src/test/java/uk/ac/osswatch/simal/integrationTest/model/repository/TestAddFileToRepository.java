/*
 * Copyright 2010 University of Oxford
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
package uk.ac.osswatch.simal.integrationTest.model.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

import static junit.framework.Assert.fail;

/**
 * test common activities relating to Projects.
 * 
 */
public class TestAddFileToRepository extends BaseRepositoryTest {
  private static final Logger LOGGER = LoggerFactory
      .getLogger(TestAddFileToRepository.class);

  private static final String[] PROJECT_URIS = {
      "http://simal.oss-watch.ac.uk/FromForm",
      "http://simal.oss-watch.ac.uk/FromFormSimpler" };

  private static final String[] ADD_FROM_FORM_FILE = {
      "testData/testAddFromForm.xml", "testData/testAddFromFormSimpler.xml" };

  @Test
  public void testAdd() throws SimalRepositoryException, URISyntaxException,
      IOException {
    int i = 0;
    for (String testFileName : ADD_FROM_FORM_FILE) {
      File testFile = new File(ISimalRepository.class.getClassLoader()
          .getResource(testFileName).toURI());

      FileInputStream fis = new FileInputStream(testFile);
      try {
        int x = fis.available();
        byte b[] = new byte[x];
        fis.read(b);
        String data = new String(b);
        getRepository().add(data);
      } catch (Exception e) {
        LOGGER.warn("Exception adding test data: " + e.getMessage(), e);
        fail();
      } finally {
        fis.close();
        IProject project1 = SimalRepositoryFactory.getProjectService().getProject(PROJECT_URIS[i]);
        if(project1 != null) {
          project1.delete();
        }
        i++;
      }
    }
  }

}

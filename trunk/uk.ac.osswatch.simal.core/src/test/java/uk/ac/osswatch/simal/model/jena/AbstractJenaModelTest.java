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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.ModelSupport;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 *
 */
public abstract class AbstractJenaModelTest {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(AbstractJenaModelTest.class);

  protected static ISimalRepository repository;
  private static final String TEST_OSS_WATCH_DOAP = "testData/ossWatchDOAP.xml";

  @AfterClass
  public static void tearDownAfterClass() {
    if (repository != null) {
      try {
        repository.destroy();
      } catch (SimalRepositoryException e) {
        LOGGER.warn("Destroying repository unexpectedly failed.", e); 
      }
    }
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    repository = SimalRepositoryFactory
        .getInstance(SimalRepositoryFactory.JENA);
    if (!repository.isInitialised()) {
      repository.initialise(System.getProperty("java.io.tmpdir"));
    }
    
    repository.addProject(ISimalRepository.class.getClassLoader().getResource(
        TEST_OSS_WATCH_DOAP), ModelSupport.TEST_FILE_BASE_URL);
  }

}
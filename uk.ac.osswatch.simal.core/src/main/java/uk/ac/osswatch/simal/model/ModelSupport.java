package uk.ac.osswatch.simal.model;
/*
 * 
 * Copyright 2007 University of Oxford
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


import java.net.URL;

import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * Utility methods for working with a Simal model.
 *
 */
public class ModelSupport {
  public static final String TEST_FILE_BASE_URL = "http://example.org/baseURI";
  public static final String TEST_FILE_URI_NO_QNAME = "testNoRDFAboutDOAP.xml";
  public static final String TEST_FILE_URI_WITH_QNAME = "testDOAP.xml";
  public static final String TEST_FILE_REMOTE_URL = "http://svn.apache.org/repos/asf/velocity/site/site/doap_anakia.rdf";

  public final static String CATEGORIES_RDF = "testData/categories.xml";

  /**
   * Adds Simal defined data to the repo. The simal data includes
   * the DOAP for Simal itself, which in turn includes descriptions
   * of all the appropriate categories used by Simal.
   * @throws SimalRepositoryException 
   * 
   * @throws SimalRepositoryException
   */
  public static void addSimalData(ISimalRepository repo) throws SimalRepositoryException {
    repo.addProject(ISimalRepository.class.getResource("/simal.rdf"), TEST_FILE_BASE_URL);
  }
  
  /**
   * Adds test data to the repo. be careful to only use this when the repo in
   * use is a test repository.
   * 
   * @throws SimalRepositoryException
   */
  public static void addTestData(ISimalRepository repo) {
    try {     
      addSimalData(repo);
      
      repo.addProject(ISimalRepository.class.getResource("/testData/"
          + TEST_FILE_URI_NO_QNAME), TEST_FILE_BASE_URL);

      repo.addProject(ISimalRepository.class.getResource("/testData/"
          + TEST_FILE_URI_WITH_QNAME), TEST_FILE_BASE_URL);

      repo.addProject(ISimalRepository.class.getResource("/testData/"
          + "ossWatchDOAP.xml"), TEST_FILE_BASE_URL);

      repo.addProject(ISimalRepository.class.getClassLoader().getResource(
          CATEGORIES_RDF), TEST_FILE_BASE_URL);

      repo.addProject(new URL(
          "http://simal.oss-watch.ac.uk/projectDetails/codegoo.rdf"),
          "http://simal.oss-watch.ac.uk");
    } catch (Exception e) {
      System.err.println("Can't add the test data, there's no point in carrying on");
      e.printStackTrace();
      System.exit(1);
          
    }
  }
}

package uk.ac.osswatch.simal.rdf;
/*
 * 
Copyright 2007 University of Oxford * 
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

import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.model.IProject;

public abstract class AbstractSimalRepository implements ISimalRepository {
  private static final Logger logger = LoggerFactory.getLogger(AbstractSimalRepository.class);

  protected static ISimalRepository instance;
  protected boolean isTest = false;
  protected boolean initialised;

  /**
   * Return true if this repository has been successfully initialised and is
   * ready to be used, otherwise return false.
   * 
   * @return
   */
  public boolean isInitialised() {
    return initialised;
  }

  public void setIsTest(boolean newValue) throws SimalRepositoryException {
    if (isInitialised() && isTest != newValue) {
      throw new SimalRepositoryException(
          "Unable to change the value of SimalRepository.isTest after initialisation.");
    }
    isTest = newValue;
  }

  /**
   * Checks to see if the repository has been correctly initialised. If it has
   * not then an exception is thrown.
   * 
   * @throws SimalRepositoryException
   */
  protected void verifyInitialised() throws SimalRepositoryException {
    if (!isInitialised()) {
      throw new SimalRepositoryException(
          "SimalRepsotory has not been initialised. Call one of the initialise methods first.");
    }
  }
  
  /**
   * Adds test data to the repo. be careful to only use this when the repo in
   * use is a test repository.
   * 
   * @throws SimalRepositoryException
   * 
   * @throws SimalRepositoryException
   */
  protected void addTestData() {
    try {
      verifyInitialised();
      
      addProject(ISimalRepository.class.getResource("/testData/"
          + TEST_FILE_URI_NO_QNAME), TEST_FILE_BASE_URL);

      addProject(ISimalRepository.class.getResource("/testData/"
          + TEST_FILE_URI_WITH_QNAME), TEST_FILE_BASE_URL);

      addProject(ISimalRepository.class.getResource("/testData/"
          + "ossWatchDOAP.xml"), TEST_FILE_BASE_URL);

      addProject(ISimalRepository.class.getClassLoader().getResource(
          CATEGORIES_RDF), TEST_FILE_BASE_URL);

      addProject(new URL(
          "http://simal.oss-watch.ac.uk/projectDetails/codegoo.rdf"),
          "http://simal.oss-watch.ac.uk");
    } catch (Exception e) {
      System.err.println("Can't add the test data, there's no point in carrying on");
      e.printStackTrace();
      System.exit(1);
          
    }
  }
  


  /**
   * Get the default QName for a Project. The default QName should be used if
   * the original resource does not provide a QName.
   * 
   * @param project
   *          the project for which we need a QName
   * @return
   */
  public QName getDefaultQName(IProject project) {
    String strQName;
    if (project.getHomepages() == null
        || project.getHomepages().size() == 0) {
      strQName = "http://simal.oss-watch.ac.uk/project/unkownSource/"
          + (String) project.getNames().toArray()[0];
    } else {
      strQName = project.getHomepages().toArray()[0].toString();
      if (!strQName.endsWith("/")) {
        strQName = strQName + "/";
      }
      strQName = strQName + (String) project.getNames().toArray()[0];
    }
    return new QName(strQName);
  }

  public String getNewProjectID() throws SimalRepositoryException {
    String strID = SimalProperties.getProperty(SimalProperties.PROPERTY_SIMAL_NEXT_PROJECT_ID, "1");
    long id = Long.parseLong(strID);

    /**
     * If the properties file is lost for any reason the next ID value will be
     * lost. We therefore need to perform a sanity check that this is unique.
     */
    boolean validID = false;
    while (!validID) {
      if (findProjectById(Long.toString(id)) == null) {
        validID = true;
      } else {
        id = id + 1;
      }
    }

    long newId = id + 1;
    SimalProperties.setProperty(SimalProperties.PROPERTY_SIMAL_NEXT_PROJECT_ID, Long.toString(newId));
    try {
      SimalProperties.save();
    } catch (Exception e) {
      logger.warn("Unable to save properties file", e);
      throw new SimalRepositoryException(
          "Unable to save properties file when creating the next project ID", e);
    }
    return Long.toString(id);
  }

  public String getNewPersonID() throws SimalRepositoryException {
    String strID = SimalProperties.getProperty(SimalProperties.PROPERTY_SIMAL_NEXT_PERSON_ID, "1");
    long id = Long.parseLong(strID);

    /**
     * If the properties file is lost for any reason the next ID value will be
     * lost. We therefore need to perform a sanity check that this is unique.
     */
    boolean validID = false;
    while (!validID) {
      if (findPersonById(Long.toString(id)) == null) {
        validID = true;
      } else {
        id = id + 1;
      }
    }

    long newId = id + 1;
    SimalProperties.setProperty(SimalProperties.PROPERTY_SIMAL_NEXT_PERSON_ID, Long.toString(newId));
    try {
      SimalProperties.save();
    } catch (Exception e) {
      logger.warn("Unable to save properties file", e);
      throw new SimalRepositoryException(
          "Unable to save properties file when creating the next project ID", e);
    }
    return Long.toString(id);
  }
}

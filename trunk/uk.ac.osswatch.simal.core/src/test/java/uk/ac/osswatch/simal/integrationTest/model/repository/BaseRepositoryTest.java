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
package uk.ac.osswatch.simal.integrationTest.model.repository;

import java.net.URISyntaxException;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;

/**
 * A base class for repository integration tests. This class provides utility
 * methods for setting up the test repository.
 * 
 */
public abstract class BaseRepositoryTest {

  public static final String TEST_PROJECT_URI = "http://simal.oss-watch.ac.uk/simalTest#";
  public static final String TEST_SIMAL_PROJECT_NAME = "Simal DOAP Test";
  public static final String TEST_SIMAL_PROJECT_SHORT_DESC = "A simple DOAP file used during automated testing.";
  public static final String TEST_SIMAL_PROJECT_CREATED = "2007-08-08";
  public static final String TEST_SIMAL_PROJECT_DESCRIPTION = "This is the full description of this DOAP file that is used during automated testing of Simal. It contains examples of all the DOAP elements that are currently in use within Simal.";
  public static final String TEST_SIMAL_PROJECT_LICENCE_ONE = "http://usefulinc.com/doap/licenses/gpl";
  public static final String TEST_SIMAL_PROJECT_LICENCE_TWO = "http://usefulinc.com/doap/licenses/asl20";
  public static final String TEST_SIMAL_PROJECT_WIKIS = "http://wiki.foo.org";
  public static final String TEST_SIMAL_PROJECT_TRANSLATORS = "Translator";
  public static final String TEST_SIMAL_PROJECT_TESTERS = "Tester";
  public static final String TEST_SIMAL_PROJECT_SCREENSHOTS = "http://www.foo.org/screenshots";

  public static final String TEST_SIMAL_PROJECT_REPOSITORIES = "http://simal.oss-watch.ac.uk/simalTest#svnTrunk";
  public static final String TEST_SIMAL_PROJECT_REPOSITORIES_ANON_ROOTS = "";
  public static final String TEST_SIMAL_PROJECT_REPOSITORIES_BROWSE_URL = "http://svn.foo.org/viewvc/simal/trunk/";
  public static final String TEST_SIMAL_PROJECT_REPOSITORIES_LOCATIONS = "https://svn.foo.org/svnroot/simalTest";

  public static final String TEST_SIMAL_PROJECT_RELEASES = "Simal Project Registry";
  public static final String TEST_SIMAL_PROJECT_RELEASES_FILE_RELEASES = "";
  public static final String TEST_SIMAL_PROJECT_RELEASES_REVISIONS = "0.1";

  public static final String TEST_SIMAL_PROJECT_PROGRAMMING_LANGUAGE_ONE = "Java";
  public static final String TEST_SIMAL_PROJECT_PROGRAMMING_LANGUAGE_TWO = "XML";
  public static final String TEST_SIMAL_PROJECT_OS = "Cross Platform";
  public static final String TEST_SIMAL_PROJECT_OLD_HOMEPAGES = "Original home page at OSS Watch";

  public static final int TEST_SIMAL_PROJECT_NUMBER_OF_MAINTAINERS = 3;
  public static final String TEST_SIMAL_PROJECT_MAINTAINER_ONE = "Joe Blogs Maintainer";
  public static final String TEST_SIMAL_PROJECT_MAINTAINER_TWO = "Jane Blogs Maintainer";

  public static final int TEST_SIMAL_PROJECT_NUMBER_OF_MAILING_LIST = 2;
  public static final String TEST_SIMAL_PROJECT_MAILING_LIST_ONE = "Mailing List 1";
  public static final String TEST_SIMAL_PROJECT_MAILING_LIST_TWO = "Mailing List 2";

  public static final int TEST_SIMAL_PROJECT_NUMBER_OF_HOMEPAGES = 2;
  public static final String TEST_SIMAL_PROJECT_HOMEPAGE_URL_ONE = "http://doapTest.oss-watch.ac.uk";
  public static final String TEST_SIMAL_PROJECT_HOMEPAGE_URL_TWO = "http://code.foo.com/p/simal";
  public static final String TEST_SIMAL_PROJECT_HOMEPAGE_NAME_ONE = "Project Home Page";
  public static final String TEST_SIMAL_PROJECT_HOMEPAGE_NAME_TWO = "Developer Home Page";

  public static final int TEST_SIMAL_PROJECT_NUMBER_OF_HELPERS = 1;
  public static final String TEST_SIMAL_PROJECT_HELPERS = "helper";

  public static final String TEST_SIMAL_PROJECT_DOWNLOAD_PAGES = "http://download.foo.org";
  public static final String TEST_SIMAL_PROJECT_DOWNLOAD_MIRRORS = "http://download.bar.org";

  public static final int TEST_SIMAL_PROJECT_NUMBER_OF_DOCUMENTERS = 1;
  public static final String TEST_SIMAL_PROJECT_DOCUMENTERS = "documenter";

  public static final int TEST_SIMAL_PROJECT_NUMBER_OF_DEVELOPERS = 2;
  public static final String TEST_SIMAL_PROJECT_DEVELOPERS = "developer";

  public static final int TEST_SIMAL_PROJECT_NUMBER_OF_TESTERS = 1;

  public static final int TEST_SIMAL_PROJECT_NUMBER_OF_TRANSLATORS = 1;

  public static final String TEST_SIMAL_PROJECT_CATEGORY_ONE = "DOAP Test";
  public static final String TEST_SIMAL_PROJECT_CATEGORY_TWO = "http://simal.oss-watch.ac.uk/category/supplementaryDOAPTest#";

  public static final String TEST_SIMAL_PROJECT_ISSUE_TRACKER = "http://issues.foo.org";

  public static final String TEST_SIMAL_ORGANISATION_NAME = "Test Organisation";

  protected final static String project1SeeAlso = "http://foo.org/seeAlso";

  protected final static String KNOWN_PERSON_SEALSO_URI = "http://foo.org/people/knownPerson/foaf.rdf";

  protected static ISimalRepository repository;

  protected static IProject project1;
  public static String testProjectURI;
  protected static String testProjectID;

  public static String testDeveloperURI;
  public static String testDocumentorURI;
  protected static String testDeveloperID;
  protected static String testDeveloperEMail;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    SimalProperties.setProperty(
        SimalProperties.PROPERTY_LOCAL_PROPERTIES_LOCATION,
        "local.simal.test.properties");
    SimalProperties.deleteLocalProperties();
    
    initRepository();
  }

  @AfterClass
  public static void tearDownAfterClass() {
    if (repository != null) {
      try {
        repository.destroy();
      } catch (SimalRepositoryException e) {
        // Don't care if failing to destroy repo 
      }
    }
  }

  protected static void initRepository() throws SimalRepositoryException,
      URISyntaxException {
    repository = SimalRepositoryFactory.getInstance(SimalRepositoryFactory.JENA);
    if (!repository.isInitialised()) {
      repository.setIsTest(true);
      repository.initialise(null);
    }
    project1 = SimalRepositoryFactory.getProjectService().findProjectBySeeAlso(TEST_PROJECT_URI);
    IPerson developer = SimalRepositoryFactory.getPersonService().findBySeeAlso("http://foo.org/~developer/#me");
    testDeveloperID = developer.getUniqueSimalID();
    testDeveloperEMail = developer.getEmail().iterator().next().getAddress();
    
    IPerson documentor = SimalRepositoryFactory.getPersonService().findBySeeAlso("http://foo.org/~documentor/#me");
    String documentorID = documentor.getUniqueSimalID();

    testProjectID = project1.getUniqueSimalID();
    testProjectURI = RDFUtils.getDefaultProjectURI(testProjectID);
    testDeveloperURI = RDFUtils.getDefaultPersonURI(testDeveloperID);
    testDocumentorURI = RDFUtils.getDefaultPersonURI(documentorID);
  }

  /**
   * Get the number of participants found in the main test project.
   * 
   * @return
   */
  public static int getNumberOfParticipants() {
    int colleagues = TEST_SIMAL_PROJECT_NUMBER_OF_MAINTAINERS;
    colleagues += TEST_SIMAL_PROJECT_NUMBER_OF_DEVELOPERS;
    colleagues += TEST_SIMAL_PROJECT_NUMBER_OF_DOCUMENTERS;
    colleagues += TEST_SIMAL_PROJECT_NUMBER_OF_HELPERS;
    colleagues += TEST_SIMAL_PROJECT_NUMBER_OF_TRANSLATORS;
    colleagues += TEST_SIMAL_PROJECT_NUMBER_OF_TESTERS;
    return colleagues;
  }

  public static ISimalRepository getRepository() {
    return repository;
  }

}

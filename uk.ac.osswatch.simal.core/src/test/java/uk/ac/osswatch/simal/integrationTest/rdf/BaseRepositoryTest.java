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
package uk.ac.osswatch.simal.integrationTest.rdf;

import javax.xml.namespace.QName;

import org.junit.BeforeClass;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A base class for repository integration tests. This class provides utility
 * methods for setting up the test repository.
 * 
 */
public abstract class BaseRepositoryTest {

  public static final String TEST_SIMAL_PROJECT_QNAME = "http://simal.oss-watch.ac.uk/simalTest#";
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

  public static final String TEST_SIMAL_PROJECT_RELEASES = "http://simal.oss-watch.ac.uk/simalTest#simal-0.1";
  public static final String TEST_SIMAL_PROJECT_RELEASES_FILE_RELEASES = "";
  public static final String TEST_SIMAL_PROJECT_RELEASES_REVISIONS = "0.1";

  public static final String TEST_SIMAL_PROJECT_PROGRAMMING_LANGUAGE_ONE = "Java";
  public static final String TEST_SIMAL_PROJECT_PROGRAMMING_LANGUAGE_TWO = "XML";
  public static final String TEST_SIMAL_PROJECT_OS = "Cross Platform";
  public static final String TEST_SIMAL_PROJECT_OLD_HOMEPAGES = "http://www.oss-watch.ac.uk/simal";

  public static final int TEST_SIMAL_PROJECT_NUMBER_OF_MAINTAINERS = 3;
  public static final String TEST_SIMAL_PROJECT_MAINTAINER_ONE = "Joe Maintainer";
  public static final String TEST_SIMAL_PROJECT_MAINTAINER_TWO = "Jane Maintainer";

  public static final int TEST_SIMAL_PROJECT_NUMBER_OF_MAILING_LIST = 2;
  public static final String TEST_SIMAL_PROJECT_MAILING_LIST_ONE = "Mailing List 1";
  public static final String TEST_SIMAL_PROJECT_MAILING_LIST_TWO = "Mailing List 2";

  public static final int TEST_SIMAL_PROJECT_NUMBER_OF_HOMEPAGES = 2;
  public static final String TEST_SIMAL_PROJECT_HOMEPAGE_URL_ONE = "http://simal.oss-watch.ac.uk";
  public static final String TEST_SIMAL_PROJECT_HOMEPAGE_URL_TWO = "http://code.google.com/p/simal";
  public static final String TEST_SIMAL_PROJECT_HOMEPAGE_NAME_ONE = "Project Home Page";
  public static final String TEST_SIMAL_PROJECT_HOMEPAGE_NAME_TWO = "Developer Home Page";
  
  public static final int TEST_SIMAL_PROJECT_NUMBER_OF_HELPERS = 1;
  public static final String TEST_SIMAL_PROJECT_HELPERS = "helper";

  public static final String TEST_SIMAL_PROJECT_DOWNLOAD_PAGES = "http://download.foo.org";
  public static final String TEST_SIMAL_PROJECT_DOWNLOAD_MIRRORS = "http://download.bar.org";

  public static final int TEST_SIMAL_PROJECT_NUMBER_OF_DOCUMENTERS = 1;
  public static final String TEST_SIMAL_PROJECT_DOCUMENTERS = "documenter";

  public static final int TEST_SIMAL_PROJECT_NUMBER_OF_DEVELOPERS = 1;
  public static final String TEST_SIMAL_PROJECT_DEVELOPERS = "developer";

  public static final int TEST_SIMAL_PROJECT_NUMBER_OF_TESTERS = 1;

  public static final int TEST_SIMAL_PROJECT_NUMBER_OF_TRANSLATORS = 1;

  public static final String TEST_SIMAL_PROJECT_CATEGORY_ONE = "DOAP Test";
  public static final String TEST_SIMAL_PROJECT_CATEGORY_TWO = "http://simal.oss-watch.ac.uk/category/supplementaryDOAPTest#";

  public static final String TEST_SIMAL_PROJECT_ISSUE_TRACKER = "http://issues.foo.org";

  protected static SimalRepository repository;

  protected static IProject project1;
  protected static String project1ID = "200";

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    SimalProperties.setProperty(
        SimalProperties.PROPERTY_LOCAL_PROPERTIES_LOCATION,
        "local.simal.test.properties");
    SimalProperties.deleteLocalProperties();

    initRepository();
  }
  
  protected static void initRepository() throws SimalRepositoryException {
    repository = SimalRepository.getInstance();
    if (!repository.isInitialised()) {
      repository.setIsTest(true);
      repository.initialise();
    }
    project1 = getSimalTestProject();
    IPerson developer = project1.getDevelopers().iterator().next();
    developer.setSimalId("15");
  }

  /**
   * Get the default test project. This is generated from the testDOAP.xml file.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  protected static IProject getSimalTestProject()
      throws SimalRepositoryException {
    QName qname;
    IProject project;
    qname = new QName(TEST_SIMAL_PROJECT_QNAME);
    project = repository.getProject(qname);
    return project;
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

}
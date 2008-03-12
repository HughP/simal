package uk.ac.osswatch.simal.integrationTest.rdf;

import javax.xml.namespace.QName;

import org.junit.BeforeClass;

import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.TransactionException;

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

  public static final String TEST_SIMAL_PROJECT_MAINTAINER_ONE = "Joe Blogs Maintainer";
  public static final String TEST_SIMAL_PROJECT_MAINTAINER_TWO = "Jane Blógs Maintainer";

  public static final int TEST_SIMAL_PROJECT_NUMBER_OF_MAILING_LIST = 2;
  public static final String TEST_SIMAL_PROJECT_MAILING_LIST_ONE = "http://foo.org/mailingList1";
  public static final String TEST_SIMAL_PROJECT_MAILING_LIST_TWO = "http://foo.org/mailingList2";

  public static final String TEST_SIMAL_PROJECT_HOMEPAGE_ONE = "Project Home Page";
  public static final String TEST_SIMAL_PROJECT_HOMEPAGE_TWO = "Developer Home Page";

  public static final String TEST_SIMAL_PROJECT_HELPERS = "helper";

  public static final String TEST_SIMAL_PROJECT_DOWNLOAD_PAGES = "http://download.foo.org";
  public static final String TEST_SIMAL_PROJECT_DOWNLOAD_MIRRORS = "http://download.bar.org";

  public static final String TEST_SIMAL_PROJECT_DOCUMENTORS = "documentor";

  public static final String TEST_SIMAL_PROJECT_DEVELOPERS = "developer";

  public static final String TEST_SIMAL_PROJECT_CATEGORY_ONE = "DOAP Test";
  public static final String TEST_SIMAL_PROJECT_CATEGORY_TWO = "http://simal.oss-watch.ac.uk/category/supplementaryDOAPTest#";

  public static final String TEST_SIMAL_PROJECT_ISSUE_TRACKER = "http://issues.foo.org";

  protected static SimalRepository repository;

  protected static IProject project1;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    createRepository();
    project1 = getSimalTestProject(false);
  }

  protected static void createRepository() throws SimalRepositoryException {
    repository = new SimalRepository();
    initialiseRepository(false);
    rollbackAndStartTransaction();
  }

  protected void resetTestData() throws SimalRepositoryException {
    rollbackAndStartTransaction();
    project1 = getSimalTestProject(true);
  }

  /**
   * Initialise a test repository. If one already exists do nothing unless the
   * reset param is true, in which case rollback the current transaction and
   * start a new one. If no repository exists then create one with default test
   * data.
   * 
   * @param reset
   *          if true then reset the repo to the original test data
   * @return
   * @throws SimalRepositoryException
   */
  protected static void initialiseRepository(boolean reset)
      throws SimalRepositoryException {
    try {
      if (repository.isInitialised() && reset) {
        repository.rollback();
        repository.startTransaction();
      }
      if (!repository.isInitialised()) {
        repository.setIsTest(true);
        repository.initialise();
        repository.startTransaction();
      }
    } catch (TransactionException e) {
      throw new SimalRepositoryException(e.getMessage(), e);
    }
  }

  /**
   * Get the default test project. This is generated from the testDOAP.xml file.
   * you should reset the data is you are testing any of the following fields:
   * 
   * <ul>
   * <li>name</li>
   * </ul>
   * 
   * @param reset
   *          true if the data is to be reset
   * 
   * @param repo
   * @return
   * @throws SimalRepositoryException
   */
  protected static IProject getSimalTestProject(boolean reset)
      throws SimalRepositoryException {
    QName qname;
    IProject project;
    qname = new QName(TEST_SIMAL_PROJECT_QNAME);
    if (reset) {
      rollbackAndStartTransaction();
    }
    project = repository.getProject(qname);
    return project;
  }

  /**
   * Rollback any existing transaction and start a new one. Of no existing
   * transaction is active just start one.
   * 
   * @throws SimalRepositoryException
   */
  protected static void rollbackAndStartTransaction()
      throws SimalRepositoryException {
    try {
      repository.rollback();
    } catch (TransactionException e) {
      // we don't care since we have no idea what state we are in
      // simply assume this is OK for test purposes
    }
    try {
      repository.startTransaction();
    } catch (TransactionException e) {
      // we don't care since we have no idea what state we are in
      // simply assume this is OK for test purposes
    }
  }

}

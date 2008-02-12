package uk.ac.osswatch.simal.rdf.integrationTest;

import javax.xml.namespace.QName;

import uk.ac.osswatch.simal.model.elmo.Project;
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
	public static final String TEST_SIMAL_PROJECT_WIKIS = "[http://wiki.foo.org]";
	public static final String TEST_SIMAL_PROJECT_TRANSLATORS = "[http://foo.org/~translator/#me]";
	public static final String TEST_SIMAL_PROJECT_TESTERS = "[http://foo.org/~tester/#me]";
	public static final String TEST_SIMAL_PROJECT_SCREENSHOTS = "[http://www.foo.org/screenshots]";
	
	public static final String TEST_SIMAL_PROJECT_REPOSITORIES = "[http://simal.oss-watch.ac.uk/simalTest#svnTrunk]";
	public static final String TEST_SIMAL_PROJECT_REPOSITORIES_ANON_ROOTS = "[]";
	public static final String TEST_SIMAL_PROJECT_REPOSITORIES_BROWSE_URL = "[http://svn.foo.org/viewvc/simal/trunk/]";
	public static final String TEST_SIMAL_PROJECT_REPOSITORIES_LOCATIONS = "[https://svn.foo.org/svnroot/simalTest]";
		
	public static final String TEST_SIMAL_PROJECT_RELEASES = "[http://simal.oss-watch.ac.uk/simalTest#simal-0.1]";
	public static final String TEST_SIMAL_PROJECT_RELEASES_FILE_RELEASES = "[]";
	public static final String TEST_SIMAL_PROJECT_RELEASES_REVISIONS = "[0.1]";
	
	public static final String TEST_SIMAL_PROJECT_PROGRAMMING_LANGUAGE_ONE = "Java";
	public static final String TEST_SIMAL_PROJECT_PROGRAMMING_LANGUAGE_TWO = "XML";
	public static final String TEST_SIMAL_PROJECT_OS = "Cross Platform";
	public static final String TEST_SIMAL_PROJECT_OLD_HOMEPAGES = "[http://www.oss-watch.ac.uk/simal]";
	
	public static final String TEST_SIMAL_PROJECT_MAINTAINER_ONE = "http://foo.org/~joeBlogs/#me";
	public static final String TEST_SIMAL_PROJECT_MAINTAINER_TWO = "http://foo.org/~janeBlogs/#me";
	
	public static final int TEST_SIMAL_PROJECT_NUMBER_OF_MAILING_LIST = 2;
	public static final String TEST_SIMAL_PROJECT_MAILING_LIST_ONE = "http://foo.org/mailingList1";
	public static final String TEST_SIMAL_PROJECT_MAILING_LIST_TWO = "http://foo.org/mailingList2";
	
	public static final String TEST_SIMAL_PROJECT_HOMEPAGE_ONE = "http://simal.oss-watch.ac.uk";
	public static final String TEST_SIMAL_PROJECT_HOMEPAGE_TWO = "http://code.google.com/p/simal";
	
	public static final String TEST_SIMAL_PROJECT_HELPERS = "[http://foo.org/~helper/#me]";
	
	public static final String TEST_SIMAL_PROJECT_DOWNLOAD_PAGES = "[http://download.foo.org]";
	public static final String TEST_SIMAL_PROJECT_DOWNLOAD_MIRRORS = "[http://download.bar.org]";
	
	public static final String TEST_SIMAL_PROJECT_DOCUMENTORS = "[http://foo.org/~documentor/#me]";
	
	public static final String TEST_SIMAL_PROJECT_DEVELOPERS = "[http://foo.org/~developer/#me]";
	
	public static final String TEST_SIMAL_PROJECT_CATEGORY_ONE = "http://simal.oss-watch.ac.uk/category/doapTest#";
	public static final String TEST_SIMAL_PROJECT_CATEGORY_TWO = "http://simal.oss-watch.ac.uk/category/supplementaryDOAPTest#";
	
	public static final String TEST_SIMAL_PROJECT_ISSUE_TRACKER = "[http://issues.foo.org]";

	public BaseRepositoryTest() {
		super();
	}

	/**
	 * Initialise a test repository. If one already exists do nothing unless the
	 * reset param is true, in which case destroy the existing one and
	 * initialise a new repository with default test data. If no repository
	 * exists then create one with default test data.
	 * 
	 * @param reset
	 *            if true then reset the repo to the original test data
	 * @return
	 * @throws SimalRepositoryException
	 */
	protected static void initialiseRepository(boolean reset)
			throws SimalRepositoryException {
		if (SimalRepository.isInitialised() && reset) {
			SimalRepository.destroy();
		}
		if (!SimalRepository.isInitialised()) {
			SimalRepository.initialise();
		}
	}

	/**
	 * Get the default test project. This is generated from the testDOAP.xml
	 * file. you should reset the data is you are testing any of the following
	 * fields:
	 * 
	 * <ul>
	 * <li>name</li>
	 * </ul>
	 * 
	 * @param reset
	 *            true if the data is to be reset
	 * 
	 * @param repo
	 * @return
	 * @throws SimalRepositoryException
	 */
	protected static Project getSimalTestProject(boolean reset)
			throws SimalRepositoryException {
		QName qname;
		Project project;
		qname = new QName(TEST_SIMAL_PROJECT_QNAME);
		if (reset) {
		  resetTestData();
		}
		project = SimalRepository.getProject(qname);
		return project;
	}

	/**
	 * Reset the repository and the test data.
	 * 
	 * @throws SimalRepositoryException
	 */
	private static void resetTestData() throws SimalRepositoryException {
		initialiseRepository(true);
	}

}

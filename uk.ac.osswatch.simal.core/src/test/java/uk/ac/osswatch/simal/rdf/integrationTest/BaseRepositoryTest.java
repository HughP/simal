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

	protected static final String TEST_SIMAL_PROJECT_QNAME = "http://simal.oss-watch.ac.uk/simalTest#";
	protected static final String TEST_SIMAL_PROJECT_NAME = "Simal DOAP Test";
	protected static final String TEST_SIMAL_PROJECT_SHORT_DESC = "A simple DOAP file used during automated testing.";
	protected static final String TEST_SIMAL_PROJECT_CREATED = "2007-08-08";
	protected static final String TEST_SIMAL_PROJECT_DESCRIPTION = "This is the full description of this DOAP file that is used during automated testing of Simal. It contains examples of all the DOAP elements that are currently in use within Simal.";
	protected static final String TEST_SIMAL_PROJECT_LICENCE_ONE = "http://usefulinc.com/doap/licenses/gpl";
	protected static final String TEST_SIMAL_PROJECT_LICENCE_TWO = "http://usefulinc.com/doap/licenses/asl20";
	protected static final String TEST_SIMAL_PROJECT_WIKIS = "[http://wiki.foo.org]";
	protected static final String TEST_SIMAL_PROJECT_TRANSLATORS = "[http://foo.org/~translator/#me]";
	protected static final String TEST_SIMAL_PROJECT_TESTERS = "[http://foo.org/~tester/#me]";
	protected static final String TEST_SIMAL_PROJECT_SCREENSHOTS = "[http://www.foo.org/screenshots]";
	
	protected static final String TEST_SIMAL_PROJECT_REPOSITORIES = "[http://simal.oss-watch.ac.uk/simalTest#svnTrunk]";
	protected static final String TEST_SIMAL_PROJECT_REPOSITORIES_ANON_ROOTS = "[]";
	protected static final String TEST_SIMAL_PROJECT_REPOSITORIES_BROWSE_URL = "[http://svn.foo.org/viewvc/simal/trunk/]";
	protected static final String TEST_SIMAL_PROJECT_REPOSITORIES_LOCATIONS = "[https://svn.foo.org/svnroot/simalTest]";
		
	protected static final String TEST_SIMAL_PROJECT_RELEASES = "[http://simal.oss-watch.ac.uk/simalTest#simal-0.1]";
	protected static final String TEST_SIMAL_PROJECT_RELEASES_FILE_RELEASES = "[]";
	protected static final String TEST_SIMAL_PROJECT_RELEASES_REVISIONS = "[0.1]";
	
	protected static final String TEST_SIMAL_PROJECT_PROGRAMMING_LANGUAGE_ONE = "Java";
	protected static final String TEST_SIMAL_PROJECT_PROGRAMMING_LANGUAGE_TWO = "XML";
	protected static final String TEST_SIMAL_PROJECT_OS = "Cross Platform";
	protected static final String TEST_SIMAL_PROJECT_OLD_HOMEPAGES = "[http://www.oss-watch.ac.uk/simal]";
	
	protected static final String TEST_SIMAL_PROJECT_MAINTAINER_ONE = "http://foo.org/~joeBlogs/#me";
	protected static final String TEST_SIMAL_PROJECT_MAINTAINER_TWO = "http://foo.org/~janeBlogs/#me";
	
	protected static final int TEST_SIMAL_PROJECT_NUMBER_OF_MAILING_LIST = 2;
	protected static final String TEST_SIMAL_PROJECT_MAILING_LIST_ONE = "http://foo.org/mailingList1";
	protected static final String TEST_SIMAL_PROJECT_MAILING_LIST_TWO = "http://foo.org/mailingList2";
	
	protected static final String TEST_SIMAL_PROJECT_HOMEPAGES = "[http://simal.oss-watch.ac.uk]";
	
	protected static final String TEST_SIMAL_PROJECT_HELPERS = "[http://foo.org/~helper/#me]";
	
	protected static final String TEST_SIMAL_PROJECT_DOWNLOAD_PAGES = "[http://download.foo.org]";
	protected static final String TEST_SIMAL_PROJECT_DOWNLOAD_MIRRORS = "[http://download.bar.org]";
	
	protected static final String TEST_SIMAL_PROJECT_DOCUMENTORS = "[http://foo.org/~documentor/#me]";
	
	protected static final String TEST_SIMAL_PROJECT_DEVELOPERS = "[http://foo.org/~developer/#me]";
	
	protected static final String TEST_SIMAL_PROJECT_CATEGORY_ONE = "http://simal.oss-watch.ac.uk/category/doapTest#";
	protected static final String TEST_SIMAL_PROJECT_CATEGORY_TWO = "http://simal.oss-watch.ac.uk/category/supplementaryDOAPTest#";
	
	protected static final String TEST_SIMAL_PROJECT_ISSUE_TRACKER = "[http://issues.foo.org]";

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
		if (reset) {
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
		resetTestData();
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

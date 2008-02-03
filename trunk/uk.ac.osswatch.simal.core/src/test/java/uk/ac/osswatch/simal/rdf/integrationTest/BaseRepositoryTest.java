package uk.ac.osswatch.simal.rdf.integrationTest;

import javax.xml.namespace.QName;

import uk.ac.osswatch.simal.model.elmo.Project;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A base class for repository integration tests.
 * This class provides utility methods for setting up
 * the test repository.
 *
 */
public abstract class BaseRepositoryTest {
	
	static SimalRepository repo;

	protected static final String TEST_SIMAL_PROJECT_QNAME = "http://simal.oss-watch.ac.uk/simalTest#";
	protected static final String TEST_SIMAL_PROJECT_NAME = "Simal DOAP Test";
	protected static final String TEST_SIMAL_PROJECT_SHORT_DESC = "A simple DOAP file used during automated testing.";
	protected static final String TEST_SIMAL_PROJECT_CREATED = "2007-08-08";
	protected static final String TEST_SIMAL_PROJECT_DESCRIPTION = "This is the full description of this DOAP file that is used during automated testing of Simal. It contains examples of all the DOAP elements that are currently in use within Simal.";
	protected static final String TEST_SIMAL_PROJECT_LICENCES = "[http://usefulinc.com/doap/licenses/asl20, http://usefulinc.com/doap/licenses/gpl]";
	protected static final String TEST_SIMAL_PROJECT_WIKIS = "[http://wiki.foo.org]";
	protected static final String TEST_SIMAL_PROJECT_TRANSLATORS = "[http://foo.org/~translator/#me]";
	protected static final String TEST_SIMAL_PROJECT_TESTERS = "[http://foo.org/~testor/#me]";
	protected static final String TEST_SIMAL_PROJECT_SCREENSHOTS = "[http://www.foo.org/screenshots]";
	protected static final String TEST_SIMAL_PROJECT_REPOSITORIES = "[http://simal.oss-watch.ac.uk/simalTest#svnTrunk]";
	protected static final String TEST_SIMAL_PROJECT_RELEASES = "[http://simal.oss-watch.ac.uk/simalTest#simal-0.1]";
	protected static final String TEST_SIMAL_PROJECT_PROGRAMMING_LANGUAGES = "[Java,X ML]";
	protected static final String TEST_SIMAL_PROJECT_OS = "[Cross Platform]";
	protected static final String TEST_SIMAL_PROJECT_OLD_HOMEPAGES = "[http://www.oss-watch.ac.uk/simal]";
	protected static final String TEST_SIMAL_PROJECT_MAINTAINERS = "[http://foo.org/~joeBlogs/#me, http://foo.org/~janeBlogs/#me]";
	protected static final String TEST_SIMAL_PROJECT_MAILING_LISTS = "[http://foo.org/mailingList1, http://foo.org/mailingList2]";
	protected static final String TEST_SIMAL_PROJECT_HOMEPAGES = "[http://simal.oss-watch.ac.uk]";
	protected static final String TEST_SIMAL_PROJECT_HELPERS = "[http://foo.org/~helper/#me]";
	protected static final String TEST_SIMAL_PROJECT_DOWNLOAD_PAGES = "[http://downlaod.foo.org]";
	protected static final String TEST_SIMAL_PROJECT_DOWNLOAD_MIRRORS = "[http://downlaod.bar.org]";
	protected static final String TEST_SIMAL_PROJECT_DOCUMENTORS = "[http://foo.org/~documentor/#me]";
	protected static final String TEST_SIMAL_PROJECT_DEVELOPERS = "[http://foo.org/~developer/#me]";
	protected static final String TEST_SIMAL_PROJECT_CATEGORIES = "[http://simal.oss-watch.ac.uk/category/doapTest#, http://simal.oss-watch.ac.uk/category/supplementaryDOAPTest#]";
	protected static final String TEST_SIMAL_PROJECT_ISSUE_TRACKER = "[http://issues.foo.org]";

	public BaseRepositoryTest() {
		super();
	}

	/**
	 * Get a test repository. If one already exists return that one,
	 * unless the reset param is true, in which case return a new
	 * repository with default test data. If no repository exists
	 * then create one with default test data.
	 * 
	 * @param reset if true then reset the repo to the original test data
	 * @return
	 * @throws SimalRepositoryException
	 */
	protected static SimalRepository getTestRepo(boolean reset) throws SimalRepositoryException {
		if (repo == null || reset) {
			repo = new SimalRepository();
		}
		return repo;
	}

	/**
	 * Get a test repository. If one already exists return that with
	 * existing data, otherwise create one and populate it with test
	 * data.
	 * 
	 * @return
	 * @throws SimalRepositoryException 
	 */
	protected static SimalRepository getTestRepo() throws SimalRepositoryException {
		return getTestRepo(false);
	}
	

	/**
	 * Get the default test project. This is generated from the testDOAP.xml
	 * file. you should reset the data is you are testing any of the following
	 * fields:
	 * 
	 * <ul>
	 *   <li>name</li>
	 * </ul>
	 * 
	 * @param reset true if the data is to be reset
	 * 
	 * @param repo
	 * @return
	 * @throws SimalRepositoryException 
	 */
	protected static Project getSimalTestProject(boolean reset) throws SimalRepositoryException {
		QName qname;
		Project project;
		qname = new QName(TEST_SIMAL_PROJECT_QNAME);
		project = getTestRepo(reset).getProject(qname);
		return project;
	}

}
	
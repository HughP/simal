package uk.ac.osswatch.simal.rdf.integrationTest;

import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import org.openrdf.elmo.sesame.SesameManager;

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

	protected static final String TEST_SIMAL_QNAME = "http://simal.oss-watch.ac.uk/simalTest#";

	protected static final String TEST_SIMAL_NAME = "Simal DOAP Test";

	protected static final String TEST_SIMAL_SHORT_DESC = "A simple DOAP file used during automated testing.";

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
		qname = new QName(TEST_SIMAL_QNAME);
		project = getTestRepo(reset).getProject(qname);
		return project;
	}

}
	
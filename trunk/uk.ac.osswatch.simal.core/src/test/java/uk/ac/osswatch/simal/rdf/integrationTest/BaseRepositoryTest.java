package uk.ac.osswatch.simal.rdf.integrationTest;

import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A base class for repository integration tests.
 * This class provides utility methods for setting up
 * the test repository.
 *
 */
public class BaseRepositoryTest {

	public BaseRepositoryTest() {
		super();
	}

	/**
	 * Create a test repository with default test data.
	 * @return
	 * @throws SimalRepositoryException
	 */
	protected SimalRepository getTestRepo() throws SimalRepositoryException {
		SimalRepository repo = new SimalRepository();
		return repo;
	}

}
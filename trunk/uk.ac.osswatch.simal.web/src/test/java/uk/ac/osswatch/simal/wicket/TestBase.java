package uk.ac.osswatch.simal.wicket;

import junit.framework.TestCase;

import org.apache.wicket.util.tester.WicketTester;

import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public abstract class TestBase extends TestCase {

	protected static final int NUMBER_OF_TEST_PROJECTS = 4;
	protected WicketTester tester;

	public TestBase() {
		super();
	}

	public TestBase(String name) {
		super(name);
	}

	public void setUp() {
		try {
			if (!SimalRepository.isInitialised()) {
				SimalRepository.initialise();
			}
		} catch (SimalRepositoryException e) {
			e.printStackTrace();
			System.exit(1);
		}

		tester = new WicketTester();
	}

}
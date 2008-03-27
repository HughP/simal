package uk.ac.osswatch.simal.wicket;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.BeforeClass;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public abstract class TestBase {

	protected static final int NUMBER_OF_TEST_PROJECTS = 4;
	protected static WicketTester tester;

	@BeforeClass
	public static void setUpBeforeClass() {
		try {
			if (!UserApplication.getRepository().isInitialised()) {
				UserApplication.getRepository().setIsTest(true);
				UserApplication.getRepository().initialise();
			}
		} catch (SimalRepositoryException e) {
			e.printStackTrace();
			System.exit(1);
		}

		tester = new WicketTester();
	}
}
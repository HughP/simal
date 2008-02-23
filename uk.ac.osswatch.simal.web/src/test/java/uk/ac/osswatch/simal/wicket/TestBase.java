package uk.ac.osswatch.simal.wicket;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.BeforeClass;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.TransactionException;

public abstract class TestBase {

	protected static final int NUMBER_OF_TEST_PROJECTS = 4;
	protected static WicketTester tester;

	@BeforeClass
	public static void setUpBeforeClass() {
		try {
			if (UserApplication.getRepository().isInitialised()) {
				try {
					UserApplication.getRepository().rollback();
				} catch (TransactionException e) {
					// we don't care if there is no transaction, we're just
					// testing
				}
				try {
					UserApplication.getRepository().startTransaction();
				} catch (TransactionException e) {
					// we don't care if there is a transaction, we're just
					// testing
				}
			} else {
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
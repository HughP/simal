package uk.ac.osswatch.simal.integrationTest.model.elmo;

import org.junit.BeforeClass;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.elmo.Project;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public abstract class AbstractTestDOAP extends BaseRepositoryTest {

	protected static Project project1;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		initialiseRepository(false);
		rollbackAndStartTransaction();

		project1 = getSimalTestProject(false);
	}

	protected static void resetTestData() throws SimalRepositoryException {
		rollbackAndStartTransaction();
		project1 = getSimalTestProject(true);
	}

	public AbstractTestDOAP() {
		super();
	}

}
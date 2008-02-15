package uk.ac.osswatch.simal.integrationTest.model.elmo;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.osswatch.simal.model.IRCS;

public class TestRCS extends AbstractTestDOAP {

	private static Set<IRCS> repositories;
	private static IRCS repository;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		initialiseRepository(false);
		project1 = getSimalTestProject(false);
		repositories = project1.getRepositories();
		repository = (IRCS) repositories.toArray()[0];
	}

	@Test
	public void testRepositoriesLoaded() {
		assertEquals("Should have one repository defined", 1, repositories
				.size());
	}

	@Test
	public void testGetAnonRoots() {
		assertEquals("Anon roots are not correct",
				TEST_SIMAL_PROJECT_REPOSITORIES_ANON_ROOTS, repository
						.getAnonRoots().toString());
	}

	@Test
	public void testGetBrowse() {
		assertEquals("Browse URL is not correct",
				TEST_SIMAL_PROJECT_REPOSITORIES_BROWSE_URL, repository
						.getBrowse().toString());
	}

	@Test
	public void testGetLocations() {
		assertEquals("Locations are not correct",
				TEST_SIMAL_PROJECT_REPOSITORIES_LOCATIONS, repository
						.getLocations().toString());
	}
}

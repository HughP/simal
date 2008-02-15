package uk.ac.osswatch.simal.integrationTest.model.elmo;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.osswatch.simal.model.IVersion;

public class TestVersion extends AbstractTestDOAP {

	private static Set<IVersion> versions;
	private static IVersion version;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		project1 = getSimalTestProject(false);
		versions = project1.getReleases();
		version = (IVersion) versions.toArray()[0];
	}

	@Test
	public void testVersionsLoaded() {
		assertEquals("Should have one version defined", 1, versions
				.size());
	}

	@Test
	public void testGetFileReleases() {
		assertEquals("File releases are not correct",
				TEST_SIMAL_PROJECT_RELEASES_FILE_RELEASES, version
						.getFileReleases().toString());
	}

	@Test
	public void testGetRevisions() {
		assertEquals("Revisions are not correct",
				TEST_SIMAL_PROJECT_RELEASES_REVISIONS, version
						.getRevisions().toString());
	}
}

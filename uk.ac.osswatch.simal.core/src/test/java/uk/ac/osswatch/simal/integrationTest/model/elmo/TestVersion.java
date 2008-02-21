package uk.ac.osswatch.simal.integrationTest.model.elmo;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IVersion;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestVersion extends BaseRepositoryTest {

	public TestVersion() throws SimalRepositoryException {
		super();
	}

	private Set<IVersion> getVersions() throws SimalRepositoryException {
		return project1.getReleases();
	}
	
	private IVersion getVersion() throws SimalRepositoryException {
		return (IVersion) getVersions().toArray()[0];
	}

	@Test
	public void testVersionsLoaded() throws SimalRepositoryException {
		assertEquals("Should have one version defined", 1, getVersions()
				.size());
	}

	@Test
	public void testGetFileReleases() throws SimalRepositoryException {
		assertEquals("File releases are not correct",
				TEST_SIMAL_PROJECT_RELEASES_FILE_RELEASES, getVersion()
						.getFileReleases().toString());
	}

	@Test
	public void testGetRevisions() throws SimalRepositoryException {
		assertEquals("Revisions are not correct",
				TEST_SIMAL_PROJECT_RELEASES_REVISIONS, getVersion()
						.getRevisions().toString());
	}
}

package uk.ac.osswatch.simal.integrationTest.model.elmo;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IDoapRelease;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestDoapReleaseBehaviour extends BaseRepositoryTest {
  @Test
  public void testGetReleases() throws SimalRepositoryException {
    Set<IDoapRelease> releases = project1.getReleases();
    IDoapRelease release = (IDoapRelease) releases.toArray()[0];
    Set<String> revisions = release.getRevisions();
    assertTrue("Don't seem to have revision 0.1", revisions.toString().contains("0.1"));
  }
}

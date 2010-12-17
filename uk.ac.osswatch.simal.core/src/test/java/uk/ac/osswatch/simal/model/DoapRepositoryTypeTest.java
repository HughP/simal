package uk.ac.osswatch.simal.model;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.osswatch.simal.integrationTest.model.repository.BaseRepositoryTest;
import uk.ac.osswatch.simal.rdf.Doap;

/**
 * Unit tests for <code>DoapRepositoryType</code> enum.
 */
public class DoapRepositoryTypeTest extends BaseRepositoryTest {

  @Test
  public void testGetResource() {
    for (DoapRepositoryType type : DoapRepositoryType.values()) {
      assertEquals(type.getResource(), DoapRepositoryType
          .getMatchingDoapRepositoryType(type.getResource()).getResource());
    }
  }

  @Test
  public void testGetLabel() {
    for (DoapRepositoryType type : DoapRepositoryType.values()) {
      assertEquals(type.getLabel(), DoapRepositoryType
          .getMatchingDoapRepositoryType(type.getResource()).getLabel());
    }
  }

  @Test
  public void testGetMatchingDoapRepositoryType() {
    assertNull("Expected no matching DoapRepositoryType for a null Resource.",
        DoapRepositoryType.getMatchingDoapRepositoryType(null));
    assertNull(
        "Expected no matching DopaRepositoryType for a Resource that's no DoapRepository.",
        DoapRepositoryType.getMatchingDoapRepositoryType(Doap.NAMESPACE));

    assertEquals(DoapRepositoryType.ARCH_REPOSITORY,
        DoapRepositoryType.getMatchingDoapRepositoryType(Doap.ARCH_REPOSITORY));
    assertEquals(DoapRepositoryType.BAZAAR_REPOSITORY,
        DoapRepositoryType.getMatchingDoapRepositoryType(Doap.BAZAARREPOSITORY));
    assertEquals(DoapRepositoryType.BK_REPOSITORY,
        DoapRepositoryType.getMatchingDoapRepositoryType(Doap.BKREPOSITORY));
    assertEquals(DoapRepositoryType.CVS_REPOSITORY,
        DoapRepositoryType.getMatchingDoapRepositoryType(Doap.CVSREPOSITORY));
    assertEquals(DoapRepositoryType.DARCS_REPOSITORY,
        DoapRepositoryType.getMatchingDoapRepositoryType(Doap.DARCSREPOSITORY));
    assertEquals(DoapRepositoryType.GIT_REPOSITORY,
        DoapRepositoryType.getMatchingDoapRepositoryType(Doap.GITREPOSITORY));
    assertEquals(DoapRepositoryType.HG_REPOSITORY,
        DoapRepositoryType.getMatchingDoapRepositoryType(Doap.HGREPOSITORY));
    assertEquals(DoapRepositoryType.SVN_REPOSITORY,
        DoapRepositoryType.getMatchingDoapRepositoryType(Doap.SVNREPOSITORY));
  }

}

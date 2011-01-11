package uk.ac.osswatch.simal.model;

/*
 * Copyright 2010 University of Oxford * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

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

/*
 * Copyright 2007 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.osswatch.simal.integrationTest.model.jena;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
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
    assertTrue("Don't seem to have revision 0.1", revisions.toString()
        .contains("0.1"));
  }

  @Test
  public void testReleaseName() {
    Iterator<IDoapRelease> releases = project1.getReleases().iterator();
    IDoapRelease release;
    while (releases.hasNext()) {
      release = releases.next();
      String name = release.getName();
      assertNotNull("Relase name should not be null", name);
    }
  }
}

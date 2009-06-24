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
package uk.ac.osswatch.simal.integrationTest.model;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IDoapLocation;
import uk.ac.osswatch.simal.model.IDoapRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestDoapRepository extends BaseRepositoryTest {
  @Test
  public void testGetRepositories() throws SimalRepositoryException {
    Set<IDoapRepository> repos = project1.getRepositories();
    assertEquals("Should have two repository objects", 2, repos.size());

    assertTrue("Does not contain the expected repositories", repos.toString()
        .contains(TEST_SIMAL_PROJECT_REPOSITORIES));
  }

  @Test
  public void testType() throws SimalRepositoryException {
    Set<IDoapRepository> repos = project1.getRepositories();
    Iterator<IDoapRepository> itr = repos.iterator();
    while (itr.hasNext()) {
      IDoapRepository repo = itr.next();
      if (repo.getURI().contains("cvs")) {
        assertTrue("CVS repo is identified as ARCH", !repo.isARCH());
        assertTrue("CVS repo is identified as BK", !repo.isBK());
        assertTrue("CVS repo is not identified as CVS", repo.isCVS());
        assertTrue("CVS repo is identified as SVN", !repo.isSVN());
      } else if (repo.getURI().contains("svn")) {
        assertTrue("SVN repo is identified as ARCH", !repo.isARCH());
        assertTrue("SVN repo is identified as BK", !repo.isBK());
        assertTrue("SVN repo is identified as CVS", !repo.isCVS());
        assertTrue("SVN repo is not identified as SVN", repo.isSVN());
      }

    }
  }

  @Test
  public void testSVNRepo() throws SimalRepositoryException {
    Set<IDoapRepository> repos = project1.getRepositories();
    Iterator<IDoapRepository> itr = repos.iterator();
    while (itr.hasNext()) {
      IDoapRepository repo = itr.next();
      if (repo.isSVN()) {
        Set<IDoapLocation> location = repo.getLocations();
        assertNotNull(repo.toString() + " does not have a location", location);

        Set<IDoapLocation> browse = repo.getBrowse();
        assertNotNull(repo.toString() + " does not have a browse", browse);
      }
    }
  }

  @Test
  public void testCVSRepo() throws SimalRepositoryException {
    Set<IDoapRepository> repos = project1.getRepositories();
    Iterator<IDoapRepository> itr = repos.iterator();
    while (itr.hasNext()) {
      IDoapRepository repo = itr.next();
      if (repo.isCVS()) {
        Set<String> anonRoot = repo.getAnonRoots();
        assertNotNull(repo.toString() + " does not have an anon root", anonRoot);

        Set<IDoapLocation> browse = repo.getBrowse();
        assertNotNull(repo.toString() + " does not have a browse", browse);

        Set<String> module = repo.getModule();
        assertNotNull(repo.toString() + " does not have a browse", module);
      }
    }
  }
}

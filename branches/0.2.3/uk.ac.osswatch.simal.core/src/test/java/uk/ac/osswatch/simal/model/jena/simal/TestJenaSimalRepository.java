/*
 * Copyright 2010 University of Oxford
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
package uk.ac.osswatch.simal.model.jena.simal;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import uk.ac.osswatch.simal.integrationTest.model.repository.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.jena.SparqlResult;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 * Test Jena specific functionality of JenaSimalRepository.
 * 
 * @author svanderwaal
 *
 */
public class TestJenaSimalRepository extends BaseRepositoryTest {

  private static final String QUERY_PREFIX = " PREFIX  xsd:  <http://www.w3.org/2001/XMLSchema#> "
      + " PREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
      + " PREFIX  doap: <http://usefulinc.com/ns/doap#> "
      + " PREFIX  simal: <http://oss-watch.ac.uk/ns/0.2/simal#> ";

  private static final String QUERY_ALL_PROJECTS = QUERY_PREFIX
      + "SELECT DISTINCT  ?project" + " WHERE "
      + " { ?project  rdf:type   doap:Project ;" + "  }";

  private static final String QUERY_INVALID = QUERY_PREFIX + "SELECT *";

  @Test
  public void testSparqlQuery() {
    SparqlResult projects;
    String expectedVarName = "project";
    int expectedNrProjects = 10;
    
    try {
      projects = getJenaSimalRepository().getSparqlQueryResult(
          QUERY_ALL_PROJECTS);
      List<String> actualVarNames = projects.getVarNames();
      assertEquals(1, actualVarNames.size());
      assertEquals(expectedVarName, actualVarNames.get(0));
      
      Iterator<List<RDFNode>> projIter  = projects.getResultsIterator();
      int actualNrProjects =0;
      while (projIter.hasNext()) {
        List<RDFNode> curResult = projIter.next();
        assertEquals(1, curResult.size());
        assertNotNull(curResult.get(0));
        actualNrProjects++;       
      }
      assertEquals(expectedNrProjects, actualNrProjects);
    } catch (SimalRepositoryException e) {
      fail("Could not query repository");
    }
  }

  @Test
  public void testInvalidSparqlQuery() {
    SparqlResult projects = null;
    try {
      projects = getJenaSimalRepository().getSparqlQueryResult(QUERY_INVALID);
      fail("Query was invalid so should fail without results.");
    } catch (SimalRepositoryException e) {
      assertNull("Query was invalid so should fail without results.", projects);
    }
  }

  private JenaSimalRepository getJenaSimalRepository() {
    ISimalRepository repo = getRepository();
    if (!(repo instanceof JenaSimalRepository)) {
      fail("Configuration problem; testing Jena when repo is not of Jena type");
    }
    return (JenaSimalRepository) repo;
  }
}

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
package uk.ac.osswatch.simal.model.jena;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 * Holds the result of a SPARQL query in tabular form
 * for easy display. This is meant as a transient data class
 * to store a query result set for use at the front end.
 * Exposes the list iterator, which makes the caller responsible
 * for first adding all results before displaying results.
 * Intended for single-threaded use because of ArrayLists.
 *  
 * @author svanderwaal
 *
 */
public class SparqlResult {
  public static final Logger LOGGER = LoggerFactory
      .getLogger(SparqlResult.class);

  private List<String> varNames;

  private List<List<RDFNode>> sparqlResults;

  /**
   * Create a new SparqlResult for given variable names.
   * @param varNames
   */
  public SparqlResult(List<String> varNames) {
    this.varNames = varNames;
    this.sparqlResults = new ArrayList<List<RDFNode>>();
  }

  /**
   * Add a result to the query result set. Corresponds to 
   * a row in a regular SQL result set.
   * @param result
   */
  public void addResult(List<RDFNode> result) {
    this.sparqlResults.add(result);
  }

  /**
   * Return the variable names that are bound for this
   * query result.
   * @return
   */
  public List<String> getVarNames() {
    return this.varNames;
  }

  /**
   * Expose iterator to let caller iterate over the result.
   * @return iterator 
   */
  public Iterator<List<RDFNode>> getResultsIterator() {
    return sparqlResults.iterator();
  }

}

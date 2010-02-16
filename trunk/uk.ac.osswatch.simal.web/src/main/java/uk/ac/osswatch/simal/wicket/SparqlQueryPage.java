/*
 * Copyright 2008 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");   *
 * you may not use this file except in compliance with the License.  *
 * You may obtain a copy of the License at                           *
 *                                                                   *
 *   http://www.apache.org/licenses/LICENSE-2.0                      *
 *                                                                   *
 * Unless required by applicable law or agreed to in writing,        *
 * software distributed under the License is distributed on an       *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY            *
 * KIND, either express or implied.  See the License for the         *
 * specific language governing permissions and limitations           *
 * under the License.                                                *
 */
package uk.ac.osswatch.simal.wicket;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.PropertyModel;

import uk.ac.osswatch.simal.wicket.data.SparqlQueryInputModel;

/**
 * A page for querying the RDF backend using SPARQL
 * 
 * @REFACTOR use an existing SPARQL endpoint provider - see ISSUE 116
 * 
 */
public class SparqlQueryPage extends BasePage {

  private static final String SPARQL_QUERY_STRING_FIELD = "query";

  private static final String QUERY_PREFIX = "PREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
      + "PREFIX  doap: <http://usefulinc.com/ns/doap#> \n"
      + "PREFIX  simal: <http://oss-watch.ac.uk/ns/0.2/simal#> \n\n";

  private final SparqlQueryInputModel inputModel = new SparqlQueryInputModel();

  /**
   * Create default SPARQL query page
   */
  public SparqlQueryPage() {
    super();

    TextArea<SparqlQueryInputModel> sparqlQueryField = new TextArea<SparqlQueryInputModel>(
        SPARQL_QUERY_STRING_FIELD, new PropertyModel<SparqlQueryInputModel>(
            inputModel, SPARQL_QUERY_STRING_FIELD));
    sparqlQueryField.add(new FocusBehaviour());
    String[] defaultValue = { QUERY_PREFIX };
    sparqlQueryField.setModelValue(defaultValue);
    add(sparqlQueryField);
  }
}

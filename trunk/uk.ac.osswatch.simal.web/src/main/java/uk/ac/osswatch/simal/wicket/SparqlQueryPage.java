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

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;

import uk.ac.osswatch.simal.model.jena.SparqlResult;
import uk.ac.osswatch.simal.model.jena.simal.JenaSimalRepository;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.data.SparqlQueryInputModel;
import uk.ac.osswatch.simal.wicket.doap.DoapFormPage;

import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 * A page for querying the RDF backend using SPARQL
 * 
 * @REFACTOR use an existing SPARQL endpoint provider - see ISSUE 116
 * 
 */
public class SparqlQueryPage extends BasePage {

  private static final String SPARQL_QUERY_STRING_FIELD = "sparqlQueryString";

  private static final String QUERY_PREFIX = " PREFIX  xsd:  <http://www.w3.org/2001/XMLSchema#> \n"
      + " PREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
      + " PREFIX  doap: <http://usefulinc.com/ns/doap#> \n"
      + " PREFIX  simal: <http://oss-watch.ac.uk/ns/0.2/simal#> \n\n";

  private final ListView<ListItem<String>> queryListView;

  private static final List<List<String>> queryList = new Vector<List<String>>();

  /**
   * Create default SPARQL query page
   */
  @SuppressWarnings("unchecked")
  public SparqlQueryPage(PageParameters parameters) {
    super();

    add(new SparqlQueryForm("sparqlQueryForm"));

    add(queryListView = new ListView("queryResults", queryList) {
      private static final long serialVersionUID = 2012355500166601921L;

      public void populateItem(final ListItem listItem) {
        final List<String> resultRow = (List<String>) listItem.getModelObject();
        listItem.add(new Label("queryResult", processResultRow(resultRow)));
      }

      /**
       * TODO Query result representation should be a table but don't know how
       * to create a table with a dynamic nr. of columns in Wicket
       * 
       * @param resultRow
       * @return
       */
      private String processResultRow(List<String> resultRow) {
        String result = "";
        for (String cell : resultRow) {
          if (!"".equals(result)) {
            result += ", ";
          }
          result += cell;
        }
        return result;
      }
    });
  }

  /**
   * Query the backend db with the queryStr and return result as received from
   * backend.
   * 
   * @param queryStr
   * @return
   */
  private SparqlResult getSparqlQueryResults(String queryStr) {
    String sparqlQuery = StringEscapeUtils.unescapeXml(queryStr);
    // @REFACTOR the UI should not be aware of the back end implementation, we need a generic result interface for queries
    SparqlResult result = null;
    try {
      ISimalRepository repo = UserApplication.getRepository();
      // @REFACTOR the UI application should not be aware of the back end implementation.
      if (repo instanceof JenaSimalRepository) {
        result = ((JenaSimalRepository) repo).getSparqlQueryResult(sparqlQuery);
      }

    } catch (SimalRepositoryException e) {
      setResponsePage(new ErrorReportPage(new UserReportableException(
          "Unable to add doap using RDF supplied", DoapFormPage.class, e)));
    }
    return result;
  }

  /**
   * Update display list with new query result.
   * 
   * @param sparqlResults
   */
  private void processQueryResults(SparqlResult sparqlResults) {
    queryList.clear();
    queryList.add(sparqlResults.getVarNames());
    Iterator<List<RDFNode>> resultIter = sparqlResults.getResultsIterator();
    while (resultIter.hasNext()) {
      List<RDFNode> result = resultIter.next();
      List<String> displayRow = new Vector<String>();
      for (RDFNode cell : result) {
        displayRow.add(cell.toString());
      }
      queryList.add(displayRow);
    }
  }

  /**
   * Form for entering the SPARQL query in the web page.
   * 
   */
  private class SparqlQueryForm extends Form<SparqlQueryInputModel> {
    private static final long serialVersionUID = 1005521653425155426L;

    private final SparqlQueryInputModel inputModel = new SparqlQueryInputModel();

    public SparqlQueryForm(String id) {
      super(id);
      TextArea<SparqlQueryInputModel> sparqlQueryField = new TextArea<SparqlQueryInputModel>(
          SPARQL_QUERY_STRING_FIELD, new PropertyModel<SparqlQueryInputModel>(
              inputModel, SPARQL_QUERY_STRING_FIELD));
      sparqlQueryField.add(new FocusBehaviour());
      String[] defaultValue = { QUERY_PREFIX };
      sparqlQueryField.setModelValue(defaultValue);
      add(sparqlQueryField);
    }

    @Override
    protected void onSubmit() {
      super.onSubmit();
      String sparqlQuery = inputModel.getSparqlQueryString();
      if (sparqlQuery != null && !"".equals(sparqlQuery)) {
        SparqlResult sparqlResults = getSparqlQueryResults(sparqlQuery);
        processQueryResults(sparqlResults);
        queryListView.modelChanged();
      }
    }

  }
}

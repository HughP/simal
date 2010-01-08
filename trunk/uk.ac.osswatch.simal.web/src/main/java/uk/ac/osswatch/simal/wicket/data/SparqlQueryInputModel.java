/*
 * Copyright 2010 University of Oxford
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
package uk.ac.osswatch.simal.wicket.data;

import org.apache.wicket.IClusterable;

/**
 * Provide inputs for the SPARQL query page
 * @author svanderwaal
 *
 */
public class SparqlQueryInputModel implements IClusterable {

  private static final long serialVersionUID = -638867731076925909L;

  private String sparqlQueryString;

  public void setSparqlQueryString(String sparqlQueryString) {
    this.sparqlQueryString = sparqlQueryString;
  }

  public String getSparqlQueryString() {
    return sparqlQueryString;
  }
}

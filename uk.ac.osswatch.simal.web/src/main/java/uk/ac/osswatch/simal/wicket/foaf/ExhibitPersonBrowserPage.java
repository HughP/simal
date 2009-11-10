package uk.ac.osswatch.simal.wicket.foaf;

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

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.UserApplication;

/**
 * Creates a page which contains an <a
 * href="http://simile.mit.edu/wiki/Exhibit">Exhibit 2.0</a> Browser. This is a
 * faceted browser for People.
 */
public class ExhibitPersonBrowserPage extends BasePage {
  private static final long serialVersionUID = 1L;

  private static final CompressedResourceReference EXHIBIT_CSS = new CompressedResourceReference(
      BasePage.class, "style/exhibit.css");

  public ExhibitPersonBrowserPage() {
    add(HeaderContributor.forCss(EXHIBIT_CSS));
    add(HeaderContributor
        .forJavaScript("http://static.simile.mit.edu/exhibit/api-2.0/exhibit-api.js"));
    StringBuilder jsonLink = new StringBuilder("<link href=\"");
    jsonLink.append(UserApplication.get().getServletContext().getContextPath());
    jsonLink = jsonLink.append("/simal-rest/allPeople/json\" rel=\"exhibit/data");
    jsonLink.append(" rel=\"exhibit/data\" />");
    add(new StringHeaderContributor(jsonLink.toString()));
  }
}

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

import java.io.File;
import java.io.FileWriter;
import java.net.URL;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserApplication;
import uk.ac.osswatch.simal.wicket.UserReportableException;

/**
 * Creates a page which contains an <a
 * href="http://simile.mit.edu/wiki/Exhibit">Exhibit 2.0</a> Browser. This is a
 * faceted browser for People.
 */
public class PersonBrowserPage extends BasePage {

  private static final CompressedResourceReference EXHIBIT_CSS = new CompressedResourceReference(
      BasePage.class, "style/exhibit.css");

  public PersonBrowserPage() {
    URL dir = UserApplication.class.getResource(DEFAULT_CSS_LOC);
    try {
      File outFile = new File(new File(dir.toURI()).getParent() + File.separator + "people.js");
      FileWriter out = new FileWriter(outFile);
      out.write(UserApplication.getRepository().getAllPeopleAsJSON());
      out.close();
    } catch (Exception e) {
      UserReportableException error = new UserReportableException("Unable to write JSON file", PersonBrowserPage.class, e);
      setResponsePage(new ErrorReportPage(error));
    }
    add(HeaderContributor.forCss(EXHIBIT_CSS));
    add(HeaderContributor
        .forJavaScript("http://static.simile.mit.edu/exhibit/api-2.0/exhibit-api.js"));
    add(new StringHeaderContributor(
        "<link href=\"/resources/uk.ac.osswatch.simal.wicket.UserApplication/style/people.js\" type=\"application/json\" rel=\"exhibit/data\" />"));
  }
}


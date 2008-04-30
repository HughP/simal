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


import org.apache.wicket.markup.html.WebPage;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.panel.PersonListPanel;

/**
 * This page provides facilities to manage people stored 
 * in the simal repository.
 */
public class PersonAdminPage extends BasePage {

  public PersonAdminPage() {
    try {
      add(new PersonListPanel("personList"));
    } catch (SimalRepositoryException e) {
      UserReportableException error = new UserReportableException(
          "Unable to get people from the repository",
          PersonAdminPage.class, e);
      setResponsePage(new ErrorReportPage(error));
    }
  }
}


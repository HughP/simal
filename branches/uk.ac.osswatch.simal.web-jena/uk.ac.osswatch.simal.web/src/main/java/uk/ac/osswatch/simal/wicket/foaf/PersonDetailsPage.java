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

import org.apache.wicket.markup.html.basic.Label;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserApplication;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.panel.PersonSummaryPanel;

/**
 * A page for displaying the details of a single person.
 */
public class PersonDetailsPage extends BasePage {
  private static final long serialVersionUID = -2362335968055139016L;
  
  public PersonDetailsPage() {
    IPerson person;
    try {
      person = UserApplication.getRepository().getPerson(
          UserApplication.DEFAULT_PERSON_URI);
      populatePage(person);
    } catch (SimalRepositoryException e) {
      UserReportableException error = new UserReportableException(
          "Unable to get person from the repository",
          PersonDetailsPage.class, e);
      setResponsePage(new ErrorReportPage(error));
    }
  }
  public PersonDetailsPage(IPerson person) {
    populatePage(person);
  }

  private void populatePage(IPerson person) {
    add(new PersonSummaryPanel("summary", person));
  }
}


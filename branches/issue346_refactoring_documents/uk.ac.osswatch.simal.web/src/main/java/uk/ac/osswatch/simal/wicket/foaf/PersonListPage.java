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


import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.panel.PersonListPanel;

/**
 * Creates a page which contains an <a
 * href="http://simile.mit.edu/wiki/Exhibit">Exhibit 2.0</a> Browser. This is a
 * faceted browser for People.
 */
public class PersonListPage extends BasePage {
  private static final long serialVersionUID = 1L;

  public PersonListPage() {
	  try {
		add(new PersonListPanel("personList", "Person List", 15));
	} catch (SimalRepositoryException e) {
	      UserReportableException error = new UserReportableException(
	              "Unable to list people in the repository",
	              PersonListPage.class, e);
	          setResponsePage(new ErrorReportPage(error));
	}
  }
}

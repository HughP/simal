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

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.IPageLink;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rest.RESTCommand;
import uk.ac.osswatch.simal.rest.SimalAPIException;
import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserApplication;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.panel.ColleaguesPanel;
import uk.ac.osswatch.simal.wicket.panel.PersonSummaryPanel;
import uk.ac.osswatch.simal.wicket.panel.ProjectListPanel;

/**
 * A page for displaying the details of a single person.
 */
public class PersonDetailPage extends BasePage {
  private static final long serialVersionUID = -2362335968055139016L;
  IPerson person;

  public PersonDetailPage(PageParameters parameters) {
    String id = null;
    if (parameters.containsKey("simalID")) {
      id = parameters.getString("simalID");

      try {
        person = UserApplication.getRepository().findPersonById(id);
        populatePage(person);
      } catch (SimalRepositoryException e) {
        UserReportableException error = new UserReportableException(
            "Unable to get person from the repository", PersonDetailPage.class,
            e);
        setResponsePage(new ErrorReportPage(error));
      }
    } else {
      UserReportableException error = new UserReportableException(
          "Unable to get simalID parameter from URL", PersonDetailPage.class);
      setResponsePage(new ErrorReportPage(error));
    }
  }

  public PersonDetailPage(IPerson person) {
    try {
      populatePage(person);
    } catch (SimalRepositoryException e) {
      UserReportableException error = new UserReportableException(
          "Unable to populate person detail page", PersonDetailPage.class, e);
      setResponsePage(new ErrorReportPage(error));
    }
  }

  private void populatePage(IPerson person) throws SimalRepositoryException {
    add(new PersonSummaryPanel("summary", person));
    add(new ColleaguesPanel("colleagues", person));
    add(new ProjectListPanel("projects", person.getProjects()));

    // source
    add(getRepeatingDataSourcePanel("sources", "seeAlso", person.getSources()));

    // FOAF link
    try {
      RESTCommand cmd = RESTCommand.createGetPerson(person.getSimalID(),
          RESTCommand.TYPE_SIMAL, RESTCommand.FORMAT_XML);
      add(new ExternalLink("rdfLink", cmd.getURL()));
      String rdfLink = "<link href=\"" + cmd.getURL()
          + "\" rel=\"meta\" title=\"RDF\" type=\"application/rdf+xml\" />";
      add(new StringHeaderContributor(rdfLink));
    } catch (SimalAPIException e) {
      UserReportableException error = new UserReportableException(
          "Unable to get a RESTful URI for the person RDF/XML document",
          PersonDetailPage.class, e);
      setResponsePage(new ErrorReportPage(error));
    }
  }

  /**
   * Get a link to a PersonDetailPage for a person.
   * 
   * @param person
   *          the person we want a detail page link for
   * @return
   */
  @SuppressWarnings("serial")
  public static IPageLink getLink(final IPerson person) {
    IPageLink link = new IPageLink() {
      public Page getPage() {
        return new PersonDetailPage(person);
      }

      public Class<PersonDetailPage> getPageIdentity() {
        return PersonDetailPage.class;
      }
    };
    return link;
  }
}

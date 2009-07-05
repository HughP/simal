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

import java.security.NoSuchAlgorithmException;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.IPageLink;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;
import uk.ac.osswatch.simal.rest.RESTCommand;
import uk.ac.osswatch.simal.rest.SimalAPIException;
import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
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

  /**
   * Allowed parameters in the URL are:
   * 
   * <ul>
   *   <li>id - a simal ID for a person</li>
   *   <li>email - a persons email address</li>
   * </ul>
   * 
   * If no person can be found using the supplied parameters then the person list page is displayed instead
   * of the person detail page. The person list page has the ability to search the repository.
   * 
   * @param parameters
   */
  public PersonDetailPage(PageParameters parameters) {
    String id = null;
    try {
	    if (parameters.containsKey("simalID")) {
	        id = SimalRepositoryFactory.getInstance().getUniqueSimalID(
	            parameters.getString("simalID"));
	        person = SimalRepositoryFactory.getPersonService().findById(id);
	    } else if (parameters.containsKey("email")) {
	    	String[] emails = (String[]) parameters.get("email");
	    	String email = emails[0];
	    	if (email == null || email.length() == 0) {
	  	      UserReportableException error = new UserReportableException(
	  		          "Must provide an email in the request URL", PersonDetailPage.class);
	  		  setResponsePage(new ErrorReportPage(error));
	    	}
	    	person = SimalRepositoryFactory.getPersonService().findBySha1Sum(RDFUtils.getSHA1(email));
	    } else {
	      UserReportableException error = new UserReportableException(
	          "URL does not have sufficient parameters for finding a unique person", PersonDetailPage.class);
	      setResponsePage(new ErrorReportPage(error));
	    }
	    
	    if (person == null) {
		      setResponsePage(new PersonListPage());
	    } else {
	        populatePage(person);
	    }
    } catch (SimalRepositoryException e) {
      UserReportableException error = new UserReportableException(
        "Unable to get person from the repository", PersonDetailPage.class,
        e);
      setResponsePage(new ErrorReportPage(error));
    } catch (NoSuchAlgorithmException e) {
        UserReportableException error = new UserReportableException(
                "Unable to calculate SHA1 has for email", PersonDetailPage.class,
                e);
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
    add(new ProjectListPanel("projects", person.getProjects(), 15));

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

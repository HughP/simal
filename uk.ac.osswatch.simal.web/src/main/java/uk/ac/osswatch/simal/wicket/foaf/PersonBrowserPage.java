package uk.ac.osswatch.simal.wicket.foaf;
/*
 * Copyright 2009 University of Oxford
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

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.panel.PersonListPanel;

/**
 * A simple person browser page. List people, pages through them and allows simple searching of the
 * list. This page can be used to show any arbitrary list of people. Simply instantiate with using
 * the empty constructor to display all people, or supply a set of people to be used.
 */
public class PersonBrowserPage extends BasePage {

	public PersonBrowserPage() {
        populatePage("");
	}
	
	/**
	 * Create a new project list page using the supplied parameters. Legal parameters are:
	 * 
	 * <ul>
	 * <li>filter: a filter to be applied</li>
	 * </ul>
	 * 
	 * @param parameters
	 */
	public PersonBrowserPage(PageParameters parameters) {
		if (parameters.containsKey("filter")) {
	      populatePage(parameters.getString("filter"));
		} else {
	      UserReportableException error = new UserReportableException(
		          "Invalid parameters", PersonListPage.class);
		  setResponsePage(new ErrorReportPage(error));
	    }
	}
	
	public void populatePage(String filter) {
		try {
			add(new PersonListPanel("personList", "People", 15, filter));
			
			add(new BookmarkablePageLink<PersonBrowserPage>("filterWithALink", PersonBrowserPage.class, new PageParameters("filter=^a(.*)")));
		    add(new BookmarkablePageLink<PersonBrowserPage>("filterWithBLink", PersonBrowserPage.class, new PageParameters("filter=^b(.*)")));
		    add(new BookmarkablePageLink<PersonBrowserPage>("filterWithCLink", PersonBrowserPage.class, new PageParameters("filter=^c(.*)")));
		    add(new BookmarkablePageLink<PersonBrowserPage>("filterWithDLink", PersonBrowserPage.class, new PageParameters("filter=^d(.*)")));
		    add(new BookmarkablePageLink<PersonBrowserPage>("filterWithELink", PersonBrowserPage.class, new PageParameters("filter=^e(.*)")));
		    add(new BookmarkablePageLink<PersonBrowserPage>("filterWithFLink", PersonBrowserPage.class, new PageParameters("filter=^f(.*)")));
		    add(new BookmarkablePageLink<PersonBrowserPage>("filterWithGLink", PersonBrowserPage.class, new PageParameters("filter=^g(.*)")));
		    add(new BookmarkablePageLink<PersonBrowserPage>("filterWithHLink", PersonBrowserPage.class, new PageParameters("filter=^h(.*)")));
		    add(new BookmarkablePageLink<PersonBrowserPage>("filterWithILink", PersonBrowserPage.class, new PageParameters("filter=^i(.*)")));
		    add(new BookmarkablePageLink<PersonBrowserPage>("filterWithJLink", PersonBrowserPage.class, new PageParameters("filter=^j(.*)")));
		    add(new BookmarkablePageLink<PersonBrowserPage>("filterWithKLink", PersonBrowserPage.class, new PageParameters("filter=^k(.*)")));
		    add(new BookmarkablePageLink<PersonBrowserPage>("filterWithLLink", PersonBrowserPage.class, new PageParameters("filter=^l(.*)")));
		    add(new BookmarkablePageLink<PersonBrowserPage>("filterWithMLink", PersonBrowserPage.class, new PageParameters("filter=^m(.*)")));
		    add(new BookmarkablePageLink<PersonBrowserPage>("filterWithNLink", PersonBrowserPage.class, new PageParameters("filter=^n(.*)")));
		    add(new BookmarkablePageLink<PersonBrowserPage>("filterWithOLink", PersonBrowserPage.class, new PageParameters("filter=^o(.*)")));
		    add(new BookmarkablePageLink<PersonBrowserPage>("filterWithPLink", PersonBrowserPage.class, new PageParameters("filter=^p(.*)")));
		    add(new BookmarkablePageLink<PersonBrowserPage>("filterWithQLink", PersonBrowserPage.class, new PageParameters("filter=^q(.*)")));
		    add(new BookmarkablePageLink<PersonBrowserPage>("filterWithRLink", PersonBrowserPage.class, new PageParameters("filter=^r(.*)")));
		    add(new BookmarkablePageLink<PersonBrowserPage>("filterWithSLink", PersonBrowserPage.class, new PageParameters("filter=^s(.*)")));
		    add(new BookmarkablePageLink<PersonBrowserPage>("filterWithTLink", PersonBrowserPage.class, new PageParameters("filter=^t(.*)")));
		    add(new BookmarkablePageLink<PersonBrowserPage>("filterWithULink", PersonBrowserPage.class, new PageParameters("filter=^u(.*)")));
		    add(new BookmarkablePageLink<PersonBrowserPage>("filterWithVLink", PersonBrowserPage.class, new PageParameters("filter=^v(.*)")));
		    add(new BookmarkablePageLink<PersonBrowserPage>("filterWithWLink", PersonBrowserPage.class, new PageParameters("filter=^w(.*)")));
		    add(new BookmarkablePageLink<PersonBrowserPage>("filterWithXYZLink", PersonBrowserPage.class, new PageParameters("filter=^[xyz](.*)")));
	    } catch (SimalRepositoryException e) {
		      UserReportableException error = new UserReportableException(
			          "Unable to retrieve people to list", PersonListPage.class);
			  setResponsePage(new ErrorReportPage(error));
		}
	}
}


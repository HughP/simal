package uk.ac.osswatch.simal.wicket.panel;
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
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserHomePage;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.doap.ProjectDetailPage;
import uk.ac.osswatch.simal.wicket.foaf.PersonDetailPage;


/**
 * A panel for displaying project information.
 */
public class PersonSummaryPanel extends Panel {
	private static final long serialVersionUID = -6078043900380156791L;
  private static final Logger logger = LoggerFactory.getLogger(PersonSummaryPanel.class);
  private IPerson person;

	/**
	 * Create a summary page for a specific person.
	 * 
	 * @param panelID
	 * @param person
	 *            the person to display in this panel
	 * @throws SimalRepositoryException 
	 */
	public PersonSummaryPanel(String panelId, IPerson person) throws SimalRepositoryException {
		super(panelId);
		populatePage(person);
  }

	private void populatePage(final IPerson person) throws SimalRepositoryException {
	  this.person = person;
	  PageLink detailLink = new PageLink("detailLink", PersonDetailPage.getLink(person));
    detailLink.add(new Label("personName", person.getLabel()));
    add(detailLink);
		add(new Label("emails", person.getEmail().toString()));
		add(new Label("homepages", person.getHomepages().toString()));
		String friendsURL = SimalProperties.getProperty(SimalProperties.PROPERTY_REST_BASEURL) + "/allColleagues/person-" + person.getSimalID() + "/xml";
		add(new ExternalLink("friendsLink", friendsURL));


    add(new Link("removePersonActionLink") {
      private static final long serialVersionUID = -5596547501782436339L;

      public void onClick() {
        try {
          removePerson();
          if (this.getPage() instanceof ProjectDetailPage) {
            setResponsePage(new ProjectDetailPage(((ProjectDetailPage)this.getPage()).getProject()));
          } else {
            logger.warn("Removing person from an unknown page, returning to user home page");
            setResponsePage(new UserHomePage());
          }
        } catch (UserReportableException e) {
          setResponsePage(new ErrorReportPage(e));
        }
      }
    });
	}

  private void removePerson() throws UserReportableException{
    Page page = this.getPage();
    if (page instanceof ProjectDetailPage) {
      IProject project = ((ProjectDetailPage)page).getProject();
      try {
        project.removeDocumenter(person);
        project.removeDeveloper(person);
        project.removeHelper(person);
        project.removeMaintainer(person);
        project.removeTester(person);
        project.removeTranslator(person);
      } catch (SimalRepositoryException e) {
        throw new UserReportableException("Unable to removePerson", PersonSummaryPanel.class, e);
      }
    } else {
      throw new UserReportableException("Unable to removePerson when parent page is type " + page.getClass().getName(), PersonSummaryPanel.class);
    }
  }

}

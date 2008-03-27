/*
 * Copyright 2007 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.osswatch.simal.wicket;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rest.RESTCommand;
import uk.ac.osswatch.simal.wicket.panel.ProjectListPanel;
import uk.ac.osswatch.simal.wicket.panel.ProjectSummaryPanel;

public class UserHomePage extends OpenSocialPage {
	private static final long serialVersionUID = -8125606657250912738L;
	protected static final String MYEXPERIMENT_FRIENDS_GADGET_URL = "http://localhost:8080/gadgets/myExperimentFriendsNavigator.xml";
  protected static final String FRIENDS_NAVIGATOR_GADGET_URL = "http://localhost:8080/gadgets/friendsNavigator.xml";
  
  public UserHomePage() {
		try {
			add(new ProjectListPanel("projectList"));
			add(new ProjectSummaryPanel("featuredProject", UserApplication
					.getRepository().getProject(UserApplication.DEFAULT_PROJECT_QNAME)));
			addGadget("friendsNavigatorGadget", FRIENDS_NAVIGATOR_GADGET_URL, RESTCommand.SOURCE_TYPE_SIMAL);
			addGadget("myExperimentFriends", MYEXPERIMENT_FRIENDS_GADGET_URL, RESTCommand.SOURCE_TYPE_MYEXPERIMENT);
		} catch (SimalRepositoryException e) {
			UserReportableException error = new UserReportableException("Unable to get project from the repository", ExhibitProjectBrowserPage.class, e);
			setResponsePage(new ErrorReportPage(error));
		}
	}
}

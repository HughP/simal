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
package uk.ac.osswatch.simal.wicket.panel;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.Panel;

import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.doap.ProjectDetailPage;

/**
 * A panel for displaying project information.
 */
public class ProjectSummaryPanel extends Panel {
	private static final long serialVersionUID = -6078043900380156791L;

	/**
	 * Create a summary page for a specific project.
	 * 
	 * @param panelID
	 * @param project
	 *            the project to display in this panel
	 */
	public ProjectSummaryPanel(String panelID, IProject project) {
		super(panelID);
		populatePage(project);
	}

	@SuppressWarnings("serial")
	private void populatePage(final IProject project) {
		if (project != null) {
			add(new Label("widgetTitle", "Featured Project"));
			add(new Label("projectName", project.getName()));
			add(new Label("shortDesc", project.getShortDesc()));
			add(new PageLink("projectDetailLink", ProjectDetailPage.getLink(project)));
		} else {
			String msg = "Requested project does not exist in the repository";

			add(new Label("widgetTitle", "Featured Project"));
			add(new Label("projectName", "Error"));
			add(new Label("shortDesc", msg));

			final UserReportableException exception = new UserReportableException(msg, ProjectSummaryPanel.class);
			IPageLink link = new IPageLink() {
				public Page getPage() {
					return new ErrorReportPage(exception);
				}

				public Class<ErrorReportPage> getPageIdentity() {
					return ErrorReportPage.class;
				}
			};
			add(new PageLink("projectDetailLink", link));

		}
	}

}

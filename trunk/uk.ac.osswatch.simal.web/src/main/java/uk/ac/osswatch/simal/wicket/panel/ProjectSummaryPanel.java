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

import javax.xml.namespace.QName;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import uk.ac.osswatch.simal.model.elmo.Project;
import uk.ac.osswatch.simal.wicket.UserApplication;

/**
 * A panel for displaying project information.
 */
public class ProjectSummaryPanel extends Panel {
	private static final long serialVersionUID = -6078043900380156791L;

	/**
	 * Create a summary panel for a specific project.
	 * 
	 * @param id
	 * @param qname
	 */
	public ProjectSummaryPanel(String id, QName qname) {
		super(id);
		try {
			Project project = UserApplication.getRepository().getProject(qname);

			if (project != null) {
				add(new Label("widgetTitle", "Featured Project"));
				add(new Label("projectName", project.getName()));
				add(new Label("shortDesc", project.getShortDesc()));
			} else {
				add(new Label("widgetTitle", "Featured Project"));
				add(new Label("projectName", "Error"));
				add(new Label("shortDesc", "Requested project QName '" + qname
						+ "' does not exist in the repository"));
			}
		} finally {
			// UserApp.getRepository().close();
		}
	}

}

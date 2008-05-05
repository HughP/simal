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
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;

import uk.ac.osswatch.simal.model.IPerson;

/**
 * A panel for displaying project information.
 */
public class PersonSummaryPanel extends Panel {
	private static final long serialVersionUID = -6078043900380156791L;

	/**
	 * Create a summary page for a specific person.
	 * 
	 * @param panelID
	 * @param person
	 *            the person to display in this panel
	 */
	public PersonSummaryPanel(String panelId, IPerson person) {
		super(panelId);
		populatePage(person);
	}

	private void populatePage(final IPerson person) {
		add(new Label("personName", person.getLabel()));
		add(new Label("personId", person.getSimalId()));
		add(new Label("emails", person.getFoafMboxes().toString()));
		add(new Label("homepages", person.getFoafHomepages().toString()));
		String friendsURL = "http://localhost:8080/simal-rest/allColleagues/person-" + person.getSimalId() + "/xml";
		add(new ExternalLink("friendsLink", friendsURL));
	}

}

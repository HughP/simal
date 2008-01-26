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

import javax.xml.namespace.QName;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

import uk.ac.osswatch.simal.wicket.panel.ProjectSummaryPanel;

public class UserHomePage extends WebPage {
	private static final long serialVersionUID = -8125606657250912738L;
	
	private static final CompressedResourceReference DEFAULT_CSS = new CompressedResourceReference(UserApplication.class, "default.css");
	
	public UserHomePage() {
		add(HeaderContributor.forCss( DEFAULT_CSS ));
		add(new Label("message", "It works, but it doesn't do much yet."));
		add(new ProjectSummaryPanel("featuredProject", new QName("http://simal.oss-watch.ac.uk/simalTest#")));
	}
}

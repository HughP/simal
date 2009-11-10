package uk.ac.osswatch.simal.wicket.doap;
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

import java.util.Set;

import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.foaf.PersonBrowserPage;
import uk.ac.osswatch.simal.wicket.panel.ProjectListPanel;

/**
 * A simple project browser page. List projects, pages through them and allows simple searching of the
 * list. This page can be used to show any arbitrary list of projects. Simply instantiate with using
 * the empty constructor to display all projects, or supply a set of projects to be used.
 */
public class ProjectBrowserPage extends BasePage {

	/**
	 * Create a default page that lists all projects in the repository.
	 */
	public ProjectBrowserPage() {
	    try {
	        add(new ProjectListPanel("projectList", 15));
	      } catch (SimalRepositoryException e) {
	        UserReportableException error = new UserReportableException(
	            "Unable to get projects from the repository",
	            PersonBrowserPage.class, e);
	        setResponsePage(new ErrorReportPage(error));
	      }
	}

	/**
	 * Create a page that lists only the projects supplied.
	 */
	public ProjectBrowserPage(Set<IProject> projects) {
	    try {
	        add(new ProjectListPanel("projectList", projects, 15));
	      } catch (SimalRepositoryException e) {
	        UserReportableException error = new UserReportableException(
	            "Unable to get projects from the repository",
	            PersonBrowserPage.class, e);
	        setResponsePage(new ErrorReportPage(error));
	      }
	}
}


package uk.ac.osswatch.simal.wicket.doap;

/*
 * Copyright 2008-9 University of Oxford
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

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserApplication;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.panel.CategorySummaryPanel;
import uk.ac.osswatch.simal.wicket.panel.PersonListPanel;
import uk.ac.osswatch.simal.wicket.panel.ProjectListPanel;

/**
 * Displays a single category in detail.
 */
public class CategoryDetailPage extends BasePage {
  private static final long serialVersionUID = 1L;
  IDoapCategory category;

  public CategoryDetailPage(IDoapCategory category) {
    try {
      populatePage(category);
    } catch (SimalRepositoryException e) {
      UserReportableException error = new UserReportableException(
          "Unable to create category detail page", CategoryDetailPage.class, e);
      setResponsePage(new ErrorReportPage(error));
    }
  }

  public CategoryDetailPage(PageParameters parameters) {
    String id = null;
    if (parameters.containsKey("simalID")) {
      id = parameters.getString("simalID");

      try {
        category = SimalRepositoryFactory.getCategoryService().findById(id);
        populatePage(category);
      } catch (SimalRepositoryException e) {
        UserReportableException error = new UserReportableException(
            "Unable to get category from the repository",
            CategoryDetailPage.class, e);
        setResponsePage(new ErrorReportPage(error));
      }
    } else {
      UserReportableException error = new UserReportableException(
          "Unable to get simalID parameter from URL", CategoryDetailPage.class);
      setResponsePage(new ErrorReportPage(error));
    }
  }

  private void populatePage(IDoapCategory category)
      throws SimalRepositoryException {
    add(new CategorySummaryPanel("summary", category));
    add(new ProjectListPanel("projectList", category.getProjects(), 15));
    add(new PersonListPanel("peopleList", "People working in this category", category.getPeople(), 15));
  }
}

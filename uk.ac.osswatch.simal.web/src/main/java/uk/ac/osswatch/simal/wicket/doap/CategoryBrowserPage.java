package uk.ac.osswatch.simal.wicket.doap;
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


import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.panel.CategoryListPanel;

/**
 * This page provides facilities to browse stored 
 * in the simal repository.
 */
public class CategoryBrowserPage extends BasePage {
  private static final long serialVersionUID = -3418218005629173956L;

  public CategoryBrowserPage() {
    try {
      add(new CategoryListPanel("categoryList"));
    } catch (SimalRepositoryException e) {
      UserReportableException error = new UserReportableException(
          "Unable to get categories from the repository",
          CategoryBrowserPage.class, e);
      setResponsePage(new ErrorReportPage(error));
    }
  }
}


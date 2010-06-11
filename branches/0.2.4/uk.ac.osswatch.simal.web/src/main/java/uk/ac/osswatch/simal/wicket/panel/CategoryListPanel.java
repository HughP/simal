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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.data.SortableCategoryDataProvider;
import uk.ac.osswatch.simal.wicket.markup.html.repeater.data.table.LinkPropertyColumn;

/**
 * A panel for listing categories. This panel allows the user to navigate the
 * people the Simal repository and, optionally, allows some manipulation of
 * those records.
 */
public class CategoryListPanel extends Panel {
  private static final long serialVersionUID = -7641153470731218965L;

  private Set<IDoapCategory> categories;

  public CategoryListPanel(String id) throws SimalRepositoryException {
    super(id);
    this.categories = SimalRepositoryFactory.getCategoryService().getAll();
    SortableDataProvider<IResource> dataProvider = new SortableCategoryDataProvider(
        this.categories);
    addCategoryList(dataProvider);
  }

  public CategoryListPanel(String id, Set<IDoapCategory> categories) {
    super(id);
    this.categories = categories;
    SortableDataProvider<IResource> dataProvider = new SortableCategoryDataProvider(
        categories);
    addCategoryList(dataProvider);
  }

  @SuppressWarnings("unchecked")
  private void addCategoryList(SortableDataProvider<IResource> dataProvider) {
    List<AbstractColumn> columns = new ArrayList<AbstractColumn>();
    columns.add(new LinkPropertyColumn(new Model<String>("Name"), "name",
        "name"));
    columns.add(new PropertyColumn(new Model("Projects"), "projects",
        "projects.size"));
    columns
        .add(new PropertyColumn(new Model("People"), "people", "people.size"));

    dataProvider.setSort(SortableCategoryDataProvider.SORT_PROPERTY_NAME, true);
    add(new AjaxFallbackDefaultDataTable("dataTable", columns, dataProvider, 15));
  }

  /**
   * Add a new category to the list. Useful when new categories are added after
   * the page has loaded.
   * 
   * @param category
   */
  public void addCategory(IDoapCategory category) {
    this.categories.add(category);
  }

}

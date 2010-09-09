package uk.ac.osswatch.simal.wicket.panel.project;

/*
 * Copyright 2010 University of Oxford
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
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import uk.ac.osswatch.simal.model.IDoapResource;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.data.SortableDoapResourceDataProvider;
import uk.ac.osswatch.simal.wicket.markup.html.repeater.data.table.LinkPropertyColumn;

/**
 * A simple panel for listing a set of any IDoapResources. 
 */
public class GenericIResourceSetPanel extends Panel {
  private static final long serialVersionUID = -932080365392667144L;
  private Set<IDoapResource> resources;
  private String title;
  private SortableDoapResourceDataProvider<IDoapResource> dataProvider;
  private IProject project;

  /**
   * Create a panel that lists all homepages in the repository.
   * 
   * @param id
   *          the wicket ID for the component
   * @param title
   *          the title if this list
   * @param numberOfPages
   *          the number of homepages to display per page
   * @throws SimalRepositoryException
   */
  public GenericIResourceSetPanel(String id, String title,
      Set<? extends IDoapResource> resources, int numberOfPages,
      IProject project) {
    this(id, title, resources, numberOfPages);
    this.project = project;
  }

  /**
   * Create a panel that lists a set of pages.
   * 
   * @param id
   *          the wicket ID for the component
   * @param title
   *          the title if this list
   * @param pages
   *          the pages to include in the list
   * @param numberOfPages
   *          the number of homepages to display per page
   * @throws SimalRepositoryException
   */
  @SuppressWarnings("unchecked")
  public GenericIResourceSetPanel(String id, String title,
      Set<? extends IDoapResource> resources, int numberOfPages) {
    super(id);
    this.title = title;
    this.resources = (Set<IDoapResource>) resources;
    populatePanel(numberOfPages);
  }

  private void populatePanel(int numberOfPages) {
    add(new Label("title", title));
    addResourcesList(resources, numberOfPages);
    add(new AddIResourcePanel("addWebsitePanel", this));
    setOutputMarkupId(true);
  }

  @SuppressWarnings("unchecked")
  private void addResourcesList(Set<? extends IDoapResource> pages,
      int numberOfPages) {
    List<IColumn<?>> columns = new ArrayList<IColumn<?>>();
    columns.add(new LinkPropertyColumn(new Model<String>("Name"), "label",
        "label"));
    columns.add(new PropertyColumn<IDoapResource>(new Model<String>("URL"),
        "url", "url") {
      private static final long serialVersionUID = -3063052337733586041L;

      @Override
      public void populateItem(Item<ICellPopulator<IDoapResource>> cellItem,
          String componentId, IModel<IDoapResource> model) {
        String label = "";

        if (model != null) {
          label = model.getObject().getURI();
        }
        cellItem.add(new Label(componentId, label));
      }
    });

    dataProvider = new SortableDoapResourceDataProvider<IDoapResource>(pages);
    dataProvider.setSort(SortableDoapResourceDataProvider.SORT_PROPERTY_NAME,
        true);
    add(new AjaxFallbackDefaultDataTable("dataTable", columns, dataProvider,
        numberOfPages));
  }

  /**
   * Add a resource to the list being displayed. This method does not add the
   * resource to the underlying data storage mechanism, it only adds it to the
   * GUI.
   * 
   * @param iDoapResource
   */
  public void add(IDoapResource iDoapResource) {
    this.resources.add(iDoapResource);
  }

  public void processAdd(IDoapResourceFormInputModel inputModel)
      throws SimalRepositoryException {
    // Subclasses that wish to use it should implement it.
    // TODO To be made abstract to force subclassing and implementation of
    // method
  }

  protected IProject getProject() {
    return this.project;
  }
}

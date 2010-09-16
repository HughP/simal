package uk.ac.osswatch.simal.wicket.panel.homepage;

/*
 * Copyright 2008-2010 University of Oxford
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
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.data.SortableDoapResourceDataProvider;
import uk.ac.osswatch.simal.wicket.markup.html.repeater.data.table.LinkPropertyColumn;

/**
 * A panel for listing homepages. This is a simple panel that just lists
 * a set of homepages.
 */
public class HomepageListPanel extends Panel {
  private static final long serialVersionUID = 1L;
  private static final Logger logger = LoggerFactory
      .getLogger(HomepageListPanel.class);
  private Set<IDoapHomepage> pages;
  private String title;
  SortableDoapResourceDataProvider dataProvider;

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
  public HomepageListPanel(String id, String title, int numberOfPages)
      throws SimalRepositoryException {
    super(id);
    this.title = title;
    this.pages = SimalRepositoryFactory.getHomepageService().getAll();
    populatePanel(numberOfPages);
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
  public HomepageListPanel(String id, String title, Set<IDoapHomepage> pages,
      int numberOfPages) {
    super(id);
    this.title = title;
    this.pages = pages;
    populatePanel(numberOfPages);
  }

  private void populatePanel(int numberOfPages) {
    add(new Label("title", title));
    addPagesList(pages, numberOfPages);
    add(new AddWebsitePanel("addWebsitePanel", null, this));
    
  }

  private void addPagesList(Set<IDoapHomepage> pages, int numberOfPages) {
    List<AbstractColumn> columns = new ArrayList<AbstractColumn>();
    columns.add(new LinkPropertyColumn(new Model<String>("Name"), "label", "label"));

    columns.add(new PropertyColumn<IDoapHomepage>(new Model<String>("URL"), "url", "url") {
      private static final long serialVersionUID = 1L;

      @Override
      public void populateItem(Item<ICellPopulator<IDoapHomepage>> cellItem, String componentId, IModel<IDoapHomepage> model) {
        IDoapHomepage page = model.getObject();
        String label = "";
        
        if (model != null) {
          label = ((IDoapHomepage) model.getObject()).getURI();
        }
        cellItem.add(new Label(componentId, label));
      }
    });

    dataProvider = new SortableDoapResourceDataProvider(pages);
    dataProvider.setSort(SortableDoapResourceDataProvider.SORT_PROPERTY_NAME, true);
    add(new AjaxFallbackDefaultDataTable("dataTable", columns, dataProvider,
        numberOfPages));
  }

  /**
   * Add a homepage to the list being displayed. This method does not add the
   * homepage to the underlying data storage mechanism, it only adds it to the
   * GUI.
   * 
   * @param person
   */
  public void add(IDoapHomepage page) {
    this.pages.add(page);
  }
}

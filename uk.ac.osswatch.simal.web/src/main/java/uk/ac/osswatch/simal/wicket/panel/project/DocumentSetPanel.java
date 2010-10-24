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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.IDocument;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.data.SortableDocumentDataProvider;
import uk.ac.osswatch.simal.wicket.markup.html.repeater.data.table.LinkPropertyColumn;

/**
 * A simple panel for listing a set of any IDoapResources. 
 */
public class DocumentSetPanel extends Panel {
  public static final Logger LOGGER = LoggerFactory
      .getLogger(DocumentSetPanel.class);

  private static final long serialVersionUID = -932080365392667144L;

  private static final int MAX_ROWS_PER_PAGE = 10;
  
  private Set<IDocument> documents;
  private String title;
  private AddIResourcePanel addDocumentPanel;
  private IProject project;
  private boolean editingOn;
  private boolean editingAllowed;

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
  public DocumentSetPanel(String id, String title,
      Set<IDocument> resources, boolean editingAllowed, IProject project) {
    super(id);
    this.title = title;
    this.project = project;
    this.editingAllowed = editingAllowed;
    this.documents = (Set<IDocument>) resources;

    this.editingOn = false;
    populatePanel();
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
  public DocumentSetPanel(String id, String title,
      Set<IDocument> resources) {
    // TODO By default no editing allowed
    this(id, title, resources, false, null);
  }

  private void populatePanel() {
    add(new Label("title", title));
    addDocumentsList();
    this.addDocumentPanel = new AddIResourcePanel("addWebsitePanel", this, editingAllowed);
    this.addDocumentPanel.setVisible(this.editingOn);
    add(this.addDocumentPanel);
    setOutputMarkupId(true);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private void addDocumentsList() {
    List<IColumn<?>> columns = new ArrayList<IColumn<?>>();
    columns.add(new LinkPropertyColumn(new Model<String>("Name"), "label",
        "label"));
    
    PropertyColumn<IDocument> urlColumn = new PropertyColumn<IDocument>(new Model<String>("URL"),
        "url", "url") {
      private static final long serialVersionUID = -3063052337733586041L;

      @Override
      public void populateItem(Item<ICellPopulator<IDocument>> cellItem,
          String componentId, IModel<IDocument> model) {
        String label = "";

        if (model != null) {
          label = model.getObject().getURI();
        }
        cellItem.add(new Label(componentId, label));
      }
      
      public String getCssClass() {
        return "visiblecell";
      }
      
    };
      
    columns.add(urlColumn);

    PropertyColumn<IDocument> deleteColumn = new PropertyColumn<IDocument>(new Model<String>("Delete"), "name", "name") {
      private static final long serialVersionUID = -3063052337733586041L;

      public void populateItem(Item<ICellPopulator<IDocument>> cellItem,
          String componentId, IModel<IDocument> model) {

        AjaxFallbackLink<IDocument> deleteLink = new AjaxFallbackLink<IDocument>(componentId, model) {
          private static final long serialVersionUID = 876069018792653905L;

          @Override
          public void onClick(AjaxRequestTarget target) {
            processDeleteOnClick(target, getModel().getObject());
          }

          public void onComponentTagBody(MarkupStream markupStream,
              ComponentTag openTag) {
            String linkText = "<a href=\"#\">Delete</a>";
            replaceComponentTagBody(markupStream, openTag, linkText);
          }
        };
        cellItem.add(deleteLink);
      }
      
      public String getCssClass() {
        return (editingOn) ? "visiblecell" : "invisiblecell";
      }
      
    };
    columns.add(deleteColumn);
      
    SortableDocumentDataProvider<IDocument> dataProvider = new SortableDocumentDataProvider<IDocument>(documents);
    dataProvider.setSort(SortableDocumentDataProvider.SORT_PROPERTY_NAME,
        true);
    add(new AjaxFallbackDefaultDataTable("dataTable", columns, dataProvider,
        MAX_ROWS_PER_PAGE));
  }

  /**
   * Add a resource to the list being displayed. This method does not add the
   * resource to the underlying data storage mechanism, it only adds it to the
   * GUI.
   * 
   * @param iFoafResource
   */
  protected void addToList(IDocument iFoafResource) {
    this.documents.add(iFoafResource);
  }

  private void processDeleteOnClick(AjaxRequestTarget target, IDocument iDoapResource) {
    try {
      processDelete(iDoapResource);
    } catch (SimalRepositoryException e) {
      LOGGER.warn("Failed to delete resource " + iDoapResource);
    }
    target.addComponent(this);
    
  }

  public void processAdd(IDoapResourceFormInputModel inputModel)
      throws SimalException {
    // Subclasses that wish to use it should implement it.
    // TODO To be made abstract to force subclassing and implementation of
    // method
  }

  /**
   * Remove a resource from the list being displayed. This method does not delete 
   * the resource from the underlying data storage mechanism, but only from the GUI.
   * 
   * @param iDoapResource
   */
  public void delete(IDocument iDoapResource) {
    this.documents.remove(iDoapResource);
  }

  public void processDelete(IDocument inputModel)
      throws SimalRepositoryException {
    // Subclasses that wish to use it should implement it.
    // TODO To be made abstract to force subclassing and implementation of
    // method
  }

  protected IProject getProject() {
    return this.project;
  }

  /**
   * @param editMode
   */
  public void setEditingOn(boolean editMode) {
    this.editingOn = (editMode && this.editingAllowed);
    this.addDocumentPanel.setVisible(this.editingOn);
  }
}

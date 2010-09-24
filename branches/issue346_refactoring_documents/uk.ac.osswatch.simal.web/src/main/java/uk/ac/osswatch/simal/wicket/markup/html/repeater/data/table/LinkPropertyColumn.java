package uk.ac.osswatch.simal.wicket.markup.html.repeater.data.table;

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

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IDocument;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.data.DetachableDocumentModel;
import uk.ac.osswatch.simal.wicket.doap.CategoryDetailPage;
import uk.ac.osswatch.simal.wicket.doap.ProjectDetailPage;
import uk.ac.osswatch.simal.wicket.foaf.PersonDetailPage;
import uk.ac.osswatch.simal.wicket.panel.LinkPanel;

/**
 * A utility class for creating a ProperyColumn for DataTables that is also a
 * bookmarkable hyperlink. It is generic for the IResources of type Category, 
 * Person and Project. 
 */
public class LinkPropertyColumn extends PropertyColumn<IResource> {

  private static final long serialVersionUID = -8731311921605414490L;

  public LinkPropertyColumn(IModel<String> displayModel, String sortProperty,
      String propertyExpression) {
    super(displayModel, sortProperty, propertyExpression);
  }

  /**
   * Add a link based on the type of resource. 
   */
  @Override
  public void populateItem(Item<ICellPopulator<IResource>> item,
      String componentId, IModel<IResource> model) {
    IResource targetResource;
    if (model instanceof DetachableDocumentModel) {
      IDocument page = (IDocument)model.getObject();
      item.add(new LinkPanel(componentId, page.getURI(), page.getLabel()));
    } else {
      targetResource = model.getObject();
      item.add(new LinkPanel(componentId, targetResource, createLabelModel(model)));
    }
  }

  /**
   * Generate the correct response page based on the type of resource this
   * model is for.
   * @param item
   * @param componentId
   * @param model
   */
  public void onClick(Item<?> item, String componentId, IModel<?> model) {
    IResource resource = (IResource) model.getObject();
    item.getRequestCycle().setResponsePage(generateResponsePage(resource));
  }

  /**
   * A somewhat easy fix to generate a subclass of BasePage which is the 
   * correct type based on the type of the IResource it is for. 
   * @param targetResource
   * @return subtype of BasePage for the specific targetResource 
   */
  private BasePage generateResponsePage(IResource targetResource) {
    BasePage targetClass = null;

    if (targetResource instanceof IProject) {
      targetClass = new ProjectDetailPage((IProject) targetResource);
    } else if (targetResource instanceof IPerson) {
      targetClass = new PersonDetailPage((IPerson) targetResource);
    } else if (targetResource instanceof IDoapCategory) {
      targetClass = new CategoryDetailPage((IDoapCategory) targetResource);
    }

    return targetClass;
  }

}

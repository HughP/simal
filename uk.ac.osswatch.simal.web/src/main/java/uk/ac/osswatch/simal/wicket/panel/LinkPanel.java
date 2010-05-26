/*
 * Copyright 2010 University of Oxford 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

package uk.ac.osswatch.simal.wicket.panel;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.doap.CategoryDetailPage;
import uk.ac.osswatch.simal.wicket.doap.ProjectDetailPage;
import uk.ac.osswatch.simal.wicket.foaf.PersonDetailPage;

/**
 * Panel for displaying a link. This can be a regular Link
 * or a  BookmarkablePageLink can be generated to a sub type 
 * of IResource.
 */
public class LinkPanel extends Panel {
  private static final Logger LOGGER = LoggerFactory.getLogger(LinkPanel.class);

  private static final long serialVersionUID = 7339822533511404418L;

  /**
   * Simple constructor to generate a link based on whatever Link object
   * is passed to it.
   * @param item
   * @param componentId
   * @param model
   * @param link
   * @param labelModel
   */
  public LinkPanel(final Item<?> item, final String componentId,
      final IModel<?> model, Link<BasePage> link, IModel<?> labelModel) {
    super(componentId);

    add(link);
    link.add(new Label("label", labelModel));
  }

  /**
   * Constructor to create a link to an IResource, which will be a 
   * bookmarkable link. If the targetResource is null, it will only 
   * a label will be added.
   * @param componentId
   * @param targetResource
   * @param labelModel
   */
  public LinkPanel(final String componentId, IResource targetResource,
      IModel<?> labelModel) {
    super(componentId);
    if (targetResource != null) {
      PageParameters parameters = new PageParameters();
      try {
        parameters.add("simalID", targetResource.getSimalID());
      } catch (SimalRepositoryException e) {
        LOGGER.warn("Problem accessing target Resource for link : "
            + e.getMessage(), e);
      }
      BookmarkablePageLink<BasePage> link = new BookmarkablePageLink<BasePage>(
          "link", getTargetPageClass(targetResource), parameters);
      add(link);
      link.add(new Label("label", labelModel));
    } else {
      add(new Label(componentId, ""));
    }
  }

  /**
   * Easy fix for returning the correct Class for the page that 
   * should be generated for a specific sub class of IResource
   * @param targetResource
   * @return Some sub class of BasePage
   */
  private Class<? extends BasePage> getTargetPageClass(IResource targetResource) {
    Class<? extends BasePage> targetClass = null;

    if (targetResource instanceof IProject) {
      targetClass = ProjectDetailPage.class;
    } else if (targetResource instanceof IPerson) {
      targetClass = PersonDetailPage.class;
    } else {
      targetClass = CategoryDetailPage.class;
    }

    return targetClass;
  }

}

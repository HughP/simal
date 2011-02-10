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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.panel.AbstractAddDoapResourcePanel;

/**
 * A simple panel for listing a set of any IDoapResources. 
 */
public abstract class AbstractEditableResourcesPanel<T extends IResource> extends Panel {

  private static final long serialVersionUID = -6519304613539785366L;

  public static final Logger LOGGER = LoggerFactory
      .getLogger(AbstractEditableResourcesPanel.class);
  
  private AbstractAddDoapResourcePanel<T> addDoapResourcePanel;

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
  public AbstractEditableResourcesPanel(String id, String title) {
    super(id);

    add(new Label("title", title));
    setOutputMarkupId(true);
  }

  public void addAddDoapResourcePanel(
      AbstractAddDoapResourcePanel<T> addDoapResourcePanel) {
    this.addDoapResourcePanel = addDoapResourcePanel;
    add(addDoapResourcePanel);
    setOutputMarkupId(true);
  }

  public abstract void addToDisplayList(T doapResource);

  public abstract void addToModel(T doapResource) throws SimalException;

  public AbstractAddDoapResourcePanel<T> getAddDoapResourcePanel() {
    return this.addDoapResourcePanel;
  }

}

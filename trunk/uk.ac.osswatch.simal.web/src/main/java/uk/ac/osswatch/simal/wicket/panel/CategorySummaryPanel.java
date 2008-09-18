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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import uk.ac.osswatch.simal.model.IDoapCategory;

/**
 * Display summary information for a category.
 */
public class CategorySummaryPanel extends Panel {
  private static final long serialVersionUID = 4436736277889089047L;

  /**
   * Create a summary page for a specific category.
   * 
   * @param panelID
   * @param category
   *          the category to display in this panel
   */
  public CategorySummaryPanel(String panelId, IDoapCategory category) {
    super(panelId);
    populatePage(category);
  }

  private void populatePage(final IDoapCategory cat) {
    add(new Label("label", cat.getLabel()));
    add(new Label("uri", cat.getURI()));
  }

}

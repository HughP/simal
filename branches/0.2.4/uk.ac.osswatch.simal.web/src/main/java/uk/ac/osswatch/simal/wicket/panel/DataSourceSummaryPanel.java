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

/**
 * A summary panel showing information about a known data source.
 */
public class DataSourceSummaryPanel extends Panel {
  private static final long serialVersionUID = -8115164444333001075L;

  /**
   * Create a summary panel for the Data source at a given URI.
   * 
   * @param panelID
   * @param uri
   */
  public DataSourceSummaryPanel(String panelID, String uri) {
    super(panelID);
    add(new Label("uri", uri));
  }
}

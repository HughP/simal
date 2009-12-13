package uk.ac.osswatch.simal.wicket.widgets;

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

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A panel to display a Wookie Widget. See http://incubator.apache.org/wookie
 */
public class WookieWidgetPanel extends Panel {
  private static final long serialVersionUID = 1L;
  private static final Logger logger = LoggerFactory.getLogger(WookieWidgetPanel.class);
  
  /**
   * Create a default Wookie Widget.
   * 
   * @param id the wiketID of this component
   * @param instance the widget instance to display
   */
  public WookieWidgetPanel(String id, Widget.Instance instance) {
    super(id);
    populatePanel(instance);
  }

  /**
   * Populate the panel with data.
   * 
   */
  private void populatePanel(Widget.Instance instance) {
    WebMarkupContainer iFrame = new WebMarkupContainer("widget"); 
    iFrame.setOutputMarkupId(true);
    iFrame.add(new SimpleAttributeModifier("src", instance.getUrl())); 
    iFrame.add(new SimpleAttributeModifier("height", instance.getHeight())); 
    iFrame.add(new SimpleAttributeModifier("width", instance.getWidth())); 
    add(iFrame);
  }
  
}

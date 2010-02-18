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

import java.util.HashMap;
import java.util.Iterator;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.wicket.UserApplication;

/**
 * A panel to display a Wookie Widget. See http://incubator.apache.org/wookie
 */
public class WookieWidgetGalleryPanel extends Panel {
  private static final long serialVersionUID = 1L;
  
  /**
   * Create a default Wookie Widget gallery panel that will display all the
   * available widgets on a server.
   * 
   * @param id the wiketID of this component
   * @throws SimalException if unable to build gallery 
   */
  public WookieWidgetGalleryPanel(String id) throws SimalException {
    super(id);
    populatePanel(UserApplication.getWookieServerConnection().getAvailableWidgets());
  }
  
  /**
   * Populate the panel with data.
   * @param widgets a hashmap of the widgets to display in this panel 
   * 
   */
  private void populatePanel(HashMap<String, Widget> widgets) {
    RepeatingView repeating = new RepeatingView("gallery");
    if (widgets.size() > 0) {
	    Iterator<Widget> itr = widgets.values().iterator();
	    Widget widget;
		while(itr.hasNext()) {
		  WebMarkupContainer item = new WebMarkupContainer(repeating.newChildId());
		  repeating.add(item);
		  widget = itr.next();
		  
		  item.add(new StaticImage("icon", new Model(widget.getIcon().toExternalForm())));
		  item.add(new Label("name", widget.getTitle()));
		  item.add(new Label("description", widget.getDescription()));
		  
		  item.add(InstantiateWidgetPage.getLink("instantiate", widget));
		}
    } else {
    	WebMarkupContainer item = new WebMarkupContainer(repeating.newChildId());
		repeating.add(item);
		item.add(new StaticImage("icon", new Model("")));
		item.add(new Label("name", "Widget Server"));
		item.add(new Label("description", "There was a problem communicting with the Widget server"));
		item.add(new Label("instantiate", ""));
    }
	add(repeating);
  }
  
  public static class StaticImage extends WebComponent {

    private static final long serialVersionUID = -8331551149365773888L;

      public StaticImage(String id, IModel model) {
          super(id, model);
      }

      protected void onComponentTag(ComponentTag tag) {
          super.onComponentTag(tag);
          checkComponentTag(tag, "img");
          tag.put("src", getDefaultModelObjectAsString());
      }

  }
}

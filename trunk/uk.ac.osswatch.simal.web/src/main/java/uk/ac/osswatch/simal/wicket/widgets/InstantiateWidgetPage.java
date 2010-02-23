package uk.ac.osswatch.simal.wicket.widgets;

/*
 * Copyright 2009 University of Oxford
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

import java.io.IOException;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserApplication;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.widgets.Widget.Instance;

/**
 * Called when a widget needs to be instantiated. This page will attempt to 
 * instantiate the widget and will return information about the widget to the user.
 */
public class InstantiateWidgetPage extends BasePage {
	
  private static final long serialVersionUID = -2075240720179434503L;

  /**
	 * Attempt to instantiate a widget using a wookie server.
	 * 
	 */
	public InstantiateWidgetPage(Widget widget) {
      Instance instance;
	  try {
		  instance = UserApplication.getWookieServerConnection().getOrCreateInstance(widget);
		  populatePage(instance);
	  } catch (IOException e) {
    	  UserReportableException error = new UserReportableException(
		          "Problem communicating with Wookie server", InstantiateWidgetPage.class);
		  setResponsePage(new ErrorReportPage(error));
	  } catch (SimalRepositoryException e) {
        UserReportableException error = new UserReportableException(
            "Problem communicating with Wookie server", InstantiateWidgetPage.class);
        setResponsePage(new ErrorReportPage(error));
	  }
	}

	/**
	 * Populate the page with the details of the instantiated widget.
	 * 
	 * @param id
	 */
	private void populatePage(Widget.Instance instance) {
	    add(new WookieWidgetPanel("widget", instance));
		
		add(new Label("id", instance.getId()));
		add(new Label("title", instance.getTitle()));
		add(new Label("url", instance.getUrl()));
		add(new Label("height", instance.getHeight()));
		add(new Label("width", instance.getWidth()));
		add(new Label("maximize", instance.getMaximize()));
	}

	/**
	   * Get a link to an InstantiateWidgetPage for a widget.
	   * 
	   * @param widget
	   *          the widget we want to instantiate with this page
	   * @return
	   */
	  public static Link getLink(final String id, final Widget widget) {	    
	   return new Link(id) {
	     private static final long serialVersionUID = 4984984523L;
	     
		   public void onClick(){
			   setResponsePage(new InstantiateWidgetPage(widget));
	      }
	   };
	  }
}


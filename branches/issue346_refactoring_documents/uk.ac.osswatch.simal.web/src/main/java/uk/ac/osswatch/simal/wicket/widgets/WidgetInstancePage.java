package uk.ac.osswatch.simal.wicket.widgets;

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

import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserApplication;
import uk.ac.osswatch.simal.wicket.UserReportableException;

/**
 * Page to display an instantiated widget. Built to just display any instance of
 * a widget as the widget instance is determined by the widget's title only.
 */
public class WidgetInstancePage extends BasePage {
  private static final Logger LOGGER = LoggerFactory
      .getLogger(WidgetInstancePage.class);

  /**
   * Key with which the widget parameter is stored and retrieved to create the 
   * WidgetInstancePage.
   */
  public static final String WIDGET_KEY = "widgetTitle";

  /**
   * Display an instantiated widget using a wookie server.
   * 
   */
  public WidgetInstancePage(PageParameters parameters) {
    super();

    if (parameters.containsKey(WIDGET_KEY)) {
      String id = parameters.getString(WIDGET_KEY);
      WidgetInstance instance = UserApplication.getWookieServerConnection()
          .getInstance(id);
      populatePage(instance);
    } else {
      String errorMessage = "No " + WIDGET_KEY + " to create widget instance from.";
      LOGGER.warn(errorMessage);
      reportError(errorMessage);
    }
  }

  /**
   * Populate the page with the details of the instantiated widget as an iframe.
   * 
   * @param instance
   */
  private void populatePage(WidgetInstance instance) {
    if (instance != null) {
      add(new Label("description", instance.getDescription()));

      WebMarkupContainer iFrame = new WebMarkupContainer("widget");
      iFrame.setOutputMarkupId(true);
      iFrame.add(new SimpleAttributeModifier("src", instance.getUrl()));
      iFrame.add(new SimpleAttributeModifier("height", instance.getHeight()));
      iFrame.add(new SimpleAttributeModifier("width", instance.getWidth()));
      add(iFrame);

    } else {
      reportError("Unable to retrieve widget instance");
    }
  }

  private void reportError(String errorMessage) {
    UserReportableException error = new UserReportableException(errorMessage,
        WidgetInstancePage.class);
    setResponsePage(new ErrorReportPage(error));
  }

  /**
   * Get a link to a WidgetInstancePage for a widget instance.
   * 
   * @param instance
   *          the widget instance we want to instantiate with this page
   * @return
   */
  public static Link<WidgetInstancePage> getPageLink(String linkName,
      WidgetInstance instance) {
    PageParameters parameters = new PageParameters();
    parameters.add(WIDGET_KEY, instance.getTitle());
    BookmarkablePageLink<WidgetInstancePage> link = new BookmarkablePageLink<WidgetInstancePage>(
        linkName, WidgetInstancePage.class, parameters);
    return link;
  }

}

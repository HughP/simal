package uk.ac.osswatch.simal.wicket;

/*
 * Copyright 2008,2010 University of Oxford
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


import java.util.Iterator;
import java.util.Set;

import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.markup.repeater.RepeatingView;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.wicket.authentication.LoginPage;
import uk.ac.osswatch.simal.wicket.doap.CategoryBrowserPage;
import uk.ac.osswatch.simal.wicket.doap.DoapFormPage;
import uk.ac.osswatch.simal.wicket.doap.ProjectBrowserPage;
import uk.ac.osswatch.simal.wicket.foaf.PersonBrowserPage;
import uk.ac.osswatch.simal.wicket.panel.DataSourceSummaryPanel;
import uk.ac.osswatch.simal.wicket.panel.StandardFooter;
import uk.ac.osswatch.simal.wicket.report.ProjectsSummaryReportPage;
import uk.ac.osswatch.simal.wicket.widgets.WidgetInstance;
import uk.ac.osswatch.simal.wicket.widgets.WidgetInstancePage;

/**
 * The base page for a standard simal web page. It contains the standard markup
 * for the site, such as headers and footers. All pages in the Simal site should
 * extend this page.
 */
public class BasePage extends WebPage {
  public static final String DEFAULT_CSS_LOC = "style/default.css";
  private static final long serialVersionUID = 7789964685286908825L;

  private static final CompressedResourceReference DEFAULT_CSS = new CompressedResourceReference(
      UserApplication.class, DEFAULT_CSS_LOC);

  public BasePage() {
    add(CSSPackageResource.getHeaderContribution(DEFAULT_CSS));
    add(new BookmarkablePageLink<UserHomePage>("homePageLink",
        UserHomePage.class));
    add(new BookmarkablePageLink<PersonBrowserPage>("personBrowserLink",
        PersonBrowserPage.class));
    add(new BookmarkablePageLink<CategoryBrowserPage>(
        "categoryBrowserPageLink", CategoryBrowserPage.class));
    add(new BookmarkablePageLink<ProjectBrowserPage>("projectBrowserLink",
        ProjectBrowserPage.class));
    add(new BookmarkablePageLink<DoapFormPage>("addDOAPLink",
        DoapFormPage.class));
    add(new BookmarkablePageLink<ProjectsSummaryReportPage>("reportsLink",
        ProjectsSummaryReportPage.class));
    add(new BookmarkablePageLink<ToolsPage>("toolsLink", ToolsPage.class));
    add(getAddProjectLink());
    add(new BookmarkablePageLink<LoginPage>("loginLink",
        LoginPage.class));
    add(new StandardFooter("footer"));
  }

  /**
   * Generate a link to the 'add project' widget if available, otherwise re-use
   * the old addDOAPLink.
   * 
   * @return
   */
  private Link<? extends BasePage> getAddProjectLink() {
    Link<? extends BasePage> link;
    WidgetInstance instance = UserApplication.getWookieServerConnection()
        .getInstance(getAddProjectWidgetName());
    if (instance != null) {
      link = WidgetInstancePage.getPageLink("addProjectLink", instance);
    } else {
      link = new BookmarkablePageLink<DoapFormPage>("addProjectLink",
          DoapFormPage.class);
    }
    return link;
  }

  /**
   * Get the name under which the 'add project' widget is registered.
   * @return
   */
  private String getAddProjectWidgetName() {
    String defaultWidgetName = "doapcreator";
    String doapProjectWidgetName;

    doapProjectWidgetName = SimalProperties.getProperty(
        SimalProperties.PROPERTY_ADD_PROJECT_WIDGET_TITLE, defaultWidgetName);

    return doapProjectWidgetName;
  }

  /**
   * Get a simple repeating view displaying a number of data source (as in
   * rdfs:seeAlso) summary panels.
   * 
   * @param repeaterWicketID
   *          the wicket ID of the repeating view
   * @param sourceWicketID
   *          the wicket ID for the repeating person panel
   * @param sources
   *          the set of URIs for sources to display in this repeating view
   * @return
   */
  protected RepeatingView getRepeatingDataSourcePanel(
      String repeaterWicketID, String sourceWicketID, Set<String> sources) {
    Iterator<String> itr = sources.iterator();
    RepeatingView repeating = new RepeatingView(repeaterWicketID);
    WebMarkupContainer item;
    String uri;
    while (itr.hasNext()) {
      item = new WebMarkupContainer(repeating.newChildId());
      repeating.add(item);
      uri = itr.next();
      item.add(new DataSourceSummaryPanel(sourceWicketID, uri));
    }
    return repeating;
  }
}

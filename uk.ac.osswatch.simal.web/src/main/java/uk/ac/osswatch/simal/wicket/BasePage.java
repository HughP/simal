package uk.ac.osswatch.simal.wicket;

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

import java.util.Iterator;
import java.util.Set;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.model.IDoapResource;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.doap.CategoryBrowserPage;
import uk.ac.osswatch.simal.wicket.doap.DoapFormPage;
import uk.ac.osswatch.simal.wicket.doap.ProjectBrowserPage;
import uk.ac.osswatch.simal.wicket.foaf.PersonBrowserPage;
import uk.ac.osswatch.simal.wicket.panel.DataSourceSummaryPanel;
import uk.ac.osswatch.simal.wicket.panel.PersonSummaryPanel;
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
  private static final Logger logger = LoggerFactory.getLogger(BasePage.class);

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
   * Get a simple repeating view. Each resource is represented by a link to the
   * URL contained in rdf:resource. the label for the link is either defined by
   * rdfs:label, or the defaultLabel property, or the value of rdf:resource.
   * 
   * If any of the resource does not yield a valid URL then the error is logged
   * and a blank URL is provided.
   * 
   * @param repeaterWicketID
   *          the wicket:id of the HTML component
   * @param linkWicketID
   *          the wicket:id of the link within each list
   * @param defaultLabel
   *          the default value to use for the links label. If the resource has
   *          an rdfs:label value that will be used, otherwise this property
   *          will be used. If this property is null then the value of
   *          rdfs:resource is used.
   * @param resources
   *          the resources to be added to the list
   * @param fetchLabels
   *          If set to true attempt to fetch a label from the repository if one
   *          is not supplied by the resource. If set to false the label will be
   *          set to either the defaultLabel (if not null) or to the resource
   *          URI.
   * @return
   */
  @SuppressWarnings("unchecked")
  protected RepeatingView getRepeatingLinks(String repeaterWicketID,
      String linkWicketID, String defaultLabel, SortableDataProvider resources,
      boolean fetchLabels) {
    Iterator<IDoapResource> itr = resources.iterator(0, resources.size());
    RepeatingView repeating = new RepeatingView(repeaterWicketID);
    WebMarkupContainer item;
    IDoapResource resource;
    String label;
    String comment;
    String url;
    ExternalLink link;
    while (itr.hasNext()) {
      item = new WebMarkupContainer(repeating.newChildId());
      repeating.add(item);
      resource = itr.next();
      comment = resource.getComment();
      url = resource.getURI();
      label = resource.getLabel(defaultLabel);
      link = new ExternalLink(linkWicketID, url);
      link.add(new Label("label", label));
      if (comment != null) {
        link.add(new SimpleAttributeModifier("alt", comment));
      }
      item.add(link);
    }
    return repeating;
  }

  /**
   * Get a simple repeating view. Each resource is represented by a link to the
   * URL contained in rdf:resource. the label for the link is either defined by
   * rdfs:label, or if no such value is defined, the value of rdf:resource.
   * 
   * @param repeaterWicketID
   *          the wicket:id of the HTML component
   * @param linkWicketID
   *          the wicket:id of the link within each list
   * @param resources
   *          the resources to be added to the list
   * @param fetchLabels
   *          If set to true attempt to fetch a label from the repository if one
   *          is not supplied by the resource. If set to false the label will be
   *          set to either the defaultLabel (if not null) or to the resource
   *          URI.
   * @return
   */
  protected RepeatingView getRepeatingLinks(String repeaterWicketID,
      String linkWicketID, SortableDataProvider<IResource> resources, boolean fetchLabels) {
    return getRepeatingLinks(repeaterWicketID, linkWicketID, null, resources,
        fetchLabels);
  }

  /**
   * Get a simple repeating view. Each resource in the supplied set will be
   * represented in the list using the supplied string as a label.
   * 
   * @param repeaterWicketID
   *          the wicket:id of the HTML component
   * @param labelWicketID
   *          the wicket:id of the label within each list item
   * @param resources
   *          the resources to be added to the list
   * @return
   */
  protected RepeatingView getRepeatingLabels(String repeaterWicketID,
      String labelWicketID, Set<String> labels) {
    Iterator<String> itr = labels.iterator();
    RepeatingView repeating = new RepeatingView(repeaterWicketID);
    WebMarkupContainer item;
    String label;
    while (itr.hasNext()) {
      item = new WebMarkupContainer(repeating.newChildId());
      repeating.add(item);
      label = itr.next();
      item.add(new Label(labelWicketID, label));
    }
    return repeating;
  }

  /**
   * Get a simple repeating view displaying a number of person summary panels.
   * 
   * @param repeaterWicketID
   *          the wicket ID of the repeating view
   * @param personWicketID
   *          the wicket ID for the repeating person panel
   * @param people
   *          the set of people to display in this repeating view
   * @return
   */
  protected RepeatingView getRepeatingPersonPanel(String repeaterWicketID,
      String personWicketID, Set<IPerson> people) {
    Iterator<IPerson> itr = people.iterator();
    RepeatingView repeating = new RepeatingView(repeaterWicketID);
    WebMarkupContainer item;
    IPerson person;
    while (itr.hasNext()) {
      item = new WebMarkupContainer(repeating.newChildId());
      repeating.add(item);
      person = itr.next();
      try {
        item.add(new PersonSummaryPanel(personWicketID, person));
      } catch (SimalRepositoryException e) {
        logger.warn("Can't display person: " + person.toString(), e);
      }
    }
    return repeating;
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
  protected RepeatingView getRepeatingDataSourcePanel(String repeaterWicketID,
      String sourceWicketID, Set<String> sources) {
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

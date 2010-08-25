package uk.ac.osswatch.simal.wicket.utils;

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

import java.util.Iterator;
import java.util.Set;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.IDoapResource;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.panel.PersonSummaryPanel;

public class MarkupUtils {
  private static final Logger LOGGER = LoggerFactory
      .getLogger(MarkupUtils.class);

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
  public static RepeatingView getRepeatingLinks(String repeaterWicketID,
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
  public static RepeatingView getRepeatingLinks(String repeaterWicketID,
      String linkWicketID, SortableDataProvider<IResource> resources,
      boolean fetchLabels) {
    return getRepeatingLinks(repeaterWicketID, linkWicketID, null, resources,
        fetchLabels);
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
  public static RepeatingView getRepeatingPersonPanel(String repeaterWicketID,
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
        LOGGER.warn("Can't display person: " + person.toString(), e);
      }
    }
    return repeating;
  }

}

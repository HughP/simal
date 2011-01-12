package uk.ac.osswatch.simal.wicket.panel;

/*
 * Copyright 2008, 2010 University of Oxford
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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import uk.ac.osswatch.simal.model.IDoapRepository;
import uk.ac.osswatch.simal.model.IDocument;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.wicket.panel.project.AbstractEditableResourcesPanel;
import uk.ac.osswatch.simal.wicket.panel.project.AddSourceRepositoryPanel;
import uk.ac.osswatch.simal.wicket.panel.project.EditProjectPanel.ReadOnlyStyleBehavior;

/**
 * A panel to display one or more Source Repository details.
 */
public class SourceRepositoriesPanel extends AbstractEditableResourcesPanel<IDoapRepository> {
  private static final long serialVersionUID = -2031486948152653715L;
  private ReadOnlyStyleBehavior rosb;
  private RepeatingView repeating;
  private IProject project;
  
  public SourceRepositoriesPanel(String panelId, String title,
      Set<IDoapRepository> repositories, ReadOnlyStyleBehavior rosb, boolean loggedIn, IProject project) {
    super(panelId, title, loggedIn);
    this.rosb = rosb;
    this.project = project;
    populateRepeatingLinks(repositories);
    populatePage(repositories);
  }

  private void populatePage(Set<IDoapRepository> repositories) {
    add(repeating);
    addAddDoapResourcePanel(new AddSourceRepositoryPanel("addRepositoryPanel", this, true));
  }

  /**
   * Get a simple repeating view. Each resource is represented by a link to the
   * URL contained in rdf:resource. the label for the link is either defined by
   * rdfs:label, or the defaultLabel property, or the value of rdf:resource.
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
   * @return
   */
  protected void populateRepeatingLinks(Set<IDoapRepository> repositories) {
    Iterator<IDoapRepository> itr = repositories.iterator();
    repeating = new RepeatingView("sourceRepositories");

    while (itr.hasNext()) {
      addToDisplayList(itr.next());
    }
  }

  /**
   * Get a simple repeating view. Each resource is represented by a link to the
   * URLs for the locations provided. The label for the link is either defined
   * by rdfs:label, or the defaultLabel property, or the value of rdf:resource.
   * 
   * @param repeaterWicketID
   *          the wicket:id of the HTML component
   * @param linkWicketID
   *          the wicket:id of the link within each list
   * @param locations
   *          the locations to be added to the list
   * @return
   */
  private Component getRepeatingLinks(String repeaterWicketID,
      String linkWicketID, Set<IDocument> locations) {
    if (locations == null) {
      locations = new HashSet<IDocument>();
    }
    RepeatingView repeating = new RepeatingView(repeaterWicketID);
    WebMarkupContainer item;
    ExternalLink link;
    for (IDocument location : locations) {
      item = new WebMarkupContainer(repeating.newChildId());
      repeating.add(item);
      link = new ExternalLink(linkWicketID, location.getURI());
      item.add(link);

      TextField<String> nameInput = new TextField<String>(
          "label", new PropertyModel<String>(location,"URI"));
      nameInput.add(this.rosb);
      item.add(nameInput);
    }
    
    return repeating;
  }

  /**
   * Get a simple repeating view. Each resource is represented by a link to the
   * URLs provided. The label for the link is either defined by rdfs:label, or
   * the defaultLabel property, or the value of rdf:resource.
   * 
   * @param repeaterWicketID
   *          the wicket:id of the HTML component
   * @param linkWicketID
   *          the wicket:id of the link within each list
   * @param urls
   *          the urls to be added to the list
   * @return
   */
  protected RepeatingView getRepeatingLinks(String repeaterWicketID,
      Set<String> urls, String linkWicketID) {
    if (urls == null) {
      urls = new HashSet<String>();
    }
    Iterator<String> itr = urls.iterator();
    RepeatingView repeating = new RepeatingView(repeaterWicketID);
    WebMarkupContainer item;
    String url;
    ExternalLink link;
    while (itr.hasNext()) {
      item = new WebMarkupContainer(repeating.newChildId());
      repeating.add(item);
      url = itr.next();

      link = new ExternalLink(linkWicketID, url);
      item.add(link);
      
      TextField<String> nameInput = new TextField<String>(
          "label", new Model<String>(url));
      nameInput.add(this.rosb);
      item.add(nameInput);
    }
    if(repeating.size() == 0) {
      //item.add(new Label(repeating.newChildId(),""));
    }
    return repeating;
  }

  @Override
  public void addToDisplayList(IDoapRepository repository) {
    WebMarkupContainer item = new WebMarkupContainer(repeating.newChildId());
    Set<String> anonRoots = repository.getAnonRoots();
    item.add(new Label("name", repository.getLabel()));
    item.add(getRepeatingLinks("anonRoots", anonRoots, "anonLink"));
    item.add(getRepeatingLinks("devLocations", "devLink", repository
        .getLocations()));
    item.add(getRepeatingLinks("browseRoots", "browseLink", repository
        .getBrowse()));
    
    repeating.add(item);
  }

  @Override
  public void addToModel(IDoapRepository repository) throws SimalException {
    this.project.addRepository(repository);
  }

}

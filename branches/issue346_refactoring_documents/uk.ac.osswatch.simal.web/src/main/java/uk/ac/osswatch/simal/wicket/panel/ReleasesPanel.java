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

import java.util.Set;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import uk.ac.osswatch.simal.model.IDoapRelease;
import uk.ac.osswatch.simal.wicket.panel.project.EditProjectPanel.ReadOnlyStyleBehavior;

/**
 * A simple panel providing details of a set of releases.
 * @param <ReadOnlyStyleBehavior>
 */
public class ReleasesPanel extends Panel {
  private static final long serialVersionUID = 3993329475348185480L;
  private ReadOnlyStyleBehavior rosb;

  public ReleasesPanel(String panelId, Set<IDoapRelease> releases, ReadOnlyStyleBehavior rosb) {
    super(panelId);
    this.rosb = rosb;
    populatePage(releases);
  }

  private void populatePage(Set<IDoapRelease> releases) {
    RepeatingView repeating = new RepeatingView("releases");
    WebMarkupContainer item;

    for (IDoapRelease release : releases) {
      item = new WebMarkupContainer(repeating.newChildId());
      repeating.add(item);
      
      item.add(new Label("releaseHeader", release.getURI()));
      
      TextField<String> nameInput = new TextField<String>(
          "name", new PropertyModel<String>(release, "name"));
      nameInput.add(this.rosb);
      item.add(nameInput);

      TextField<String> createdInput = new TextField<String>(
          "created", new PropertyModel<String>(release, "created"));
      createdInput.add(this.rosb);
      item.add(createdInput);

      item.add(getRepeatingLabels("revisions", "revision", release
          .getRevisions()));
    }
    add(repeating);
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
    RepeatingView repeating = new RepeatingView(repeaterWicketID);
    WebMarkupContainer item;
    
    for (String label : labels) {
      item = new WebMarkupContainer(repeating.newChildId());
      repeating.add(item);

      TextField<String> nameInput = new TextField<String>(
          labelWicketID, new Model<String>(label));
      nameInput.add(this.rosb);

      item.add(nameInput);
      //item.add(new Label(labelWicketID, label));
    }
    return repeating;
  }

}

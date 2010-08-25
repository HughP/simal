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

import java.util.Iterator;
import java.util.Set;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

import uk.ac.osswatch.simal.model.IDoapRelease;

/**
 * A simple panel providing details of a set of releases.
 */
public class ReleasesPanel extends Panel {
  private static final long serialVersionUID = 3993329475348185480L;

  public ReleasesPanel(String panelId, Set<IDoapRelease> releases) {
    super(panelId);
    populatePage(releases);
  }

  private void populatePage(Set<IDoapRelease> releases) {
    Iterator<IDoapRelease> itr = releases.iterator();
    RepeatingView repeating = new RepeatingView("releases");
    WebMarkupContainer item;
    IDoapRelease release;
    Label label;
    while (itr.hasNext()) {
      item = new WebMarkupContainer(repeating.newChildId());
      repeating.add(item);
      release = itr.next();
      label = new Label("name", release.getName());
      item.add(label);
      item.add(getRepeatingLabels("revisions", "revision", release
          .getRevisions()));
      label = new Label("created", release.getCreated());
      item.add(label);
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

}

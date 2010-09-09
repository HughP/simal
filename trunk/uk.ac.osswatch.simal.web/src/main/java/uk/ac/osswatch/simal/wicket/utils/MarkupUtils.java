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

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.panel.PersonSummaryPanel;

public class MarkupUtils {
  private static final Logger LOGGER = LoggerFactory
      .getLogger(MarkupUtils.class);

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

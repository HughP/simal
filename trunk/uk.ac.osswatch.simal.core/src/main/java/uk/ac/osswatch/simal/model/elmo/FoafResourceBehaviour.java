/*
 * Copyright 2007 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.osswatch.simal.model.elmo;

import java.util.Set;

import org.openrdf.concepts.foaf.Person;
import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.model.IFoafResourceBehaviour;

@rdf("http://xmlns.com/foaf/0.1/Person")
public class FoafResourceBehaviour extends ResourceBehavior implements
    IFoafResourceBehaviour {
  private static final long serialVersionUID = -3852417254318582808L;

  public FoafResourceBehaviour(org.openrdf.concepts.foaf.Person person) {
    super(person);
  }

  /**
   * Create a JSON representation of this person.
   * 
   * @param asRecord
   *          set to true if this is a record within another JSON file or false
   *          if you want this to be a complete JSON file.
   */
  public String toJSON(boolean asRecord) {
    StringBuffer json = new StringBuffer();
    if (!asRecord) {
      json.append("{ \"items\": [");
    }
    json.append("{");
    json.append("\"id\":\"" + elmoEntity.getQName() + "\",");
    json.append("\"label\":\"" + getLabel() + "\",");
    json.append("}");
    if (!asRecord) {
      json.append("]}");
    }
    return json.toString();
  }

  private Person getFoafPerson() {
    return (Person) elmoEntity;
  }

  /**
   * Get the label for this person. The label for a person is derived from their
   * known names. If the person does not have any defined names then the
   * toString() method is used..
   * 
   * @return
   */
  public String getLabel() {
    Set<Object> givenNames = getFoafPerson().getFoafGivennames();
    if (givenNames.size() == 0) {
      givenNames = getFoafPerson().getFoafFirstNames();
    }
    Set<Object> familyNames = getFoafPerson().getFoafFamily_names();

    if (familyNames == null && givenNames == null) {
      return toString();
    } else {
      String name = null;
      if (familyNames != null && familyNames.size() > 0) {
        name = (String) familyNames.toArray()[0];
      }
      if (givenNames.size() > 0) {
        if (name != null) {
          name = (String) givenNames.toArray()[0] + " " + name;
        } else {
          name = (String) givenNames.toArray()[0];
        }
      }
      return name;
    }
  }
}

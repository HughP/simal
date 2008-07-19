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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Utilities {

  /**
   * Convert a set of objects to a set of strings.
   * 
   * @param sourceSet
   * @return
   */
  protected static Set<String> convertToSetOfStrings(Set<Object> sourceSet) {
    Set<String> result = new HashSet<String>(sourceSet.size());
    Iterator<Object> itr = sourceSet.iterator();
    while (itr.hasNext()) {
      result.add(itr.next().toString());
    }
    return result;
  }
}

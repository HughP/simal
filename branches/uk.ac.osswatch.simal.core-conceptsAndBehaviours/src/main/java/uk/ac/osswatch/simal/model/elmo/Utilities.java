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

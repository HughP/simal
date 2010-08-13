/*
 * Copyright 2010 University of Oxford 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

package uk.ac.osswatch.simal.model.utils;

import java.io.Serializable;
import java.util.Comparator;

import uk.ac.osswatch.simal.model.IDoapResource;

/**
 * Comparator to compare IDoapResources by their default names
 */
public class DoapResourceByNameComparator implements Comparator<IDoapResource>, Serializable {

  private static final long serialVersionUID = 6389372938440343538L;

  /**
   * Compare IDoapResources by their default names
   * 
   * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
   */
  public int compare(IDoapResource a, IDoapResource b) {
    int result = (a == null ? 
        (b == null ? 0 : -1) : 
        (b == null ? 1 : compareNames(a, b)));

    return result;
  }

  /**
   * Compares two IDoapResources by name assuming that both objects
   * are non-null.
   * 
   * @param a
   * @param b
   * @return
   */
  private int compareNames(IDoapResource a, IDoapResource b) {
    String aName = a.getName();
    String bName = b.getName();

    int result = (aName == null ? 
        (bName == null ? 0 : -1) : 
        (bName == null ? 1 : aName.compareTo(bName)));

    return result;
  }
}

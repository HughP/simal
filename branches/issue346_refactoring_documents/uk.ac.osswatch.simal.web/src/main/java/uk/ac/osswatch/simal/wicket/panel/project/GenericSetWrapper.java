package uk.ac.osswatch.simal.wicket.panel.project;

import java.io.Serializable;
import java.util.Set;

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

/**
 * Wrapper for items in a set so they can be edited on a form. When the Set is a
 * property of another object it needs to be updated separately, for example
 * when OSes are edited for an IProject.
 * 
 * @param <T>
 */
public class GenericSetWrapper<T> implements Serializable {

  private static final long serialVersionUID = 7273060956309086010L;

  private Set<T> parentSet;

  private T localValue;

  /**
   * Create a new wrapper for a set and an item in the set.
   * Reference to the parentSet is retained to be able to update
   * the parentSet from this wrapper. 
   * 
   * @param parentSet
   * @param value
   */
  public GenericSetWrapper(Set<T> parentSet, T value) {
    this.parentSet = parentSet;
    this.localValue = value;
  }

  /**
   * Set the value and update the parent Set.
   * 
   * @param value
   */
  public void setValue(T value) {
    parentSet.remove(this.localValue);
    if (value != null) {
      parentSet.add(value);
    }
    this.localValue = value;
  }

  /**
   * Return the locally retained value.
   * 
   * @return
   */
  public T getValue() {
    return localValue;
  }

}

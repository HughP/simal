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
package uk.ac.osswatch.simal.model;

import java.util.Set;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A definition for a FOAF Resource
 */
public interface IFoafResource extends IResource {

  /**
   * Get the default name for this organisation.
   */
  public String getDefaultName();

  public void setDefaultName(String name);

  /**
   * Add a name to the organisation. Each organisation may have more than one
   * name.
   * 
   * @param name
   */
  public void addName(String name);


  /**
   * Get all names for this person. If there is no name defined then return the
   * givennames. If there are no given names either then return an empty set.
   * 
   * Note that this can sometimes give some confusing behaviour as getNames()
   * when there is no name will return a result, then after an addName(name)
   * call this method will return a different result without the original
   * givennames.
   * 
   * @TODO: consider changing this behaviour and having the client decide what
   *        to do when there is no name.
   * @return
   */
  public Set<String> getNames();

  /**
   * @param names
   */
  public void setNames(Set<String> names);

  /**
   * Remove a given name from this person.
   * 
   * @throws SimalRepositoryException if the name does not exist
   * 
   * @param name
   */
  public void removeName(String name) throws SimalRepositoryException;
}
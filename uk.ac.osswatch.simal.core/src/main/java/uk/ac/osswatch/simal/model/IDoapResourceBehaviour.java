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

import java.net.URL;
import java.util.Set;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * The definition of a behaviour for Elmo Doap Resources.
 */
public interface IDoapResourceBehaviour extends IResourceBehaviour {
  /**
   * Returns the default name for this resource/
   * 
   * If no names are supplied then the return value of getLabel() is returned.
   */
  public String getName();

  /**
   * Add a name.
   */
  public void addName(String name);
  
  /**
   * Get a human readable label for this resource.
   * Wherever posssible return a value degined by rds:label,
   * otherwise return a sensible value derived from other data.
   */
  public String getLabel();
  

  /**
   * Return all names associated with this resource. If no names are available
   * then a set containing a single value is returned. this value is generated
   * using a getLabel(true) method call.
   * 
   * @return
   */
  public abstract Set<String> getNames();

  public abstract String getShortDesc();

  public abstract void setShortDesc(String shortDesc);

  public abstract String getCreated();

  public abstract void setCreated(String newCreated)
      throws SimalRepositoryException;

  public abstract String getDescription();

  public abstract void setDescription(String newDescription);

  public abstract Set<String> getLicences();

  /**
   * Get a JSON representation of this resource.
   * 
   * @return
   */
  public abstract String toJSON();

  /**
   * Get a JSON representation of this resource as a JSON record, that is a
   * representation that is not a complete JSON file but is intended to be
   * inserted into another JSON file.
   * 
   * @return
   */
  public abstract String toJSONRecord();

  /**
   * Get the URL for this resource. Attempts to construct a URL
   * for this resource from the QName. This will often work if a resource
   * is defined by rdf:resource. If it is not possible to create an
   * URL for the resource an exception is thrown.
   * @throws SimalRepositoryException 
   */
  public URL getURL() throws SimalRepositoryException;

}
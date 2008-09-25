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

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * The definition of a behaviour for Elmo Doap Resources.
 */
public interface IDoapResourceService extends IResourceService {

  /**
   * Get a JSON representation of this resource.
   * 
   * @return
   */
  public abstract String toJSON() throws SimalRepositoryException;

  /**
   * Get a JSON representation of this resource as a JSON record, that is a
   * representation that is not a complete JSON file but is intended to be
   * inserted into another JSON file.
   * 
   * @return
   */
  public abstract String toJSONRecord() throws SimalRepositoryException;

}
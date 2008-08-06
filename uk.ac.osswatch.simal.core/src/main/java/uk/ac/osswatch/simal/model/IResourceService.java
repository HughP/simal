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
 * A wrapper for an RDFS Resource in Elmo.
 * 
 * @see org.opeenrdf.sesame.elmo.concepts.rdfs.IResourceService
 * 
 */
public interface IResourceService {

  /**
   * Create a JSON representation of this resource.
   * 
   * @param asRecord
   *          set to true if this is a record within another JSON file or false
   *          if you want this to be a complete JSON file.
   */
  public String toJSON(boolean asRecord);

  /**
   * Create an XML representation of this resource.
   * @throws SimalRepositoryException 
   * 
   */
  public String toXML() throws SimalRepositoryException;
  
  /**
   * Delete this resource from the repository.
   */
  public void delete() throws SimalRepositoryException;
  
  /**
   * Get all the sources for the data we hold about
   * this resource. This translates to the rdfs:seeAlso
   * triples.
   */
  public Set<String> getSources();
}

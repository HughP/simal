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

import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A behaviour for FOAF people objects.
 * 
 */
@rdf("http://xmlns.com/foaf/0.1/Person")
public interface IFoafPersonBehaviour extends IFoafResourceBehaviour {

  /**
   * Get all the colleagues of this person. A colleague is defined as anyone who
   * works on the same project as this person.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public Set<IPerson> getColleagues() throws SimalRepositoryException;
  

  /**
   * Get all the people that this person knows
   */
  public Set<IPerson> getKnows();

  /**
   * Get the label for this person. The label for a person is derived
   * from their known names. If the person does not have any defined
   * names then the toString() method is used..
   * 
   * @return
   */
  public String getLabel();

  /**
   * Get the email address for this person.
   * 
   * @return
   */
  public String getEmail();
}

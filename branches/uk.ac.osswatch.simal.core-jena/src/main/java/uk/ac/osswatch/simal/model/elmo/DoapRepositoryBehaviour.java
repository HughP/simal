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

import org.openrdf.concepts.doap.Repository;
import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.model.IDoapRepository;
import uk.ac.osswatch.simal.model.IDoapRepositoryBehaviour;

@rdf("http://usefulinc.com/ns/doap#repository")
public class DoapRepositoryBehaviour extends DoapResourceBehaviour implements
    IDoapRepositoryBehaviour {

  /**
   * Create a new repository behaviour to operate on a IRepository object.
   */
  public DoapRepositoryBehaviour(IDoapRepository repo) {
    super(repo);
  }

  /**
   * Get the anonymous access repositories.
   */
  public Set<String> getAnonRoots() {
    return Utilities.convertToSetOfStrings(getRepository().getDoapAnonRoots());
  }

  private Repository getRepository() {
    return (Repository) elmoEntity;
  }

  /**
   * Get the locations for this repository.
   */
  public Set<String> getLocations() {
    return Utilities.convertToSetOfStrings(getRepository().getDoapLocations());
  }

  /**
   * Get the browseble locations for this repository.
   */
  public Set<String> getBrowse() {
    return Utilities.convertToSetOfStrings(getRepository().getDoapBrowse());
  }
}

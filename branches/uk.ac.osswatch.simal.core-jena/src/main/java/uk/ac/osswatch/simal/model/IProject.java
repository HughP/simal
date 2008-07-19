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

import org.openrdf.concepts.doap.Project;
import org.openrdf.elmo.annotations.rdf;

/**
 * Extra data provided by simal.Project objects over and
 * above that provided by doap.Project objects.
 * 
 */
@rdf("http://usefulinc.com/ns/doap#Project")
public interface IProject extends Project, IDoapProjectBehaviour {

  /**
   * Get the Simal ID for this project. This is a unique identifier
   * within the repository from which it was retrieved.
   * 
   * @return 
   */
  @rdf("http://simal.oss-watch.ac.uk/ns/0.2/simal#projectId")
  public String getSimalID();

  /**
   * Set the Simal ID for this project. This is a unique identifier
   * within the repository from which it was retrieved.
   * 
   * @return 
   */
  public void setSimalID(String newID);
}

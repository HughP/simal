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

/**
 * A category is part of the project classification system. It is a resource
 * used to define the doap:category entries of a doap:Project.
 * 
 */
public interface IDoapCategory extends IDoapResource {
  /**
   * Get all the projects that are within this category.
   * 
   * @return
   */
  public Set<IProject> getProjects();

  /**
   * Get all the people that are working on projects in this category.
   * 
   * @return
   */
  public Set<IPerson> getPeople();

}

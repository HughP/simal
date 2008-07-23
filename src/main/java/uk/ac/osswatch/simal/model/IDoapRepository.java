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
 * A repository that is known to apply be used by a project
 * It is a resource used to define the doap:repository entries
 * of doap:Project.  
 *
 */
public interface IDoapRepository extends IDoapResource {

  Set<String> getAnonRoots();

  Set<String> getLocations();

  Set<String> getBrowse();

}

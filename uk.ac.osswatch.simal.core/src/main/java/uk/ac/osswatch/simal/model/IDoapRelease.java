/*
 * Copyright 2007, 2011 University of Oxford
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
 * A release of a project. It is a resource used to define the doap:Version
 * entries that are tied to instances of doap:Project using doap:release.
 * 
 */
public interface IDoapRelease extends IDoapResource {

  /**
   * Get all version identifiers for this release. A version identifier is a
   * version number. In DOAP it corresponds to doap:revision.
   */
  public Set<String> getRevisions();

  /**
   * Set all the version identifiers for this release. A version identifier is a
   * version number. In DOAP it corresponds to doap:revision.
   */
  public void setRevisions(Set<String> revisions);
}

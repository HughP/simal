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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.openrdf.concepts.doap.Version;
import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.model.IDoapRelease;
import uk.ac.osswatch.simal.model.IDoapReleaseBehaviour;

@rdf("http://usefulinc.com/ns/doap#release")
public class DoapReleaseBehaviour extends DoapResourceBehaviour implements IDoapReleaseBehaviour {

  /**
   * Create a new issue tracker behaviour to operate on a
   * IIssueTracker object.
   */
  public DoapReleaseBehaviour(IDoapRelease release) {
    super(release);
  }

  /**
   * Get all version identifiers for this release. A version
   * identifier is a version number. In DOAP it corresponds to
   * doap:revision.
   */
  public Set<String> getRevisions() {
    Version version = getVersionEntity();
    Iterator<Object> versions = version.getDoapRevisions().iterator();
    Set<String> result = new HashSet<String>(version.getDoapRevisions().size());
    while (versions.hasNext()) {
      result.add(versions.next().toString());
    }
    return result; 
  }
  
  private Version getVersionEntity() {
    return (Version)elmoEntity;
  }

}

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

import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.model.IDoapDownloadMirror;
import uk.ac.osswatch.simal.model.IDoapDownloadMirrorBehaviour;

@rdf("http://usefulinc.com/ns/doap#download-mirror")
public class DoapDownloadMirrorBehaviour extends DoapResourceBehaviour implements IDoapDownloadMirrorBehaviour {

  /**
   * Create a new download mirror behaviour to operate on a
   * ICategory object.
   */
  public DoapDownloadMirrorBehaviour(IDoapDownloadMirror mirror) {
    super(mirror);
  }

}

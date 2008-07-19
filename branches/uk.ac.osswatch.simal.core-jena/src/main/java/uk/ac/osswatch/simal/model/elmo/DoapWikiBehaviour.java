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

import uk.ac.osswatch.simal.model.IDoapWiki;
import uk.ac.osswatch.simal.model.IDoapWikiBehaviour;

@rdf("http://usefulinc.com/ns/doap#wiki")
public class DoapWikiBehaviour extends DoapResourceBehaviour implements IDoapWikiBehaviour {

  /**
   * Create a new wiki behaviour to operate on a
   * ICategory object.
   */
  public DoapWikiBehaviour(IDoapWiki wiki) {
    super(wiki);
  }

}

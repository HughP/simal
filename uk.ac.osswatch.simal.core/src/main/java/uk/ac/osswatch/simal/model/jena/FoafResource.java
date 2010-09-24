package uk.ac.osswatch.simal.model.jena;
/*
 * Copyright 2010 University of Oxford
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

import java.util.Set;

import com.hp.hpl.jena.sparql.vocabulary.FOAF;

import uk.ac.osswatch.simal.model.IFoafResource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class FoafResource extends Resource implements IFoafResource {

  private static final long serialVersionUID = 1685790657270805922L;

  public FoafResource(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
  }

  @Override
  public String getName() {
    // TODO Auto-generated method stub
    return null;
  }

  public void addName(String name) {
    getJenaResource().addProperty(FOAF.name, name);
  }

  @Override
  public Set<String> getNames() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setNames(Set<String> names) {
    // TODO Auto-generated method stub

  }

  @Override
  public void removeName(String name) throws SimalRepositoryException {
    // TODO Auto-generated method stub

  }

}

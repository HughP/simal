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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import uk.ac.osswatch.simal.model.IFoafResource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class FoafResource extends Resource implements IFoafResource {

  private static final long serialVersionUID = 1685790657270805922L;

  public FoafResource(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
  }

  public void setDefaultName(String name) {
    if (name != null && name.length() > 0) {
      addLiteralStatement(FOAF.name, name);
      addLiteralStatement(RDFS.label, name);
    }
  }

  public String getDefaultName() {
    String name = getLiteralValue(RDFS.label);

    if (name == null) {
      name = getLiteralValue(FOAF.name);
    }

    if (name == null) {
      name = getURI();
    }
    
    return name;
  }

  public void addName(String name) {
    addLiteralStatement(FOAF.name, name);
  }

  public Set<String> getNames() {
    Iterator<Statement> itr = listProperties(FOAF.name).iterator();
    Set<String> names = new HashSet<String>();
    while (itr.hasNext()) {
      names.add(itr.next().getString());
    }

    if (names.size() == 0) {
      names.add(getURI());
    }
    
    return names;
  }

  public void setNames(Set<String> names) {
    replaceLiteralStatements(FOAF.name, names);
  }

  public void removeName(String name) throws SimalRepositoryException {
    removeLiteralStatement(FOAF.name, name);
    removeLiteralStatement(RDFS.label, name);
  }

}

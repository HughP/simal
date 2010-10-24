package uk.ac.osswatch.simal.model.jena;

/*
 * 
 Copyright 2007,2010 University of Oxford * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

import java.util.Set;

import uk.ac.osswatch.simal.model.IDocument;
import uk.ac.osswatch.simal.model.IncompatibleTypeException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.RDF;

public class Document extends FoafResource implements IDocument {

  private static final long serialVersionUID = 1908107004236890792L;

  public Document(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
  }

  public IDocument ensureDocumentType() throws IncompatibleTypeException {
    Resource r = getJenaResource();
    Model model = r.getModel();
    Set<Statement> set = r.listProperties(RDF.type).toSet();

    if (set.size() > 0) {
      Resource type = set.iterator().next().getResource();
      if (!type.equals(FOAF.Document)) {
        throw new IncompatibleTypeException("Expected resource " + r
            + " to be of type " + FOAF.Document + " but is of type " + type);
      }
    } else {
      Statement s = model.createStatement(r, RDF.type, FOAF.Document);
      model.add(s);
    }
    return this;
  }
}

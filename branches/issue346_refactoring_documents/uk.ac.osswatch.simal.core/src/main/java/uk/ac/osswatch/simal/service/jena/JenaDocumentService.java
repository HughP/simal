package uk.ac.osswatch.simal.service.jena;

/*
 * Copyright 2007,2010 University of Oxford 
 *  
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IDocument;
import uk.ac.osswatch.simal.model.IncompatibleTypeException;
import uk.ac.osswatch.simal.model.jena.Document;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.IDocumentService;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.RDF;

public class JenaDocumentService extends JenaService implements
    IDocumentService {
  public static final Logger logger = LoggerFactory
      .getLogger(JenaDocumentService.class);

  public JenaDocumentService(ISimalRepository repo) {
    super(repo);
  }

  public IDocument create(String uri) throws SimalRepositoryException,
      DuplicateURIException {
    if (uri == null || uri.length() == 0) {
      throw new SimalRepositoryException("URI cannot be blank or null");
    }
    Model model = getModel();
    if (SimalRepositoryFactory.getInstance().containsResource(uri)) {
      throw new DuplicateURIException(
          "Attempt to create a second homepage with the URI " + uri);
    }

    com.hp.hpl.jena.rdf.model.Resource r = model.createResource(uri);
    Statement s = model.createStatement(r, RDF.type, FOAF.Document);
    model.add(s);

    IDocument page = new Document(r);
    return page;
  }

  public IDocument get(String uri) throws SimalRepositoryException, IncompatibleTypeException {
    IDocument document = null;

    if (getRepository().containsResource(uri)) {
      Resource docResource = getModel().getResource(uri);
      document = new Document(docResource).ensureDocumentType();
    }

    return document;
  }

  public IDocument getOrCreate(String url) throws SimalRepositoryException, IncompatibleTypeException {
    Document document;
    
    if (getRepository().containsResource(url)) {
      document = (Document) get(url);
    } else {
      Model model = getModel();
      document = new Document(model.createResource(url));
    }

    return document.ensureDocumentType();
  }

}

package uk.ac.osswatch.simal.service.jena;
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

import java.net.URI;
import java.net.URISyntaxException;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.model.IDoapRepository;
import uk.ac.osswatch.simal.model.jena.Repository;
import uk.ac.osswatch.simal.model.jena.simal.JenaSimalRepository;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.InvalidURIException;
import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;
import uk.ac.osswatch.simal.service.IRepositoryService;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.sparql.vocabulary.DOAP;
import com.hp.hpl.jena.vocabulary.RDF;

public class JenaRepositoryService extends JenaService implements
    IRepositoryService {

  private static final Logger LOGGER = LoggerFactory
  .getLogger(JenaRepositoryService.class);

  public JenaRepositoryService(ISimalRepository simalRepository) {
    super(simalRepository);
  }

  public IDoapRepository create(String uri) throws SimalRepositoryException,
      DuplicateURIException, InvalidURIException {
    ensureValidURI(uri);

    Model model = ((JenaSimalRepository) getRepository()).getModel();

    com.hp.hpl.jena.rdf.model.Resource r = model.createResource(uri);
    Statement s = model.createStatement(r, RDF.type, DOAP.Repository);
    model.add(s);

    return new Repository(r);
  }
  

  public IDoapRepository create() throws SimalException {
    String uri = RDFUtils.getDefaultRepositoryURI(getNewID());
    IDoapRepository repo = null;
    try {
      repo = create(uri);
    } catch (DuplicateURIException e) {
      String msg = "Generated URI unexpectedly duplicate : " + uri;
      LOGGER.warn(msg);
      throw new SimalException(msg, e);
    } catch (InvalidURIException e) {
      String msg = "Generated URI unexpectedly invalid : " + uri;
      LOGGER.warn(msg);
      throw new SimalException(msg, e);
    }
    return repo;
  }
  
  private void ensureValidURI(String uri) throws DuplicateURIException, InvalidURIException {
    if (StringUtils.isEmpty(uri)) {
      throw new IllegalArgumentException("uri cannot be empty.");
    }

    try {
      new URI(uri);
    } catch (URISyntaxException e) {
      throw new InvalidURIException("Invalid URI; error: " + e.getMessage());
    }
    
    if (getRepository().containsResource(uri)) {
      throw new DuplicateURIException(
          "Attempt to create a repository with a URI that already exists: " + uri);
    }
  }

  public String getNewID() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return getNewID(SimalProperties.PROPERTY_SIMAL_NEXT_RCS_ID, "rcs");
  }

  public IDoapRepository getById(String fullID) {
    // TODO Auto-generated method stub
    return null;
  }

}

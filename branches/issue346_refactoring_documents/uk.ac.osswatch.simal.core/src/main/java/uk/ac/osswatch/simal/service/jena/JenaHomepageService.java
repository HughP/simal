package uk.ac.osswatch.simal.service.jena;
/*
 * Copyright 2007 University of Oxford 
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

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IDocument;
import uk.ac.osswatch.simal.model.jena.Document;
import uk.ac.osswatch.simal.model.jena.simal.JenaSimalRepository;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.IHomepageService;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

public class JenaHomepageService extends JenaService implements IHomepageService {
  public static final Logger logger = LoggerFactory
      .getLogger(JenaHomepageService.class);

	public JenaHomepageService(ISimalRepository repo) {
		super(repo);
	}


  public IDocument create(String uri) throws SimalRepositoryException,
      DuplicateURIException {
	if (uri == null || uri.length() == 0) {
		throw new SimalRepositoryException("URI cannot be blank or null");
	}
	Model model = ((JenaSimalRepository)getRepository()).getModel();
    if (SimalRepositoryFactory.getInstance().containsResource(uri)) {
      throw new DuplicateURIException(
          "Attempt to create a second homepage with the URI " + uri);
    }

    Property o = model.createProperty("http://usefulinc.com/ns/doap#homepage");
    com.hp.hpl.jena.rdf.model.Resource r = model.createResource(uri);
    Statement s = model.createStatement(r, RDF.type, o);
    model.add(s);

    IDocument page = new Document(r);
    page.setSimalID(getNewID());
    return page;
  }

	public String getNewID() throws SimalRepositoryException {
	    String fullID = null;
	    String strEntityID = SimalProperties.getProperty(
	        SimalProperties.PROPERTY_SIMAL_NEXT_HOMEPAGE_ID, "1");
	    long entityID = Long.parseLong(strEntityID);

	    /**
	     * If the properties file is lost for any reason the next ID value will be
	     * lost. We therefore need to perform a sanity check that this is unique.
	     * 
	     * @FIXME: the ID should really be held in the database then there would be
	     *         no need for this time consuming verification See ISSUE 190
	     */
	    boolean validID = false;
	    while (!validID) {
	      fullID = SimalRepositoryFactory.getInstance().getUniqueSimalID("page" + Long.toString(entityID));
	      logger.debug("Checking to see if homepage ID of {} is available", fullID);
	      if (SimalRepositoryFactory.getProjectService().getProjectById(fullID) == null) {
	        validID = true;
	      } else {
	        entityID = entityID + 1;
	      }
	    }

	    long newId = entityID + 1;
	    SimalProperties.setProperty(SimalProperties.PROPERTY_SIMAL_NEXT_HOMEPAGE_ID,
	        Long.toString(newId));
	    try {
	      SimalProperties.save();
	    } catch (Exception e) {
	      logger.warn("Unable to save properties file", e);
	      throw new SimalRepositoryException(
	          "Unable to save properties file when creating the next project ID", e);
	    }
	    logger.debug("Generated new homepage ID {}", fullID);
	    return fullID;
	}

  public IDocument get(String uri) throws SimalRepositoryException {
    if (getRepository().containsResource(uri)) {
      Model model = ((JenaSimalRepository) getRepository()).getModel();
      return new Document(model.getResource(uri));
    } else {
      return null;
    }
  }

	public IDocument getOrCreate(String url)
			throws SimalRepositoryException {
    if (getRepository().containsResource(url)) {
      return get(url);
    } else {
      Model model = ((JenaSimalRepository)getRepository()).getModel();
      return new Document(model.getResource(url));
    }
  }


  public Set<IDocument> getAll() {
    Model model = ((JenaSimalRepository)getRepository()).getModel();
    Property homepage = model.createProperty(JenaSimalRepository.DOAP_HOMEPAGE_URI);
    StmtIterator itr = model.listStatements(null, homepage, (String)null);
    Set<IDocument> pages = new HashSet<IDocument>();
    while (itr.hasNext()) {
      String uri = ((Resource)itr.nextStatement().getObject()).getURI();
      pages.add(new Document(model.getResource(uri)));
    }
    return pages;
  }
}

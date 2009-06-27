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
import java.net.URI;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.jena.simal.JenaSimalRepository;
import uk.ac.osswatch.simal.model.jena.simal.Review;
import uk.ac.osswatch.simal.model.simal.IReview;
import uk.ac.osswatch.simal.model.simal.SimalOntology;
import uk.ac.osswatch.simal.rdf.AbstractSimalRepository;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;
import uk.ac.osswatch.simal.service.AbstractService;
import uk.ac.osswatch.simal.service.IReviewService;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * A class for working with projects in the repository.
 * 
 */
public class JenaReviewService extends AbstractService implements IReviewService {
  private static final Logger logger = LoggerFactory
      .getLogger(JenaReviewService.class);

	public JenaReviewService(ISimalRepository simalRepository) {
		setRepository(simalRepository);
	};
	
	public Set<IReview> getReviews() {
		JenaSimalRepository simalRepository = (JenaSimalRepository)getRepository();
		Model model = simalRepository.getModel();
	    StmtIterator itr = model.listStatements(null, RDF.type, SimalOntology.REVIEW);
        
        Set<IReview> reviews = new HashSet<IReview>();
	    while (itr.hasNext()) {
	      String uri = itr.nextStatement().getSubject().getURI();
	      reviews.add(new Review(model.getResource(uri)));
	    }
	    return reviews;
	}
	
	public Set<IReview> getReviewsForProject(IProject project) throws SimalRepositoryException {
		// FIXME: there must be a better way of doing this using SPARQL
		
		Iterator<URI> seeAlso = project.getSeeAlso().iterator();
		String queryStr = "PREFIX simal: <" + AbstractSimalRepository.SIMAL_NAMESPACE_URI
        + "> " + "PREFIX rdf: <" + AbstractSimalRepository.RDF_NAMESPACE_URI + "> "
        + "PREFIX rdfs: <" + AbstractSimalRepository.RDFS_NAMESPACE_URI + ">"
        + "SELECT DISTINCT ?review WHERE { " + "?review a simal:Review . "
        + "?review simal:Project <" + project.getURI() + ">} ";
		HashSet<IReview> reviews = findReviewsBySPARQL(queryStr);

		while (seeAlso.hasNext()) {
			queryStr = "PREFIX simal: <" + AbstractSimalRepository.SIMAL_NAMESPACE_URI
	        + "> " + "PREFIX rdf: <" + AbstractSimalRepository.RDF_NAMESPACE_URI + "> "
	        + "PREFIX rdfs: <" + AbstractSimalRepository.RDFS_NAMESPACE_URI + ">"
	        + "SELECT DISTINCT ?review WHERE { " + "?review a simal:Review . "
	        + "?review simal:Project <" + seeAlso.next() + ">} ";
			reviews.addAll(findReviewsBySPARQL(queryStr));
		}
		return reviews;
	}
	  
	  public HashSet<IReview> findReviewsBySPARQL(String queryStr) {
	    Model model = ((JenaSimalRepository)getRepository()).getModel();
	    Query query = QueryFactory.create(queryStr);
	    QueryExecution qe = QueryExecutionFactory.create(query, model);
	    ResultSet results = qe.execSelect();

	    HashSet<IReview> reviews = new HashSet<IReview>();
	    while (results.hasNext()) {
	      QuerySolution soln = results.nextSolution();
	      RDFNode node = soln.get("review");
	      if (node.isResource()) {
	        reviews.add(new Review((com.hp.hpl.jena.rdf.model.Resource) node));
	      }
	    }
	    qe.close();
	    return reviews;
	  }
	  
	  public String getNewReviewID() throws SimalRepositoryException {
	        StringBuilder fullID = new StringBuilder();
		    String strEntityID = SimalProperties.getProperty(
		        SimalProperties.PROPERTY_SIMAL_NEXT_REVIEW_ID, "1");
		    long entityID = Long.parseLong(strEntityID);

		    /**
		     * If the properties file is lost for any reason the next ID value will be
		     * lost. We therefore need to perform a sanity check that this is unique.
		     */
		    boolean validID = false;
		    while (!validID) {
		      String instanceID = SimalProperties
		        .getProperty(SimalProperties.PROPERTY_SIMAL_INSTANCE_ID);
		      fullID.append(instanceID);
		      fullID.append("-");
		      fullID.append("rev" + Long.toString(entityID));
		      if (findReviewById(fullID.toString()) == null) {
		        validID = true;
		      } else {
		        entityID = entityID + 1;
		      }
		    }

		    long newId = entityID + 1;
		    SimalProperties.setProperty(SimalProperties.PROPERTY_SIMAL_NEXT_REVIEW_ID,
		        Long.toString(newId));
		    try {
		      SimalProperties.save();
		    } catch (Exception e) {
		      logger.warn("Unable to save properties file", e);
		      throw new SimalRepositoryException(
		          "Unable to save properties file when creating the next person ID", e);
		    }

		    return fullID.toString();
		  }

	  public IReview findReviewById(String id) throws SimalRepositoryException {
	    String queryStr = "PREFIX xsd: <" + AbstractSimalRepository.XSD_NAMESPACE_URI
	        + "> " + "PREFIX rdf: <" + AbstractSimalRepository.RDF_NAMESPACE_URI + ">"
	        + "PREFIX simal: <" + AbstractSimalRepository.SIMAL_NAMESPACE_URI + ">"
	        + "SELECT DISTINCT ?review WHERE { " + "?review simal:reviewId \"" + id
	        + "\"^^xsd:string }";
	    HashSet<IReview> reviews = findReviewsBySPARQL(queryStr);
	    if (reviews.size() == 0) {
	    	return null;
	    }
	    if (reviews.size() > 1) {
	    	throw new SimalRepositoryException("Mulitple reviews found with ID = " + id);
	    }
	    return (IReview) reviews.toArray()[0];
	  }

	public IReview create(String uri) throws DuplicateURIException, SimalRepositoryException {
	    if (containsReview(uri)) {
	      throw new DuplicateURIException(
	          "Attempt to create a second review with the URI " + uri);
	    }
		Model model = ((JenaSimalRepository)getRepository()).getModel();
	    
	    String simalReviewURI;
	    if (!uri.startsWith(RDFUtils.SIMAL_REVIEW_NAMESPACE_URI)) {
		    String reviewID = getNewReviewID();
		    simalReviewURI = RDFUtils.getDefaultReviewURI(reviewID);
		    logger.debug("Creating a new Simal review instance with URI: "
		        + simalReviewURI);
	    } else {
	        simalReviewURI = uri;
	    }

	    com.hp.hpl.jena.rdf.model.Resource r = model.createResource(simalReviewURI);
	    Statement s = model.createStatement(r, RDF.type, SimalOntology.REVIEW);
	    model.add(s);
	        
	    if (!uri.startsWith(RDFUtils.PROJECT_NAMESPACE_URI)) {
	        com.hp.hpl.jena.rdf.model.Resource res = model.createResource(uri);
	        s = model.createStatement(r, RDFS.seeAlso, res);
	        model.add(s);
	      }

	    IReview review = new Review(r);
	    review.setSimalID(getNewReviewID());
	    return review;
	  }
	
	  public boolean containsReview(String uri) {
		Model model = ((JenaSimalRepository)getRepository()).getModel();
	    Property o = model.createProperty(RDFUtils.SIMAL_REVIEW_NAMESPACE_URI);
	    com.hp.hpl.jena.rdf.model.Resource r = model.createResource(uri);
	    Statement doap = model.createStatement(r, RDF.type, o);

	    o = model.createProperty(RDFUtils.SIMAL_PROJECT);
	    Statement simal = model.createStatement(r, RDF.type, o);
	    return model.contains(doap) || model.contains(simal);
	  }

	public IReview getReview(String uri) {
	    if (containsReview(uri)) {
	      return new Review(((JenaSimalRepository)getRepository()).getModel().getResource(uri));
	    } else {
	      return null;
	    }
	}
		  

	
}

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
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.jena.Project;
import uk.ac.osswatch.simal.model.jena.simal.JenaSimalRepository;
import uk.ac.osswatch.simal.model.simal.SimalOntology;
import uk.ac.osswatch.simal.rdf.AbstractSimalRepository;
import uk.ac.osswatch.simal.rdf.Doap;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;
import uk.ac.osswatch.simal.service.IProjectService;

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
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * A class for working with projects in the repository.
 * 
 */
public class JenaProjectService extends JenaService implements IProjectService {
  public static final Logger logger = LoggerFactory
      .getLogger(JenaProjectService.class);

	public JenaProjectService(ISimalRepository simalRepository) {
		super(simalRepository);
	};
	
	public Set<IProject> getProjectsWithRCS() throws SimalRepositoryException {
		return getProjectsWith(Doap.REPOSITORY);
	}
	
	public Set<IProject> getProjectsWithoutRCS() throws SimalRepositoryException {
		return getProjectsWithout(Doap.REPOSITORY);
	}

	public Set<IProject> getProjectsWithHomepage() throws SimalRepositoryException {
		return getProjectsWith(Doap.HOMEPAGE);
	}

	public Set<IProject> getProjectsWithoutHomepage() throws SimalRepositoryException {
		return getProjectsWithout(Doap.HOMEPAGE);
	}

	public Set<IProject> getProjectsWithMaintainer() throws SimalRepositoryException {
		return getProjectsWith(Doap.MAINTAINER);
	}

	public Set<IProject> getProjectsWithMailingList() throws SimalRepositoryException {
		return getProjectsWith(Doap.MAILING_LIST);
	}

	public Set<IProject> getProjectsWithBugDatabase() throws SimalRepositoryException {
		return getProjectsWith(Doap.BUG_DATABASE);
	}

	public Set<IProject> getProjectsWithRelease() throws SimalRepositoryException {
		return getProjectsWith(Doap.RELEASE);
	}
	
	private Set<IProject> getProjectsWith(Property property) throws SimalRepositoryException {
		String queryStr = "PREFIX simal: <" + AbstractSimalRepository.SIMAL_NAMESPACE_URI
        + "> " + "PREFIX rdf: <" + AbstractSimalRepository.RDF_NAMESPACE_URI + "> "
        + "PREFIX rdfs: <" + AbstractSimalRepository.RDFS_NAMESPACE_URI + ">"
        + "PREFIX doap: <" + Doap.NS + ">"
        + "SELECT DISTINCT ?project WHERE { ?doapProject a doap:Project . "
        + "?doapProject <" + property.getURI() + "> ?value . "
        + " ?project a simal:Project . "
        + " ?project rdfs:seeAlso ?doapProject }";

	    return findProjectsBySPARQL(queryStr);
	}
	
	/**
	 * Get all proejcts that do not have a given property.
	 * 
	 * @param property
	 * @return
	 * @throws SimalRepositoryException
	 */
	private Set<IProject> getProjectsWithout(Property property) throws SimalRepositoryException {
		String queryStr = "PREFIX simal: <" + AbstractSimalRepository.SIMAL_NAMESPACE_URI
        + "> " + "PREFIX rdf: <" + AbstractSimalRepository.RDF_NAMESPACE_URI + "> "
        + "PREFIX rdfs: <" + AbstractSimalRepository.RDFS_NAMESPACE_URI + ">"
        + "PREFIX doap: <" + Doap.NS + ">"
        + "SELECT DISTINCT ?project WHERE { { "
        + " ?project a simal:Project . "
        + "OPTIONAL { "
        + " ?doapProject a doap:Project . "
        + " ?project rdfs:seeAlso ?doapProject . "
        + " ?doapProject <" + property.getURI() + "> ?value }  "
        + "FILTER (!bound(?value)) "
        + " } }";

	    return findProjectsBySPARQL(queryStr);
	}

	  public IProject getProject(String uri) throws SimalRepositoryException {
		if(uri.startsWith(RDFUtils.PROJECT_NAMESPACE_URI)) {
		    if (containsProject(uri)) {
		      return new Project(((JenaSimalRepository)getRepository()).getModel().getResource(uri));
		    } else {
		      return null;
		    }
		} else {
			return findProjectBySeeAlso(uri);
		}
	  }
	  
	  public boolean containsProject(String uri) {
		  Model model = ((JenaSimalRepository)getRepository()).getModel();
		    Property o = model.createProperty("http://usefulinc.com/ns/doap#Project");
		    com.hp.hpl.jena.rdf.model.Resource r = model.createResource(uri);
		    Statement doap = model.createStatement(r, RDF.type, o);

		    o = model.createProperty(RDFUtils.SIMAL_PROJECT);
		    Statement simal = model.createStatement(r, RDF.type, o);
		    return model.contains(doap) || model.contains(simal);
		  }


	  public IProject findProjectBySeeAlso(String seeAlso)
	      throws SimalRepositoryException {
		String queryStr = "PREFIX simal: <" + AbstractSimalRepository.SIMAL_NAMESPACE_URI
	        + "> " + "PREFIX rdf: <" + AbstractSimalRepository.RDF_NAMESPACE_URI + "> "
	        + "PREFIX doap: <" + Doap.NS + ">"
	        + "PREFIX rdfs: <" + AbstractSimalRepository.RDFS_NAMESPACE_URI + ">"
	        + "SELECT DISTINCT ?project WHERE { " + "?project a simal:Project . "
	        + "?project rdfs:seeAlso <" + seeAlso + ">} ";
		
	    Set<IProject> projects = findProjectsBySPARQL(queryStr);

	    if (projects.size() == 0) {
	    	return null;
	    } else {
	      return (IProject) projects.toArray()[0];
	    }
	  }  
	  

	  /**
	   * Find all projects returned using a SPARQL query.
	   * 
	   * @param queryStr
	   * @return
	   */
	  public Set<IProject> findProjectsBySPARQL(String queryStr) {
		Model model = ((JenaSimalRepository)getRepository()).getModel();
	    Query query = QueryFactory.create(queryStr);
	    QueryExecution qe = QueryExecutionFactory.create(query, model);
	    ResultSet results = qe.execSelect();
		
	    Set<IProject> projects = new HashSet<IProject>();
	    while (results.hasNext()) {
	      QuerySolution soln = results.nextSolution();
	      RDFNode node = soln.get("project");
	      if (node.isResource()) {
	        projects.add(new Project((com.hp.hpl.jena.rdf.model.Resource) node));
	      }
	    }
	    qe.close();
	    return projects;
	  }

	public Set<IProject> getProjectsWithReview() throws SimalRepositoryException {
		String queryStr = "PREFIX simal: <" + AbstractSimalRepository.SIMAL_NAMESPACE_URI
        + "> " + "PREFIX rdf: <" + AbstractSimalRepository.RDF_NAMESPACE_URI + "> "
        + "PREFIX rdfs: <" + AbstractSimalRepository.RDFS_NAMESPACE_URI + ">"
        + "PREFIX doap: <" + Doap.NS + ">"
        + "SELECT DISTINCT ?project WHERE {" 
        + "?review a simal:Review . "
        + "?review simal:Project ?project }";
		
	    return findProjectsBySPARQL(queryStr);
	}

	public Set<IProject> getProjectsWithoutReview() throws SimalRepositoryException {
		String queryStr = "PREFIX simal: <" + AbstractSimalRepository.SIMAL_NAMESPACE_URI
        + "> " + "PREFIX rdf: <" + AbstractSimalRepository.RDF_NAMESPACE_URI + "> "
        + "PREFIX rdfs: <" + AbstractSimalRepository.RDFS_NAMESPACE_URI + ">"
        + "PREFIX doap: <" + Doap.NS + ">"
        + "SELECT DISTINCT ?project WHERE {"
        + "?project rdf:type doap:Project . "
        + "OPTIONAL { "
        + "?review a simal:Review . "
        + "?review simal:Project ?project "
        + "} "
        + "FILTER (!bound(?review)) }";
		return findProjectsBySPARQL(queryStr);
	}

	
	  public IProject getOrCreateProject(String uri)
	  		throws SimalRepositoryException {
		if (SimalRepositoryFactory.getInstance().containsResource(uri)) {
			return getProject(uri);
		} else {
			IProject project = findProjectBySeeAlso(uri);
			if (project == null) {
				try {
					return createProject(uri);
				} catch (DuplicateURIException e) {
					logger.error("Threw a DuplicateURIEception when we had already checked for resource existence", e);
					return null;
				}
			} else {
				return project;
			}
		}
	  }
	  
	  public IProject createProject(String uri) throws SimalRepositoryException,
	      DuplicateURIException {
	    if (containsProject(uri)) {
	      throw new DuplicateURIException(
	          "Attempt to create a second project with the URI " + uri);
	    }
		Model model = ((JenaSimalRepository)getRepository()).getModel();
	    
	    String simalProjectURI;
	    if (!uri.startsWith(RDFUtils.PROJECT_NAMESPACE_URI)) {
		    String projectID = getNewProjectID();
		    simalProjectURI = RDFUtils.getDefaultProjectURI(projectID);
		    logger.debug("Creating a new Simal Projectinstance with URI: "
		        + simalProjectURI);
	    } else {
	        simalProjectURI = uri;
	    }

	    com.hp.hpl.jena.rdf.model.Resource r = model.createResource(simalProjectURI);
	    Statement s = model.createStatement(r, RDF.type, SimalOntology.PROJECT);
	    model.add(s);
	        
	    if (!uri.startsWith(RDFUtils.PROJECT_NAMESPACE_URI)) {
	        com.hp.hpl.jena.rdf.model.Resource res = model.createResource(uri);
	        s = model.createStatement(r, RDFS.seeAlso, res);
	        model.add(s);
	      }

	    IProject project = new Project(r);
	    project.setSimalID(getNewProjectID());
	    return project;
	  }
	  
	  public String getNewProjectID() throws SimalRepositoryException {
		    String fullID = null;
		    String strEntityID = SimalProperties.getProperty(
		        SimalProperties.PROPERTY_SIMAL_NEXT_PROJECT_ID, "1");
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
		      fullID = SimalRepositoryFactory.getInstance().getUniqueSimalID("prj" + Long.toString(entityID));
		      logger.debug("Checking to see if project ID of {} is available", fullID);
		      if (getProjectById(fullID) == null) {
		        validID = true;
		      } else {
		        entityID = entityID + 1;
		      }
		    }

		    long newId = entityID + 1;
		    SimalProperties.setProperty(SimalProperties.PROPERTY_SIMAL_NEXT_PROJECT_ID,
		        Long.toString(newId));
		    try {
		      SimalProperties.save();
		    } catch (Exception e) {
		      logger.warn("Unable to save properties file", e);
		      throw new SimalRepositoryException(
		          "Unable to save properties file when creating the next project ID", e);
		    }
		    logger.debug("Generated new project ID {}", fullID);
		    return fullID;
		  }
	  
	  public IProject getProjectById(String id) throws SimalRepositoryException {
		    logger.debug("Attempting to get a project with the id " + id);
		    if (!SimalRepositoryFactory.getInstance().isValidSimalID(id)) {
		      logger.info(
		          "Attempt to find a project using an invalid Simal ID of "
		              + id
		              + " attempting to create a valid ID using ISimalRepository.getUniqueSimalID(id)");
		      id = JenaSimalRepository.getInstance().getUniqueSimalID(id);
		      logger.info("Trying to retrieve project using unique ID of " + id);
		      return getProjectById(id);
		    }
		    String queryStr = "PREFIX xsd: <" + AbstractSimalRepository.XSD_NAMESPACE_URI
		        + "> " + "PREFIX rdf: <" + AbstractSimalRepository.RDF_NAMESPACE_URI + ">"
		        + "PREFIX simal: <" + AbstractSimalRepository.SIMAL_NAMESPACE_URI + ">"
		        + "SELECT DISTINCT ?project WHERE { " + "?project simal:projectId \""
		        + id + "\"^^xsd:string }";

		    IProject project = findProjectBySPARQL(queryStr);

		    if (project == null) {
		      logger.debug("No project with that ID found");
		    } else {
		      logger.debug("Retrieved project name: " + project.getName());
		    }
		    
		    return project;
		  }
	  
	  /**
	   * Find a single project by executing a SPARQL query.
	   * If the query returns more than one result then
	   * only one is returned.
	   * 
	   * @param queryStr
	   * @return
	   * 
	   */
	  private IProject findProjectBySPARQL(String queryStr) {
		logger.debug("Get project with SPARQL:\n" + queryStr);
		Model model = ((JenaSimalRepository)getRepository()).getModel();
	    Query query = QueryFactory.create(queryStr);
	    QueryExecution qe = QueryExecutionFactory.create(query, model);
	    ResultSet results = qe.execSelect();

	    IProject project = null;
	    while (results.hasNext()) {
	      QuerySolution soln = results.nextSolution();
	      RDFNode node = soln.get("project");
	      if (node.isResource()) {
	        project = new Project((com.hp.hpl.jena.rdf.model.Resource) node);
	      }
	    }
	    qe.close();
	    return project;
	  }

}

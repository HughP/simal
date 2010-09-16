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
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
import uk.ac.osswatch.simal.rdf.io.RDFXMLUtils;
import uk.ac.osswatch.simal.service.IProjectService;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryException;
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
  private static final Logger LOGGER = LoggerFactory
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

	public Set<IProject> getProjectsWithoutMaintainer() throws SimalRepositoryException {
		return getProjectsWithout(Doap.MAINTAINER);
	}
	
	public Set<IProject> getProjectsWithMailingList() throws SimalRepositoryException {
		return getProjectsWith(Doap.MAILING_LIST);
	}

	public Set<IProject> getProjectsWithoutMailingList() throws SimalRepositoryException {
		return getProjectsWithout(Doap.MAILING_LIST);
	}
	
	public Set<IProject> getProjectsWithBugDatabase() throws SimalRepositoryException {
		return getProjectsWith(Doap.BUG_DATABASE);
	}

	public Set<IProject> getProjectsWithoutBugDatabase() throws SimalRepositoryException {
		return getProjectsWithout(Doap.BUG_DATABASE);
	}
	
	public Set<IProject> getProjectsWithRelease() throws SimalRepositoryException {
		return getProjectsWith(Doap.RELEASE);
	}

	public Set<IProject> getProjectsWithoutRelease() throws SimalRepositoryException {
		return getProjectsWithout(Doap.RELEASE);
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
	    // FIXME This needs rework; will sometimes return doap:Project, sometimes simal:Project    
		if(uri.startsWith(RDFUtils.PROJECT_NAMESPACE_URI)) {
		    if (containsProject(uri)) {
		      return new Project(getModel().getResource(uri));
		    } else {
		      return null;
		    }
		} else {
			return findProjectBySeeAlso(uri);
		}
	  }
	  
	  public boolean containsProject(String uri) {
		  Model model = getModel();
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
	   * @throws SimalRepositoryException 
	   * @TODO should be private 
	   */
	  public Set<IProject> findProjectsBySPARQL(String queryStr) throws SimalRepositoryException {
      Set<IProject> projects = new HashSet<IProject>();
      QueryExecution qe = null;
      try {
        Query query = QueryFactory.create(queryStr);
        qe = QueryExecutionFactory.create(query, getModel());
        ResultSet results = qe.execSelect();
            
        while (results.hasNext()) {
          QuerySolution soln = results.nextSolution();
          RDFNode node = soln.get("project");
          if (node.isResource()) {
            projects.add(new Project((com.hp.hpl.jena.rdf.model.Resource) node));
          }
        }
      } catch (QueryException e) {
        String message = "QueryException when trying to SPARQLquery projects with query: " + queryStr;
        LOGGER.warn(message + "; message: " + e.getMessage());
        throw new SimalRepositoryException(message, e);
      } finally {
        if (qe != null) {
          qe.close();
        }
      }
      
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
					LOGGER.error("Threw a DuplicateURIEception when we had already checked for resource existence", e);
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
		Model model = getModel();
	    
	    String simalProjectURI;
	    if (!uri.startsWith(RDFUtils.PROJECT_NAMESPACE_URI)) {
		    String projectID = getNewProjectID();
		    simalProjectURI = RDFUtils.getDefaultProjectURI(projectID);
		    LOGGER.debug("Creating a new Simal Projectinstance with URI: "
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
		      LOGGER.debug("Checking to see if project ID of {} is available", fullID);
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
		      LOGGER.warn("Unable to save properties file", e);
		      throw new SimalRepositoryException(
		          "Unable to save properties file when creating the next project ID", e);
		    }
		    LOGGER.debug("Generated new project ID {}", fullID);
		    return fullID;
		  }
	  
	  public IProject getProjectById(String id) throws SimalRepositoryException {
		    LOGGER.debug("Attempting to get a project with the id " + id);
		    if (!SimalRepositoryFactory.getInstance().isValidSimalID(id)) {
		      LOGGER.info(
		          "Attempt to find a project using an invalid Simal ID of "
		              + id
		              + " attempting to create a valid ID using ISimalRepository.getUniqueSimalID(id)");
		      id = JenaSimalRepository.getInstance().getUniqueSimalID(id);
		      LOGGER.info("Trying to retrieve project using unique ID of " + id);
		      return getProjectById(id);
		    }
		    String queryStr = "PREFIX xsd: <" + AbstractSimalRepository.XSD_NAMESPACE_URI
		        + "> " + "PREFIX rdf: <" + AbstractSimalRepository.RDF_NAMESPACE_URI + ">"
		        + "PREFIX simal: <" + AbstractSimalRepository.SIMAL_NAMESPACE_URI + ">"
		        + "SELECT DISTINCT ?project WHERE { " + "?project simal:projectId \""
		        + id + "\" }";

		    IProject project = findProjectBySPARQL(queryStr);

		    if (project == null) {
		      LOGGER.debug("No project with that ID found");
		    } else {
		      LOGGER.debug("Retrieved project name: " + project.getName());
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
		LOGGER.debug("Get project with SPARQL:\n" + queryStr);
	    Query query = QueryFactory.create(queryStr);
	    QueryExecution qe = QueryExecutionFactory.create(query, getModel());
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
	  
	  public Set<IProject> filterByName(String filter) throws SimalRepositoryException {
	    String queryStr = "PREFIX xsd: <" + AbstractSimalRepository.XSD_NAMESPACE_URI
	        + "> " + "PREFIX doap: <" + RDFUtils.DOAP_NS + "> "
          + "PREFIX rdf: <" + AbstractSimalRepository.RDF_NAMESPACE_URI + "> "
          + "PREFIX rdfs: <" + AbstractSimalRepository.RDFS_NAMESPACE_URI + "> "
	        + "PREFIX simal: <" + AbstractSimalRepository.SIMAL_NAMESPACE_URI + "> "
	        + "SELECT DISTINCT ?project WHERE " 
	        + "{ ?project a simal:Project ."
	        + "  ?project rdfs:seeAlso ?doapUri ."
	        + "  ?doapUri doap:name ?name ."
	        + "  FILTER regex(?name, \"" + convertFilterToRE(filter) + "\", \"i\") }";
	    Set<IProject> projects = findProjectsBySPARQL(queryStr);

	    return projects;
	  }

    /* (non-Javadoc)
     * @see uk.ac.osswatch.simal.service.IProjectService#createProject(org.w3c.dom.Document)
     */
    public IProject createProject(Document sourceXml)
        throws SimalRepositoryException {
      // Strip any extra XML, such as Atom feed data or web services response
      // data
      if (sourceXml == null) {
        throw new SimalRepositoryException("Supplied RDF/XML document is null.");
      }
      
      IProject newProject = null;
      NodeList projects = sourceXml.getElementsByTagNameNS(RDFUtils.DOAP_NS,
          "Project");

      if (projects.getLength() == 0) {
        throw new SimalRepositoryException("No projects in the supplied RDF/XML document");

        // FIXME when multiple projects are in the document, only the last created Project is returned. 
        //      } else if (projects.getLength() > 1) {
        //        throw new SimalRepositoryException("Too many projects in the supplied RDF/XML document");
      } else {
        for (int i = 0; i < projects.getLength(); i = i + 1) {
          Element project = (Element) projects.item(i);
          newProject = addProject(project);
        }
      }
      return newProject;
    }

    /**
     * Add a single DOAP project document. The root of the project must be a "doap:Project" element
     * otherwise an IllegalArgumentException is thrown. If your document is an RDF document with
     * multiple doap:Projects in it (i.e. the root is rdf:RDF) then use 
     * addProject(Document originalDoc, URL sourceURL, String baseURI) instead.
     * 
     * @param sourceProejctRoot
     *          the doap:Project RDF node
     * @throws DOMException
     * @throws SimalRepositoryException
     * @throws IllegalArgumentException if the element supplied is no a "doap:Project" element
     * @seeAlso addProject(Document originalDoc, URL sourceURL, String baseURI)
     *
     * @refactor should be moved to ProjectService class
     */
    private IProject addProject(Element sourceProjectRoot)
        throws SimalRepositoryException, DOMException {
      if (!sourceProjectRoot.getLocalName().equals("Project")) {
        throw new IllegalArgumentException(
            "Supplied element is not a doap:Project element it is a "
                + sourceProjectRoot.getNodeName());
      }
      
      try {
        cleanProject(sourceProjectRoot);
      } catch (UnsupportedEncodingException e) {
        throw new SimalRepositoryException(
            "Unable to encode URIs for blank nodes", e);
      }
      
      String uri = sourceProjectRoot.getAttributeNS(RDFUtils.RDF_NS, "about");
      IProject project = SimalRepositoryFactory.getProjectService().findProjectBySeeAlso(uri);
      if (project == null) {
        // simal:Project entity for doap:Project does not currently exist
        // look to see if we have data about the same entity but with a different URI
        
        // look for an existing simap:Project identified by a common homepage
        NodeList homepages = sourceProjectRoot.getElementsByTagNameNS(
            RDFUtils.DOAP_NS, "homepage");
        Element homepage;
        for (int i = 0; i < homepages.getLength(); i = i + 1) {
          homepage = (Element) homepages.item(i);
          if (homepage.getParentNode().equals(sourceProjectRoot)) {
              String url = homepage.getAttributeNS(RDFUtils.RDF_NS, "resource")
                  .trim();
              project = findProjectByHomepage(url);
              if (project != null) {
                LOGGER
                    .debug("Simal already has a Project record with the homepage "
                        + url + " with URI " + project.getURI());
              }
          }
        }
    
        // find existing simal:Project identified by a common rdf:seeAlso
        NodeList seeAlsos = sourceProjectRoot.getElementsByTagNameNS(
            RDFUtils.RDFS_NS, "seeAlso");
        Element seeAlso;
        for (int i = 0; i < seeAlsos.getLength(); i = i + 1) {
          seeAlso = (Element) seeAlsos.item(i);
          if (seeAlso.getParentNode().equals(sourceProjectRoot)) {
            String seeAlsoUri = seeAlso.getAttributeNS(RDFUtils.RDF_NS, "resource").trim();
            project = SimalRepositoryFactory.getProjectService().findProjectBySeeAlso(seeAlsoUri);
            if (project != null) {
              LOGGER
                  .debug("Simal already has a Project record with the rdfs:seeAlso "
                      + seeAlsoUri);
            }
          }
        }
    
        if (project == null) {
        if (uri == null || uri.equals("")) {
            // we don't allow blank project nodes
            uri = RDFUtils.getDefaultProjectURI(getNewProjectID());
            sourceProjectRoot.setAttributeNS(RDFUtils.RDF_NS, "about", uri);
        }
        project = createSimalProject(uri);
        } else {
          LOGGER.debug("Updating an existing simal:Project instance with URI "
            + project.getURI());
          try {
        project.addSeeAlso(new URI(uri));
      } catch (URISyntaxException e) {
        throw new SimalRepositoryException("Unable to add a seeAlso attribute to an existing object", e);
      }
        }
      }
      
      // Handle all people
      addDoapPeople(sourceProjectRoot.getElementsByTagNameNS(RDFUtils.DOAP_NS,
          "maintainer"), project.getURI());
      addDoapPeople(sourceProjectRoot.getElementsByTagNameNS(RDFUtils.DOAP_NS,
          "developer"), project.getURI());
      addDoapPeople(sourceProjectRoot.getElementsByTagNameNS(RDFUtils.DOAP_NS,
          "documenter"), project.getURI());
      addDoapPeople(sourceProjectRoot.getElementsByTagNameNS(RDFUtils.DOAP_NS,
          "translator"), project.getURI());
      addDoapPeople(sourceProjectRoot.getElementsByTagNameNS(RDFUtils.DOAP_NS,
          "tester"), project.getURI());
      addDoapPeople(sourceProjectRoot.getElementsByTagNameNS(RDFUtils.DOAP_NS,
          "helper"), project.getURI());

      /**
       * record any new seeAlsos against the project
       */
      NodeList seeAlsos = sourceProjectRoot.getElementsByTagNameNS(
          RDFUtils.RDFS_NS, "seeAlso");
      Element seeAlso;
      for (int i = 0; i < seeAlsos.getLength(); i = i + 1) {
        seeAlso = (Element) seeAlsos.item(i);
        if (seeAlso.getParentNode().equals(sourceProjectRoot)) {
          String seeAlsoUri = seeAlso.getAttributeNS(RDFUtils.RDF_NS, "resource").trim();
          try {
        project.addSeeAlso(new URI(seeAlsoUri));
      } catch (URISyntaxException e) {
        throw new SimalRepositoryException("Unable to add see also to simal:Project", e);
      }
        }
      }
      
      // add the source RDF
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);    DocumentBuilder db;
      try {
        db = dbf.newDocumentBuilder();
      } catch (ParserConfigurationException e) {
        throw new SimalRepositoryException("Unable to create document builder", e);
      }
      Document sourceDoc = db.newDocument();
      Node root = sourceDoc.createElementNS(RDFUtils.RDF_NS, "RDF");
      root.appendChild(sourceDoc.importNode(sourceProjectRoot, true));
      sourceDoc.appendChild(root);
      
      RDFXMLUtils.addRDFXMLToModel(sourceDoc, getModel());
      return project;
    }

    /**
     * Look for and fix common mistakes in RDF/XML DOAP records.
     * 
     * @param projectRoot
     * @throws UnsupportedEncodingException
     * @throws DOMException
     * @throws SimalRepositoryException
     */
    private void cleanProject(Element projectRoot) throws DOMException,
        UnsupportedEncodingException, SimalRepositoryException {
      RDFXMLUtils.validateResourceDefinition(projectRoot.getElementsByTagNameNS(
          RDFUtils.DOAP_NS, "bug-database"));
      RDFXMLUtils.validateResourceDefinition(projectRoot.getElementsByTagNameNS(
          RDFUtils.DOAP_NS, "download-page"));
      RDFXMLUtils.validateResourceDefinition(projectRoot.getElementsByTagNameNS(
          RDFUtils.DOAP_NS, "license"));
      RDFXMLUtils.validateResourceDefinition(projectRoot.getElementsByTagNameNS(
          RDFUtils.DOAP_NS, "mailing-list"));
      RDFXMLUtils.validateResourceDefinition(projectRoot.getElementsByTagNameNS(
          RDFUtils.DOAP_NS, "wiki"));
      RDFXMLUtils.validateResourceDefinition(projectRoot.getElementsByTagNameNS(
          RDFUtils.DOAP_NS, "category"));

      Document doc = projectRoot.getOwnerDocument();
      RDFXMLUtils.removeBNodes(doc);

      RDFXMLUtils.checkCategoryIDs(doc);
      RDFXMLUtils.checkHomePageNodes(doc);
      RDFXMLUtils.checkCDataSections(doc);
      RDFXMLUtils.checkResources(doc);
      RDFXMLUtils.checkPersonIDs(doc);
      RDFXMLUtils.checkProjectIDs(doc);
    }

    /**
     * Create a new simal:Project entity.
     * @param seeAlsoUri the URI for the doap:Project this simal:Project is to represent
     * @throws SimalRepositoryException 
     *
     * @refactor should be moved to ProjectService class
     */ 
    private IProject createSimalProject(String seeAlsoUri) throws SimalRepositoryException {
      String projectID = SimalRepositoryFactory.getProjectService().getNewProjectID();
      String uri = RDFUtils.getDefaultProjectURI(projectID);
      LOGGER.debug("Creating a new Simal Project instance with URI "
        + uri);
      
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      DocumentBuilder db;
      try {
        db = dbf.newDocumentBuilder();
      } catch (ParserConfigurationException e) {
        throw new SimalRepositoryException("Unable to create document builder", e);
      }
      Document simalProjectDoc = db.newDocument();
      Node simalProjectRDFRoot = simalProjectDoc.createElementNS(RDFUtils.RDF_NS,
          "RDF");
      
      Element simalProjectElement = null;
      simalProjectElement = simalProjectDoc.createElementNS(
              RDFUtils.SIMAL_NS, "Project");
      
      simalProjectElement.setAttributeNS(RDFUtils.RDF_NS, "about",
              uri);

      Element simalID = simalProjectDoc.createElementNS(RDFUtils.SIMAL_NS,
            RDFUtils.SIMAL_PROJECT_ID);
        simalID.setTextContent(projectID);
        simalProjectElement.appendChild(simalID);

      
      Element seeAlso = simalProjectDoc.createElementNS(RDFUtils.RDFS_NS, "seeAlso");
      seeAlso.setAttributeNS(RDFUtils.RDF_NS, "resource", seeAlsoUri);
      simalProjectElement.appendChild(seeAlso);

      simalProjectRDFRoot.appendChild(simalProjectElement);
      simalProjectDoc.appendChild(simalProjectRDFRoot);

      RDFXMLUtils.addRDFXMLToModel(simalProjectDoc, getModel());

      return getProject(uri);
    }

    /**
     * @refactor should be moved to ProjectService class
     */ 
    public IProject findProjectByHomepage(String homepage)
        throws SimalRepositoryException {
      String queryStr = "PREFIX simal: <" + RDFUtils.SIMAL_NS + "> "
          + "PREFIX rdf: <" + AbstractSimalRepository.RDF_NAMESPACE_URI + "> "
          + "PREFIX doap: <" + RDFUtils.DOAP_NS + ">" + "PREFIX rdfs: <"
          + AbstractSimalRepository.RDFS_NAMESPACE_URI + ">"
          + "SELECT DISTINCT ?project WHERE { { ?project a simal:Project . "
          + "?project doap:homepage <" + homepage + "> } UNION "
          + "{ ?doaproject a doap:Project . " + "?doapProject doap:homepage <"
          + homepage + "> . ?project rdfs:seeAlso ?doapProject } }";
      // ?project a simal:Project
      // ?project doap:homepage x
      // or ?project rdfs:seeAlso ?linkedProject
      // ?linkedProject doap:homepage x

      IProject project = null;
      Set<IProject> projects = findProjectsBySPARQL(queryStr);

      if (!projects.isEmpty()) {
        project = projects.iterator().next();
      }
      
      return project;
    }

    /**
     * Add the people contained in or referenced from doap:maintianer,
     * doap:developer, doap:documenter, doap:translator, doap:tester, doap:helper
     * elements.
     * 
     * @param doapPeople
   * @param simalProjectURI 
     * @throws SimalRepositoryException
     */
    private void addDoapPeople(NodeList doapPeople, String simalProjectURI)
        throws SimalRepositoryException {
      Element person;
      for (int i = 0; i < doapPeople.getLength(); i = i + 1) {
        Element doapPerson = (Element) doapPeople.item(i);
        String uri = doapPerson.getAttributeNS(RDFUtils.RDF_NS, "resource");
        if (uri == null || uri.equals("")) {
          NodeList people = doapPerson.getElementsByTagNameNS(RDFUtils.FOAF_NS,
              "Person");
          for (int idx = 0; idx < people.getLength(); idx = idx + 1) {
            person = (Element) people.item(idx);
            SimalRepositoryFactory.getPersonService().addPerson(person, simalProjectURI);
          }
        } else {
          doapPerson.removeAttributeNS(RDFUtils.RDF_NS, "resource");
          Element personEl = doapPerson.getOwnerDocument().createElementNS(
              RDFUtils.FOAF_NS, "Person");
          personEl.setAttributeNS(RDFUtils.RDF_NS, "about", uri);
          doapPerson.appendChild(personEl);
          SimalRepositoryFactory.getPersonService().addPerson(personEl, simalProjectURI);
        }
      }
    }

}

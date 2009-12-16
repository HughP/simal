package uk.ac.osswatch.simal.model.jena.simal;

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.Foaf;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.model.ModelSupport;
import uk.ac.osswatch.simal.model.jena.Homepage;
import uk.ac.osswatch.simal.model.jena.Project;
import uk.ac.osswatch.simal.model.jena.Resource;
import uk.ac.osswatch.simal.model.simal.SimalOntology;
import uk.ac.osswatch.simal.rdf.AbstractSimalRepository;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;
import uk.ac.osswatch.simal.service.jena.JenaProjectService;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

public final class JenaSimalRepository extends AbstractSimalRepository {
  public static final Logger logger = LoggerFactory
      .getLogger(JenaSimalRepository.class);

  private static ISimalRepository instance;
  
  private transient Model model;

  /**
   * Use getInstance instead.
   */
  private JenaSimalRepository() {
    super();
    model = null;
  }

  /**
   * Get the SimalRepository object. Note that only one of these can exist in a
   * single virtual machine.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public synchronized static ISimalRepository getInstance() throws SimalRepositoryException {
    if (instance == null) {
      instance = new JenaSimalRepository();
    }
    return instance;
  }

  /**
   * Initialise the repository.
   * 
   * @param directory
   *          the directory for the database if it is a persistent repository
   *          (i.e. not a test repo)
   * @throws SimalRepositoryException
   */
  public void initialise(String directory) throws SimalRepositoryException {
    if (model != null) {
      throw new SimalRepositoryException(
          "Illegal attempt to create a second SimalRepository in the same JAVA VM.");
    }

    if (isTest) {
      model = ModelFactory.createDefaultModel();
    } else {
      String className = "org.apache.derby.jdbc.EmbeddedDriver"; // path of
      // driver
      // class
      try {
        Class.forName(className);
      } catch (ClassNotFoundException e) {
        throw new SimalRepositoryException("Unable to find derby driver", e);
      }
      String DB_URL;
      if (directory != null) {
        DB_URL = "jdbc:derby:"
            + directory
            + "/"
            + SimalProperties
                .getProperty(SimalProperties.PROPERTY_RDF_DATA_FILENAME)
            + ";create=true";
      } else {
        DB_URL = "jdbc:derby:"
            + SimalProperties
                .getProperty(SimalProperties.PROPERTY_RDF_DATA_DIR)
            + "/"
            + SimalProperties
                .getProperty(SimalProperties.PROPERTY_RDF_DATA_FILENAME)
            + ";create=true";
      }
      String DB_USER = "";
      String DB_PASSWD = "";
      String DB = "Derby";

      // Create database connection
      logger.debug("Creating DB with URL " + DB_URL);
      IDBConnection conn = new DBConnection(DB_URL, DB_USER, DB_PASSWD, DB);
      ModelMaker maker = ModelFactory.createModelRDBMaker(conn);

      // create or open the default model
      model = maker.createDefaultModel();

      // Close the database connection
      // conn.close();
    }

    initialised = true;

    if (isTest) {
      try {
		ModelSupport.addTestData(this);
	  } catch (Exception e) {
		throw new SimalRepositoryException("Unable to add test data", e);
	  }
    }
  }

  public void add(String data) throws SimalRepositoryException {
    logger.debug("Adding RDF data string:\n\t" + data);

    File file = new File("tempData.rdf");
    FileWriter fw = null;
    BufferedWriter bw = null;
    try {
      fw = new FileWriter(file);
      bw = new BufferedWriter(fw);
      bw.write(data);
      bw.flush();

      addProject(file.toURI().toURL(), "");
    } catch (MalformedURLException mue) {
      // should never happen as we created the file here
      throw new SimalRepositoryException(
          "Strange... a file we created has a malformed URL", mue);
    } catch (IOException e) {
      throw new SimalRepositoryException(
          "Unable to write file from data string", e);
    } finally {
      if (bw != null) {
        try {
          bw.close();
        } catch (IOException e) {
          logger.warn("Unable to close writer", e);
        }
      }
      if (fw != null) {
        try {
          fw.close();
        } catch (IOException e) {
          logger.warn("Unable to close writer", e);
        }
      }
      boolean deleted = file.delete();
      if (!deleted) {
        logger.warn("Failt to delete temporary file {}", file.toString());
      }
    }

  }

  public void addRDFXML(URL url, String baseURI)
      throws SimalRepositoryException {
    try {
      model.read(url.openStream(), baseURI);
      logger.debug("Added RDF/XML from " + url.toString());
    } catch (IOException e) {
      throw new SimalRepositoryException("Unable to open stream for " + url, e);
    }
  }

  public void addRDFXML(Document doc) throws SimalRepositoryException {
    StringWriter xmlAsWriter = new StringWriter();
    StreamResult result = new StreamResult(xmlAsWriter);
    try {
      TransformerFactory.newInstance().newTransformer().transform(
          new DOMSource(doc), result);
    } catch (TransformerConfigurationException e) {
      throw new SimalRepositoryException("Unable configure XSL Transformer", e);
    } catch (TransformerException e) {
      throw new SimalRepositoryException("Unable to transform document", e);
    } catch (TransformerFactoryConfigurationError e) {
      throw new SimalRepositoryException("Unable to create XSL Transformer", e);
    }
    String xml = xmlAsWriter.toString();
    StringReader xmlReader = new StringReader(xml);
    model.read(xmlReader, "");
  }
  
  public void destroy() throws SimalRepositoryException {
    logger.info("Destorying the SimalRepository");
    model.close();
    model = null;
    initialised = false;
  }
  
  public IDoapHomepage getHomepage(String uri) {
	  if (containsResource(uri)) {
	      return new Homepage(model.getResource(uri));
	  } else {
	      return null;
      }
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
    Set<IProject> projects = ((JenaProjectService) SimalRepositoryFactory
        .getProjectService()).findProjectsBySPARQL(queryStr);

    if (!projects.isEmpty()) {
      project = projects.iterator().next();
    }
    
    return project;
  }

  /**
   * @refactor should be moved to ProjectService class
   */ 
  public Set<IProject> getAllProjects() throws SimalRepositoryException {
    Property o = model.createProperty(SIMAL_PROJECT_URI);
    StmtIterator itr = model.listStatements(null, RDF.type, o);
    Set<IProject> projects = new HashSet<IProject>();
    while (itr.hasNext()) {
      String uri = itr.nextStatement().getSubject().getURI();
      projects.add(new Project(model.getResource(uri)));
    }
    return projects;
  }
  

	/**
	 * Get a featured project. At the present time this will
	 * return a single random project from the repository.
	 * 
	 * @return
	 * @throws SimalRepositoryException 
	 */
	public IProject getFeaturedProject() throws SimalRepositoryException {
	  IProject project;
	  Set<IProject> allProjects = getAllProjects();
      Random rand = new Random();
      int size = allProjects.size();
      if (size > 0) {
        int idx = rand.nextInt(size);
        project = (IProject) allProjects.toArray()[idx];
      } else {
        project = null;
      }
      return project;
	}

  /**
   * @refactor should be moved to ProjectService class
   */ 
  public String getAllProjectsAsJSON() throws SimalRepositoryException {
    StringBuffer json = new StringBuffer("{ \"items\": [");
    Iterator<IProject> projects = getAllProjects().iterator();
    IProject project;
    while (projects.hasNext()) {
      project = projects.next();
      json.append(project.toJSONRecord());
      if (projects.hasNext()) {
        json.append(",");
      }
    }
    json.append("]}");
    return json.toString();
  }
  
  /**
   * Test to see if an organisation with the given String exists.
   * 
   * @param uri
   * @return
   */
  public boolean containsOrganisation(String uri) {
	    Property o = model.createProperty(Foaf.NS + "Organization");
	    com.hp.hpl.jena.rdf.model.Resource r = model.createResource(uri);
	    Statement foaf = model.createStatement(r, RDF.type, o);
	    return model.contains(foaf);
  }

  public boolean containsPerson(String uri) {
	Property o = model.createProperty(Foaf.NS + "Person");
    com.hp.hpl.jena.rdf.model.Resource r = model.createResource(uri);
    Statement foaf = model.createStatement(r, RDF.type, o); 
    Statement simal = model.createStatement(r, RDF.type, SimalOntology.PERSON);
    return model.contains(foaf) || model.contains(simal);
  }

  public boolean containsResource(String uri) {
    com.hp.hpl.jena.rdf.model.Resource r = model.createResource(uri);
    return model.containsResource(r);
  }

  public void removeAllData() {
    model.removeAll();
  }

  /**
   * Get a Jena Resource.
   * 
   * @param uri
   * @return
   */
  public com.hp.hpl.jena.rdf.model.Resource getJenaResource(String uri) {
    return model.getResource(uri);
  }

  public IResource getResource(String uri) {
    return new Resource(model.getResource(uri));
  }

  public void writeBackup(Writer writer) {
    model.write(writer, "N3");
  }

  public void addProject(Document originalDoc, URL sourceURL, String baseURI)
      throws SimalRepositoryException {
    // Strip any extra XML, such as Atom feed data or web services response
    // data
    NodeList projects = originalDoc.getElementsByTagNameNS(RDFUtils.DOAP_NS,
        "Project");

    if (projects.getLength() == 0) {
    	throw new SimalRepositoryException("No projects in the supplied RDF/XML document");
    } else {
      for (int i = 0; i < projects.getLength(); i = i + 1) {
        Element project = (Element) projects.item(i);
        addProject(project);
      }
    }
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
    logger.debug("Creating a new Simal Project instance with URI "
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

    addRDFXML(simalProjectDoc);

    return SimalRepositoryFactory.getProjectService().getProject(uri);
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
  public void addProject(Element sourceProjectRoot)
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
	            logger
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
	          logger
	              .debug("Simal already has a Project record with the rdfs:seeAlso "
	                  + seeAlsoUri);
	        }
	      }
	    }
	
	    if (project == null) {
		  if (uri == null || uri.equals("")) {
		      // we don't allow blank project nodes
		      uri = RDFUtils.getDefaultProjectURI(SimalRepositoryFactory.getProjectService().getNewProjectID());
		      sourceProjectRoot.setAttributeNS(RDFUtils.RDF_NS, "about", uri);
		  }
		  project = createSimalProject(uri);
	    } else {
	      logger.debug("Updating an existing simal:Project instance with URI "
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
    addRDFXML(sourceDoc);
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
          addPerson(person, simalProjectURI);
        }
      } else {
        doapPerson.removeAttributeNS(RDFUtils.RDF_NS, "resource");
        Element personEl = doapPerson.getOwnerDocument().createElementNS(
            RDFUtils.FOAF_NS, "Person");
        personEl.setAttributeNS(RDFUtils.RDF_NS, "about", uri);
        doapPerson.appendChild(personEl);
        addPerson(personEl, simalProjectURI);
      }
    }
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
    RDFUtils.validateResourceDefinition(projectRoot.getElementsByTagNameNS(
        RDFUtils.DOAP_NS, "bug-database"));
    RDFUtils.validateResourceDefinition(projectRoot.getElementsByTagNameNS(
        RDFUtils.DOAP_NS, "download-page"));
    RDFUtils.validateResourceDefinition(projectRoot.getElementsByTagNameNS(
        RDFUtils.DOAP_NS, "license"));
    RDFUtils.validateResourceDefinition(projectRoot.getElementsByTagNameNS(
        RDFUtils.DOAP_NS, "mailing-list"));
    RDFUtils.validateResourceDefinition(projectRoot.getElementsByTagNameNS(
        RDFUtils.DOAP_NS, "wiki"));
    RDFUtils.validateResourceDefinition(projectRoot.getElementsByTagNameNS(
        RDFUtils.DOAP_NS, "category"));

    Document doc = projectRoot.getOwnerDocument();
    RDFUtils.removeBNodes(doc);

    RDFUtils.checkCategoryIDs(doc, this);
    RDFUtils.checkHomePageNodes(doc);
    RDFUtils.checkCDataSections(doc);
    RDFUtils.checkResources(doc);
    RDFUtils.checkPersonIDs(doc, this);
    RDFUtils.checkProjectIDs(doc, this);
  }

  /**
   * Add a person to the repository. We attempt to handle duplicates by matching on items
   * such as email email hashes and seeAlso URIs.
   * 
   * @param simalProjectURI
   *          the URI of the simal record representing the project this person beongs to
   * @throws DOMException
   * @throws SimalRepositoryException
   */
  public void addPerson(Element sourcePersonRoot, String simalProjectURI)
      throws SimalRepositoryException, DOMException {
    if (!sourcePersonRoot.getLocalName().equals("Person")) {
      throw new IllegalArgumentException(
          "Supplied element is not a doap:Person element, it is "
              + sourcePersonRoot.getNodeName());
    }
    String simalPersonURI = null;
    
    cleanPerson(sourcePersonRoot);
    RDFUtils.checkPersonSHA1(sourcePersonRoot.getOwnerDocument());

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    DocumentBuilder db;
    try {
      db = dbf.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      throw new SimalRepositoryException("Unable to create document builder", e);
    }
    Document simalPersonDoc = db.newDocument();
    Node simalPersonRDFRoot = simalPersonDoc.createElementNS(RDFUtils.RDF_NS,
        "RDF");
    Element simalPersonElement = simalPersonDoc.createElementNS(
        RDFUtils.SIMAL_NS, "Person");

    // handle duplicate people identified by their mbox_sha1sum
    NodeList sha1sums = sourcePersonRoot.getElementsByTagNameNS(
        RDFUtils.FOAF_NS, "mbox_sha1sum");
    for (int i = 0; i < sha1sums.getLength(); i = i + 1) {
      Element sha1sumNode = (Element) sha1sums.item(i);
      if (sha1sumNode.getParentNode().equals(sourcePersonRoot)) {
        String sha1Sum = sha1sumNode.getFirstChild().getNodeValue().trim();
        IPerson person = SimalRepositoryFactory.getPersonService().findBySha1Sum(sha1Sum);
        if (person != null) {
          logger
              .debug("Simal already has a Person record with the foaf:mbox_sha1: "
                  + sha1Sum + " called " + person);
          person = SimalRepositoryFactory.getPersonService().findBySeeAlso(person.getURI());
          simalPersonURI = person.getURI();
        }
      }
    }

    // handle duplicate people identified by their rdf:about
    String uri = sourcePersonRoot.getAttributeNS(RDFUtils.RDF_NS, "about");
    IPerson person = SimalRepositoryFactory.getPersonService().findBySeeAlso(uri);
    if (person != null) {
      logger
          .debug("Simal already has a Person record about "
                  + uri + " called " + person);
      simalPersonURI = person.getURI();
    }

    // handle duplicate people identified by their rdfs:seeAlso
    NodeList seeAlsos = sourcePersonRoot.getElementsByTagNameNS(
        RDFUtils.RDFS_NS, "seeAlso");
    Element seeAlso;
    for (int i = 0; i < seeAlsos.getLength(); i = i + 1) {
      seeAlso = (Element) seeAlsos.item(i);
      if (seeAlso.getParentNode().equals(sourcePersonRoot)) {
        uri = seeAlso.getAttributeNS(RDFUtils.RDF_NS, "resource").trim();
        person = SimalRepositoryFactory.getPersonService().findBySeeAlso(uri);
        if (person != null) {
          logger
              .debug("Simal already has a Person record with the rdfs:seeAlso "
                  + uri + " called " + person);
          simalPersonURI = person.getURI();
        } else {
          seeAlso = simalPersonDoc.createElementNS(RDFUtils.RDFS_NS, "seeAlso");
          seeAlso.setAttributeNS(RDFUtils.RDF_NS, "resource", uri);
          simalPersonElement.appendChild(seeAlso);
        }
      }
    }

    // Add current project records
    Element projNode = simalPersonDoc.createElementNS(RDFUtils.FOAF_NS,
      "currentProject");
    projNode.setAttributeNS(RDFUtils.RDF_NS, "resource", simalProjectURI);
    simalPersonElement.appendChild(projNode);

    if (simalPersonURI == null) {
      String personID = getNewPersonID();
      simalPersonURI = RDFUtils.getDefaultPersonURI(personID);
      logger.debug("Creating a new Simal Person instance with URI: "
          + simalPersonURI);

      Element simalID = simalPersonDoc.createElementNS(RDFUtils.SIMAL_NS,
          RDFUtils.SIMAL_PERSON_ID);
      simalID.setTextContent(personID);
      simalPersonElement.appendChild(simalID);
    } else {
      logger.debug("Updating an existing Simal Person instance with URI: "
          + simalPersonURI);
    }
    simalPersonElement.setAttributeNS(RDFUtils.RDF_NS, "about", simalPersonURI);

    seeAlso = simalPersonDoc.createElementNS(RDFUtils.RDFS_NS, "seeAlso");
    String resource = sourcePersonRoot.getAttributeNS(RDFUtils.RDF_NS, "about");
    if (resource == null || resource.equals("")) {
      // we don't allow blank person nodes
      resource = RDFUtils.getDefaultPersonURI(getNewPersonID());
      sourcePersonRoot.setAttributeNS(RDFUtils.RDF_NS, "about", resource);
    }
    seeAlso.setAttributeNS(RDFUtils.RDF_NS, "resource", resource);
    simalPersonElement.appendChild(seeAlso);

    simalPersonRDFRoot.appendChild(simalPersonElement);
    simalPersonDoc.appendChild(simalPersonRDFRoot);

    addRDFXML(simalPersonDoc);

    // Handle all known people
    NodeList people = sourcePersonRoot.getElementsByTagNameNS(RDFUtils.FOAF_NS,
        "Person");
    Element personEl;
    for (int i = 0; i < people.getLength(); i = i + 1) {
      personEl = (Element) people.item(i);
      addPerson(personEl, simalProjectURI);
    }

    // add the source RDF
    Document sourceDoc = db.newDocument();
    Node root = sourceDoc.createElementNS(RDFUtils.RDF_NS, "RDF");
    root.appendChild(sourceDoc.importNode(sourcePersonRoot, true));
    sourceDoc.appendChild(root);
    addRDFXML(sourceDoc);
  }

  /**
   * Check the RDF for a person element for common mistakes and try to fix them.
   * 
   * @param personRoot
   */
  private void cleanPerson(Element personRoot) {
    RDFUtils.validateResourceDefinition(personRoot.getElementsByTagNameNS(
        RDFUtils.FOAF_NS, "homepage"));
  }
	
	/**
	 * Get the RDF model for this repository.
	 * @return
	 */
	public Model getModel() {
		return model;
	}
}

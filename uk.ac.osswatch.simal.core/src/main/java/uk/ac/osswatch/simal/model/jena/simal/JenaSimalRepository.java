package uk.ac.osswatch.simal.model.jena.simal;

/*
 * 
 Copyright 2007 University of Oxford * 
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
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
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
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IOrganisation;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.model.ModelSupport;
import uk.ac.osswatch.simal.model.jena.Category;
import uk.ac.osswatch.simal.model.jena.Homepage;
import uk.ac.osswatch.simal.model.jena.Organisation;
import uk.ac.osswatch.simal.model.jena.Person;
import uk.ac.osswatch.simal.model.jena.Project;
import uk.ac.osswatch.simal.model.jena.Resource;
import uk.ac.osswatch.simal.model.simal.SimalOntology;
import uk.ac.osswatch.simal.rdf.AbstractSimalRepository;
import uk.ac.osswatch.simal.rdf.Doap;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

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
  public static ISimalRepository getInstance() throws SimalRepositoryException {
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
    StringReader xmlReader = new StringReader(xmlAsWriter.toString());
    model.read(xmlReader, "");
  }

  public IPerson createPerson(String uri) throws SimalRepositoryException,
      DuplicateURIException {
    if (containsPerson(uri)) {
      throw new DuplicateURIException(
          "Attempt to create a second person with the URI " + uri);
    }

    String personID = getNewPersonID();
    String simalPersonURI = RDFUtils.getDefaultPersonURI(personID);
    logger.debug("Creating a new Simal Person instance with URI: "
        + simalPersonURI);

    com.hp.hpl.jena.rdf.model.Resource r = model.createResource(simalPersonURI);
    Statement s = model.createStatement(r, RDF.type, SimalOntology.PERSON);
    model.add(s);
    
    if (!uri.startsWith(RDFUtils.PERSON_NAMESPACE_URI)) {
      com.hp.hpl.jena.rdf.model.Resource res = model.createResource(uri);
      s = model.createStatement(r, RDFS.seeAlso, res);
      model.add(s);
    }

    IPerson person = new Person(r);
    person.setSimalID(personID);
    return person;
  }

  public IOrganisation createOrganisation(String uri) throws DuplicateURIException, SimalRepositoryException {
    if (containsProject(uri)) {
        throw new DuplicateURIException(
            "Attempt to create a second project with the URI " + uri);
      }

    com.hp.hpl.jena.rdf.model.Resource foafOrg = model.createResource(uri);
    Statement s = model.createStatement(foafOrg, RDF.type, Foaf.ORGANIZATION);
    model.add(s);
    
    IOrganisation org = new Organisation(foafOrg);
    return org;
  }

  /**
   * @deprecated use ProjectService.createProject(uri) instead
   */
  public IProject createProject(String uri) throws SimalRepositoryException,
      DuplicateURIException {
    if (containsProject(uri)) {
      throw new DuplicateURIException(
          "Attempt to create a second project with the URI " + uri);
    }
    
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

  /**
   * @deprecated use HomepageService.createHomepage(uri) instead
   */
  public IDoapHomepage createHomepage(String uri) throws SimalRepositoryException,
      DuplicateURIException {
	if (uri == null || uri.length() == 0) {
		throw new SimalRepositoryException("URI cannot be blank or null");
	}
    if (containsResource(uri)) {
      throw new DuplicateURIException(
          "Attempt to create a second homepage with the URI " + uri);
    }

    Property o = model.createProperty("http://usefulinc.com/ns/doap#homepage");
    com.hp.hpl.jena.rdf.model.Resource r = model.createResource(uri);
    Statement s = model.createStatement(r, RDF.type, o);
    model.add(s);

    IDoapHomepage page = new Homepage(r);
    page.setSimalID(getNewHomepageID());
    return page;
  }
  
  public IDoapCategory getOrCreateCategory(String uri)
  		throws SimalRepositoryException {
	if (containsResource(uri)) {
		return getCategory(uri);
	} else {
		try {
			return createCategory(uri);
		} catch (DuplicateURIException e) {
			logger.error("Threw a DuplicateURIEception when we had already checked for resource existence", e);
			return null;
		}
	}
  }

  /** 
   * @deprecated Use ProjectServer.getOrCreateProject(uri) instead
   */
  public IProject getOrCreateProject(String uri)
  		throws SimalRepositoryException {
	if (containsResource(uri)) {
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
  
  public IDoapCategory createCategory(String uri) throws SimalRepositoryException,
	DuplicateURIException {
      if (containsResource(uri)) {
        throw new DuplicateURIException(
            "Attempt to create a second project category with the URI " + uri);
      }

      Property o = Doap.CATEGORY;
      com.hp.hpl.jena.rdf.model.Resource r = model.createResource(uri);
      Statement s = model.createStatement(r, RDF.type, o);
      model.add(s);

      IDoapCategory cat = new Category(r);
      cat.setSimalID(getNewCategoryID());
      return cat;
  }

  public void destroy() throws SimalRepositoryException {
    logger.info("Destorying the SimalRepository");
    model.close();
    model = null;
    initialised = false;
  }

  public IDoapCategory getCategory(String uri) throws SimalRepositoryException {
    if (containsResource(uri)) {
      return new Category(model.getResource(uri));
    } else {
      return null;
    }
  }
  
  public IDoapHomepage getHomepage(String uri) {
	  if (containsResource(uri)) {
	      return new Homepage(model.getResource(uri));
	  } else {
	      return null;
      }
  }

  public IDoapCategory findCategoryById(String id)
      throws SimalRepositoryException {
    String queryStr = "PREFIX rdf: <" + AbstractSimalRepository.RDF_NAMESPACE_URI + ">"
        + "PREFIX simal: <" + AbstractSimalRepository.SIMAL_NAMESPACE_URI + ">"
        + "SELECT DISTINCT ?category WHERE { "
        + "?category simal:categoryId \"" + id + "\"}";
    Query query = QueryFactory.create(queryStr);
    QueryExecution qe = QueryExecutionFactory.create(query, model);
    ResultSet results = qe.execSelect();

    IDoapCategory category = null;
    while (results.hasNext()) {
      QuerySolution soln = results.nextSolution();
      RDFNode node = soln.get("category");
      if (node.isResource()) {
        category = new Category((com.hp.hpl.jena.rdf.model.Resource) node);
      }
    }
    qe.close();

    return category;
  }
  
  public Set<IPerson> filterPeopleByName(String filter) {
    String queryStr = "PREFIX xsd: <" + AbstractSimalRepository.XSD_NAMESPACE_URI
    + "> " + "PREFIX foaf: <" + RDFUtils.FOAF_NS + "> "
    + "PREFIX rdf: <" + AbstractSimalRepository.RDF_NAMESPACE_URI + ">"
    + "PREFIX simal: <" + AbstractSimalRepository.SIMAL_NAMESPACE_URI + ">"
    + "SELECT DISTINCT ?person WHERE { ?person a foaf:Person;"
    + "  foaf:name ?name . "
    + "  FILTER regex(?name, \"" + filter + "\", \"i\") }";

    return filterPeopleBySPARQL(queryStr);
  }

  public IPerson findPersonById(String id) throws SimalRepositoryException {
    if (!isValidSimalID(id)) {
      throw new SimalRepositoryException(
          "Attempt to find a person using an invalid Simal ID of "
              + id
              + " are you sure that's a world unique identifier? You may need to call getUniqueSimalID() or RDFUtils.getUniqueSimalID(id)");
    }
    String queryStr = "PREFIX xsd: <" + AbstractSimalRepository.XSD_NAMESPACE_URI
        + "> " + "PREFIX rdf: <" + AbstractSimalRepository.RDF_NAMESPACE_URI + ">"
        + "PREFIX simal: <" + AbstractSimalRepository.SIMAL_NAMESPACE_URI + ">"
        + "SELECT DISTINCT ?person WHERE { " + "?person simal:personId \"" + id
        + "\"^^xsd:string }";
    IPerson person = findPersonBySPARQL(queryStr);

    return person;
  }

  public IPerson findPersonBySeeAlso(String seeAlso)
      throws SimalRepositoryException {
    String queryStr = "PREFIX simal: <" + RDFUtils.SIMAL_NS + "> "
        + "PREFIX rdf: <" + AbstractSimalRepository.RDF_NAMESPACE_URI + "> "
        + "PREFIX rdfs: <" + AbstractSimalRepository.RDFS_NAMESPACE_URI + "> "
        + "SELECT DISTINCT ?person WHERE { "
        + "?person rdf:type simal:Person . " + "?person rdfs:seeAlso <"
        + seeAlso + ">}";

    IPerson person = findPersonBySPARQL(queryStr);

    return person;
  }

  /**
   * Find a single person by executing a SPARQL query.
   * 
   * @param queryStr
   * @return
   */
  private IPerson findPersonBySPARQL(String queryStr) {
    Query query = QueryFactory.create(queryStr);
    QueryExecution qe = QueryExecutionFactory.create(query, model);
    ResultSet results = qe.execSelect();

    IPerson person = null;
    while (results.hasNext()) {
      QuerySolution soln = results.nextSolution();
      RDFNode node = soln.get("person");
      if (node.isResource()) {
        person = new Person((com.hp.hpl.jena.rdf.model.Resource) node);
      }
    }
    qe.close();
    return person;
  }

  public IPerson findPersonBySha1Sum(String sha1sum)
      throws SimalRepositoryException {
    String queryStr = "PREFIX foaf: <" + RDFUtils.FOAF_NS + "> "
        + "PREFIX rdf: <" + AbstractSimalRepository.RDF_NAMESPACE_URI + ">"
        + "SELECT DISTINCT ?person WHERE { " + "?person foaf:mbox_sha1sum \""
        + sha1sum + "\"}";

    IPerson person = findPersonBySPARQL(queryStr);

    return person;
  }

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

    IProject project = findProjectBySPARQL(queryStr);

    return project;
  }

  /**
   * @deprecated use ProjectService.getProjecByID(id) instead
   */
  public IProject findProjectById(String id) throws SimalRepositoryException {
    if (!isValidSimalID(id)) {
      throw new SimalRepositoryException(
          "Attempt to find a project using an invalid Simal ID of "
              + id
              + " are you sure that is a unique ID? You may need to call RDFUtils.getUniqueSimalID(id)");
    }
    String queryStr = "PREFIX xsd: <" + AbstractSimalRepository.XSD_NAMESPACE_URI
        + "> " + "PREFIX rdf: <" + AbstractSimalRepository.RDF_NAMESPACE_URI + ">"
        + "PREFIX simal: <" + AbstractSimalRepository.SIMAL_NAMESPACE_URI + ">"
        + "SELECT DISTINCT ?project WHERE { " + "?project simal:projectId \""
        + id + "\"^^xsd:string }";

    IProject project = findProjectBySPARQL(queryStr);

    return project;
  }

  public Set<IProject> filterProjectsByName(String filter) {
    String queryStr = "PREFIX xsd: <" + AbstractSimalRepository.XSD_NAMESPACE_URI
        + "> " + "PREFIX doap: <" + RDFUtils.DOAP_NS + "> "
        + "PREFIX rdf: <" + AbstractSimalRepository.RDF_NAMESPACE_URI + ">"
        + "PREFIX simal: <" + AbstractSimalRepository.SIMAL_NAMESPACE_URI + ">"
        + "SELECT DISTINCT ?project WHERE { ?project a doap:Project;"
        + "  doap:name ?name . "
        + "  FILTER regex(?name, \"" + filter + "\", \"i\") }";

    return filterProjectsBySPARQL(queryStr);
  }

  /**
   * @deprecated
   */
  public IProject findProjectBySeeAlso(String seeAlso)
      throws SimalRepositoryException {
    return SimalRepositoryFactory.getProjectService().findProjectBySeeAlso(seeAlso);
  }

  /**
   * Find a single project by executing a SPARQL query.
   * If the query returns more than one result then
   * only one is returned.
   * 
   * @param queryStr
   * @return
   * 
   * @deprecated No longer needed when other methods have been moved to ProjectService
   */
  private IProject findProjectBySPARQL(String queryStr) {
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

  /**
   * Find all projects returned using a SPARQL query.
   * 
   * @param queryStr
   * @return
   * @deprecated use ProjectService.findProjectsbySPARQL
   */
  public Set<IProject> filterProjectsBySPARQL(String queryStr) {
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
  
  /**
   * Find all people returned using a SPARQL query.
   * 
   * @param queryStr
   * @return
   */
  private Set<IPerson> filterPeopleBySPARQL(String queryStr) {
    Query query = QueryFactory.create(queryStr);
    QueryExecution qe = QueryExecutionFactory.create(query, model);
    ResultSet results = qe.execSelect();

    Set<IPerson> people = new HashSet<IPerson>();
    while (results.hasNext()) {
      QuerySolution soln = results.nextSolution();
      RDFNode node = soln.get("person");
      if (node.isResource()) {
        people.add(new Person((com.hp.hpl.jena.rdf.model.Resource) node));
      }
    }
    qe.close();
    return people;
  }

  public Set<IDoapCategory> getAllCategories() throws SimalRepositoryException {
    NodeIterator itr = model.listObjectsOfProperty(Doap.CATEGORY);
    Set<IDoapCategory> categories = new HashSet<IDoapCategory>();
    while (itr.hasNext()) {
      String uri = itr.nextNode().toString();
      categories.add(new Category(model.getResource(uri)));
    }
    return categories;
  }

  public Set<IPerson> getAllPeople() throws SimalRepositoryException {
    Property o = model.createProperty(SIMAL_PERSON_URI);
    StmtIterator itr = model.listStatements(null, RDF.type, o);
    Set<IPerson> people = new HashSet<IPerson>();
    while (itr.hasNext()) {
      String uri = itr.nextStatement().getSubject().getURI();
      people.add(new Person(model.getResource(uri)));
    }
    return people;
  }

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

  public Set<IOrganisation> getAllOrganisations() throws SimalRepositoryException {
    StmtIterator itr = model.listStatements(null, RDF.type, Foaf.ORGANIZATION);
    Set<IOrganisation> orgs = new HashSet<IOrganisation>();
    while (itr.hasNext()) {
      String uri = itr.nextStatement().getSubject().getURI();
      orgs.add(new Organisation(model.getResource(uri)));
    }
    return orgs;
  }

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

  public String getAllPeopleAsJSON() throws SimalRepositoryException {
    StringBuffer json = new StringBuffer("{ \"items\": [");
    Set<IPerson> setOfPeople = getAllPeople();
    Iterator<IPerson> people = setOfPeople.iterator();
    IPerson person;
    while (people.hasNext()) {
      person = people.next();
      json.append(person.toJSONRecord());
      if (people.hasNext()) {
        json.append(",");
      }
    }
    json.append("]}");
    return json.toString();
  }

  public IPerson getPerson(String uri) throws SimalRepositoryException {
	if(uri.startsWith(RDFUtils.PERSON_NAMESPACE_URI)) {
	    if (containsPerson(uri)) {
	      return new Person(model.getResource(uri));
	    } else {
	      return null;
	    }
	} else {
		return findPersonBySeeAlso(uri);
	}
  }

  /**
   * @deprecated
   */
  public IProject getProject(String uri) throws SimalRepositoryException {
	return SimalRepositoryFactory.getProjectService().getProject(uri);
  }

  public IOrganisation getOrganisation(String uri)
  		throws SimalRepositoryException {
    if (containsOrganisation(uri)) {
        return new Organisation(model.getResource(uri));
    } else {
        return null;
    }
  }

  /**
   * @throws SimalRepositoryException 
 * @deprecated
   */
  public boolean containsProject(String uri) throws SimalRepositoryException {
    return SimalRepositoryFactory.getProjectService().containsProject(uri);
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
   */
  public void addProject(Element sourceProjectRoot)
      throws SimalRepositoryException, DOMException {
    if (!sourceProjectRoot.getLocalName().equals("Project")) {
      throw new IllegalArgumentException(
          "Supplied element is not a doap:Project element it is a "
              + sourceProjectRoot.getNodeName());
    }
    String simalProjectURI = null;

    try {
      cleanProject(sourceProjectRoot);
    } catch (UnsupportedEncodingException e) {
      throw new SimalRepositoryException(
          "Unable to encode URIs for blank nodes", e);
    }

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
    String uri = sourceProjectRoot.getAttributeNS(RDFUtils.RDF_NS, "about");
    IProject project = findProjectBySeeAlso(uri);
    if (project == null) {
  	    simalProjectElement = simalProjectDoc.createElementNS(
  	        RDFUtils.SIMAL_NS, "Project");
   	    simalProjectElement.appendChild(simalProjectDoc.createElementNS(
  	        RDFUtils.DOAP_NS, "Project"));
     } else {
   	    simalProjectElement = simalProjectDoc.createElementNS(
   	        RDFUtils.DOAP_NS, "Project");
     }
    
    // FIXME: the duplicate handling below is duplicated in RDFUtils.deDupeProject
    
    // handle duplicate projects identified by their homepage
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
            simalProjectURI = project.getURI();
          }
      }
    }

    // handle duplicate projects identified by their rdfs:seeAlso
    NodeList seeAlsos = sourceProjectRoot.getElementsByTagNameNS(
        RDFUtils.RDFS_NS, "seeAlso");
    Element seeAlso;
    for (int i = 0; i < seeAlsos.getLength(); i = i + 1) {
      seeAlso = (Element) seeAlsos.item(i);
      if (seeAlso.getParentNode().equals(sourceProjectRoot)) {
        uri = seeAlso.getAttributeNS(RDFUtils.RDF_NS, "resource").trim();
        project = findProjectBySeeAlso(uri);
        if (project != null) {
          logger
              .debug("Simal already has a Project record with the rdfs:seeAlso "
                  + uri);
          simalProjectURI = project.getURI();
        } else {
          seeAlso = simalProjectDoc
              .createElementNS(RDFUtils.RDFS_NS, "seeAlso");
          seeAlso.setAttributeNS(RDFUtils.RDF_NS, "resource", uri);
          simalProjectElement.appendChild(seeAlso);
        }
      }
    }

    if (simalProjectURI == null) {
      String projectID = getNewProjectID();
      simalProjectURI = RDFUtils.getDefaultProjectURI(projectID);
      logger.debug("Creating a new Simal Project instance with URI "
          + simalProjectURI);

      Element simalID = simalProjectDoc.createElementNS(RDFUtils.SIMAL_NS,
          RDFUtils.SIMAL_PROJECT_ID);
      simalID.setTextContent(projectID);
      simalProjectElement.appendChild(simalID);
    } else {
      logger.debug("Updating an existing Simal Project instance with URI "
          + simalProjectURI);
    }
    simalProjectElement.setAttributeNS(RDFUtils.RDF_NS, "about",
        simalProjectURI);

    seeAlso = simalProjectDoc.createElementNS(RDFUtils.RDFS_NS, "seeAlso");
    String resource = sourceProjectRoot
        .getAttributeNS(RDFUtils.RDF_NS, "about");
    if (resource == null || resource.equals("")) {
      // we don't allow blank project nodes
      resource = RDFUtils.getDefaultProjectURI(getNewProjectID());
      sourceProjectRoot.setAttributeNS(RDFUtils.RDF_NS, "about", resource);
    }
    seeAlso.setAttributeNS(RDFUtils.RDF_NS, "resource", resource);
    simalProjectElement.appendChild(seeAlso);

    simalProjectRDFRoot.appendChild(simalProjectElement);
    simalProjectDoc.appendChild(simalProjectRDFRoot);

    addRDFXML(simalProjectDoc);

    // Handle all people
    addDoapPeople(sourceProjectRoot.getElementsByTagNameNS(RDFUtils.DOAP_NS,
        "maintainer"), simalProjectURI);
    addDoapPeople(sourceProjectRoot.getElementsByTagNameNS(RDFUtils.DOAP_NS,
        "developer"), simalProjectURI);
    addDoapPeople(sourceProjectRoot.getElementsByTagNameNS(RDFUtils.DOAP_NS,
        "documenter"), simalProjectURI);
    addDoapPeople(sourceProjectRoot.getElementsByTagNameNS(RDFUtils.DOAP_NS,
        "translator"), simalProjectURI);
    addDoapPeople(sourceProjectRoot.getElementsByTagNameNS(RDFUtils.DOAP_NS,
        "tester"), simalProjectURI);
    addDoapPeople(sourceProjectRoot.getElementsByTagNameNS(RDFUtils.DOAP_NS,
        "helper"), simalProjectURI);

    // add the source RDF
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
    RDFUtils.deDupeProjects(doc, this);
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
        IPerson person = findPersonBySha1Sum(sha1Sum);
        if (person != null) {
          logger
              .debug("Simal already has a Person record with the foaf:mbox_sha1: "
                  + sha1Sum + " called " + person);
          person = findPersonBySeeAlso(person.getURI());
          simalPersonURI = person.getURI();
        }
      }
    }

    // handle duplicate people identified by their rdf:about
    String uri = sourcePersonRoot.getAttributeNS(RDFUtils.RDF_NS, "about");
    IPerson person = findPersonBySeeAlso(uri);
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
        person = findPersonBySeeAlso(uri);
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

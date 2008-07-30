package uk.ac.osswatch.simal.rdf.jena;
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
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.model.Doap;
import uk.ac.osswatch.simal.model.Foaf;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.ModelSupport;
import uk.ac.osswatch.simal.model.jena.Category;
import uk.ac.osswatch.simal.model.jena.Person;
import uk.ac.osswatch.simal.model.jena.Project;
import uk.ac.osswatch.simal.rdf.AbstractSimalRepository;
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
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

public class SimalRepository extends AbstractSimalRepository {
  private static final Logger logger = LoggerFactory
      .getLogger(SimalRepository.class);

  private Model model;

  /**
   * Use getInstance instead.
   */
  private SimalRepository() {
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
      instance = new SimalRepository();
    }
    return instance;
  }

  /**
   * Initialise the repository.
   * 
   * @param directory the directory for the database if 
   * it is a persistent repository (i.e. not a test repo)
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
        throw new SimalRepositoryException("Unablew to find derby driver", e);
      }
      String DB_URL;
      if (directory != null) {
        DB_URL = "jdbc:derby:" + directory + "/" + SimalProperties.getProperty(SimalProperties.PROPERTY_RDF_DATA_FILENAME) + ";create=true";
      } else {
        DB_URL = "jdbc:derby:" + SimalProperties.getProperty(SimalProperties.PROPERTY_RDF_DATA_DIR) + "/"  + SimalProperties.getProperty(SimalProperties.PROPERTY_RDF_DATA_FILENAME) + ";create=true";
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
      ModelSupport.addTestData(this);
    }
  }

  public void addProject(URL url, String baseURI)
      throws SimalRepositoryException {
    logger.info("Adding a project from " + url.toString());

    verifyInitialised();

    Iterator<File> annotatedFiles = null;
    try {
      logger.info("Preprocessing file");
      annotatedFiles = RDFUtils.preProcess(url, baseURI, this).iterator();
      logger.info("Adding processed RDF/XML");
      while (annotatedFiles.hasNext()) {
        addRDFXML(annotatedFiles.next().toURL(), baseURI);
      }
    } catch (IOException e) {
      throw new SimalRepositoryException(
          "Unable to write the annotated RDF/XML file: " + e.getMessage(), e);
    }
  }

  public void add(String data) throws SimalRepositoryException {
    logger.debug("Adding RDF data string:\n\t" + data);

    File file = new File("testingAddData.rdf");
    try {
      FileWriter fw = new FileWriter(file);
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(data);
      bw.close();

      addProject(file.toURL(), "");
    } catch (MalformedURLException mue) {
      // should never happen as we created the file here
      throw new SimalRepositoryException(
          "Strange... a file we created has a malformed URL", mue);
    } catch (IOException e) {
      throw new SimalRepositoryException(
          "Unable to write file from data string", e);
    } finally {
      file.delete();
    }

  }

  public void addRDFXML(URL url, String baseURI)
      throws SimalRepositoryException {
    try {
      model.read(url.openStream(), baseURI);
    } catch (IOException e) {
      throw new SimalRepositoryException("Unable to open stream for " + url, e);
    }
  }

  public IPerson createPerson(String uri) throws SimalRepositoryException,
      DuplicateURIException {
    if (containsPerson(uri)) {
      throw new DuplicateURIException(
          "Attempt to create a second person with the URI " + uri);
    }

    Resource r = model.createResource(uri.toString());
    Statement s = model.createStatement(r, RDF.type, Foaf.PERSON);
    model.add(s);

    IPerson person = new Person(model.createResource(uri));
    person.setSimalID(getNewPersonID());
    return person;
  }

  public IProject createProject(String uri) throws SimalRepositoryException,
      DuplicateURIException {
    if (containsProject(uri)) {
      throw new DuplicateURIException(
          "Attempt to create a second project with the URI " + uri);
    }

    Property o = model.createProperty("http://usefulinc.com/ns/doap#Project");
    Resource r = model.createResource(uri.toString());
    Statement s = model.createStatement(r, RDF.type, o);
    model.add(s);

    IProject project = new Project(model.createResource(uri));
    project.setSimalID(getNewProjectID());
    return project;
  }

  public void destroy() throws SimalRepositoryException {
    model.close();
    model = null;
    initialised = false;
  }

  public IDoapCategory findCategory(String uri) throws SimalRepositoryException {
    if (containsCategory(uri)) {
      return new Category(model.getResource(uri.toString()));
    } else {
      return null;
    }
  }

  public IPerson findPersonById(String id) throws SimalRepositoryException {
    String queryStr = "PREFIX foaf: <" + SimalRepository.FOAF_NAMESPACE_URI
        + "> " + "PREFIX rdf: <" + SimalRepository.RDF_NAMESPACE_URI + ">"
        + "PREFIX simal: <" + SimalRepository.SIMAL_NAMESPACE_URI + ">"
        + "SELECT DISTINCT ?person WHERE { "
        + "?project rdf:type foaf:Person . " + "?person simal:personId \"" + id
        + "\"}";
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

  public IPerson findPersonBySeeAlso(String seeAlso)
      throws SimalRepositoryException {
    String queryStr = "PREFIX foaf: <" + SimalRepository.FOAF_NAMESPACE_URI
        + "> " + "PREFIX rdf: <" + SimalRepository.RDF_NAMESPACE_URI + "> "
        + "PREFIX rdfs: <" + SimalRepository.RDFS_NAMESPACE_URI + ">"
        + "SELECT DISTINCT ?person WHERE { "
        + "?person rdf:type foaf:Person . " + "?person rdfs:seeAlso <"
        + seeAlso + ">}";

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
    String queryStr = "PREFIX foaf: <" + SimalRepository.FOAF_NAMESPACE_URI
        + "> " + "PREFIX rdf: <" + SimalRepository.RDF_NAMESPACE_URI + ">"
        + "SELECT DISTINCT ?person WHERE { "
        + "?project rdf:type foaf:Person . " + "?person foaf:mbox_sha1sum \""
        + sha1sum + "\"}";

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

  public IProject findProjectByHomepage(String homepage)
      throws SimalRepositoryException {
    String queryStr = "PREFIX doap: <" + SimalRepository.DOAP_NAMESPACE_URI
        + "> " + "PREFIX rdf: <" + SimalRepository.RDF_NAMESPACE_URI + "> "
        + "PREFIX rdfs: <" + SimalRepository.RDFS_NAMESPACE_URI + ">"
        + "SELECT DISTINCT ?project WHERE { " + "?project a doap:Project . "
        + "?project doap:homepage <" + homepage + ">}";

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

  public IProject findProjectById(String id) throws SimalRepositoryException {
    String queryStr = "PREFIX doap: <" + SimalRepository.DOAP_NAMESPACE_URI
        + "> " + "PREFIX rdf: <" + SimalRepository.RDF_NAMESPACE_URI + ">"
        + "PREFIX simal: <" + SimalRepository.SIMAL_NAMESPACE_URI + ">"
        + "SELECT DISTINCT ?project WHERE { "
        + "?project rdf:type doap:Project . " + "?project simal:projectId \""
        + id + "\"}";

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

  public IProject findProjectBySeeAlso(String seeAlso)
      throws SimalRepositoryException {
    String queryStr = "PREFIX doap: <" + SimalRepository.DOAP_NAMESPACE_URI
        + "> " + "PREFIX rdf: <" + SimalRepository.RDF_NAMESPACE_URI + "> "
        + "PREFIX rdfs: <" + SimalRepository.RDFS_NAMESPACE_URI + ">"
        + "SELECT DISTINCT ?project WHERE { " + "?project a doap:Project . "
        + "?project rdfs:seeAlso <" + seeAlso + ">}";

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
    StmtIterator itr = model.listStatements(null, RDF.type, Foaf.PERSON);
    Set<IPerson> people = new HashSet<IPerson>();
    while (itr.hasNext()) {
      String uri = itr.nextStatement().getSubject().getURI();
      people.add(new Person(model.getResource(uri)));
    }
    return people;
  }

  public Set<IProject> getAllProjects() throws SimalRepositoryException {
    Property o = model.createProperty(DOAP_PROJECT_URI);
    StmtIterator itr = model.listStatements(null, RDF.type, o);
    Set<IProject> projects = new HashSet<IProject>();
    while (itr.hasNext()) {
      String uri = itr.nextStatement().getSubject().getURI();
      projects.add(new Project(model.getResource(uri)));
    }
    return projects;
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

  public IPerson getPerson(String uri) throws SimalRepositoryException {
    if (containsPerson(uri)) {
      return new Person(model.getResource(uri.toString()));
    } else {
      return null;
    }
  }

  public IProject getProject(String uri) throws SimalRepositoryException {
    if (containsProject(uri)) {
      return new Project(model.getResource(uri.toString()));
    } else {
      return null;
    }
  }

  public void writeXML(Writer writer, String uri)
      throws SimalRepositoryException {
    Resource resource = model.getResource(uri);
    resource.getModel().write(writer);
  }

  public boolean containsProject(String uri) {
    Property o = model.createProperty("http://usefulinc.com/ns/doap#Project");
    Resource r = model.createResource(uri.toString());
    Statement s = model.createStatement(r, RDF.type, o);
    return model.contains(s);
  }

  public boolean containsPerson(String uri) {
    Resource r = model.createResource(uri.toString());
    Statement s = model.createStatement(r, RDF.type, Foaf.PERSON);
    return model.contains(s);
  }

  public boolean containsCategory(String uri) {
    Resource r = model.createResource(uri.toString());
    return model.containsResource(r);
  }

  public void initialise() throws SimalRepositoryException {
    initialise(SimalProperties.getProperty(SimalProperties.PROPERTY_RDF_DATA_DIR));
  }

  public void removeAllData() {
    model.removeAll();
  }
  
}
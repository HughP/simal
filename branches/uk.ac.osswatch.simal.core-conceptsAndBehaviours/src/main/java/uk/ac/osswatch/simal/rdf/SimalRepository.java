package uk.ac.osswatch.simal.rdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.EntityTransaction;
import javax.xml.namespace.QName;

import org.openrdf.concepts.doap.Project;
import org.openrdf.elmo.ElmoModule;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.elmo.sesame.SesameManagerFactory;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.rdfxml.RDFXMLParser;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;
import org.openrdf.sail.memory.MemoryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.elmo.DoapProjectBehaviour;
import uk.ac.osswatch.simal.model.elmo.DoapResourceBehaviour;
import uk.ac.osswatch.simal.model.elmo.FoafPersonBehaviour;
import uk.ac.osswatch.simal.model.elmo.FoafResourceBehaviour;
import uk.ac.osswatch.simal.model.elmo.ResourceBehavior;
import uk.ac.osswatch.simal.rdf.io.AnnotatingRDFXMLHandler;

/**
 * A class for handling common repository actions. Applications should not
 * instantiate this class but should interact with it via its methods.
 * 
 */
public class SimalRepository extends SimalProperties {
  private static final Logger logger = LoggerFactory
      .getLogger(SimalRepository.class);

  // FIXME: standardise names of constants
  public static final String TEST_FILE_BASE_URL = "http://exmple.org/baseURI";
  public static final String TEST_FILE_URI_NO_QNAME = "testNoRDFAboutDOAP.xml";
  public static final String DEFAULT_PROJECT_NAMESPACE_URI = "http://simal/oss-watch.ac.uk/defaultProjectNS#";
  public static final String DEFAULT_PERSON_NAMESPACE_URI = "http://simal/oss-watch.ac.uk/defaultPersonNS#";

  public static final String FOAF_NAMESPACE_URI = "http://xmlns.com/foaf/0.1/";
  public static final String FOAF_KNOWS_PREDICATE = FOAF_NAMESPACE_URI
      + "knows";

  public static final String DOAP_NAMESPACE_URI = "http://usefulinc.com/ns/doap#";
  public static final String DOAP_PROJECT_URI = DOAP_NAMESPACE_URI + "Project";

  public static final String SIMAL_NAMESPACE_URI = "http://simal.oss-watch.ac.uk/ns/0.2/simal#";
  public static final String SIMAL_ID = SIMAL_NAMESPACE_URI + "project-id";

  public static final String RDF_NAMESPACE_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

  private final String CATEGORIES_RDF = "categories.xml";

  private SailRepository _repository;
  private boolean isTest = false;

  public SimalRepository() throws SimalRepositoryException {
    super();
  }

  private RepositoryConnection getConnection() throws SimalRepositoryException {
    return getManager().getConnection();
  }

  /**
   * Add a an RDF file from a given URL.
   * 
   * @param url
   * @throws SimalRepositoryException
   */
  public void addProject(URL url, String baseURI)
      throws SimalRepositoryException {
    logger.info("Adding a project from " + url.toString());
    AnnotatingRDFXMLHandler annotatingHandler;

    verifyInitialised();

    RDFParser parser = new RDFXMLParser();
    // FIXME: use a proper file location
    File annotatedFile;
    try {
      annotatedFile = new File("annotatedRDFXML.xml");
      logger.debug("Annotated file written to "
          + annotatedFile.getAbsolutePath());
      annotatingHandler = new AnnotatingRDFXMLHandler(annotatedFile, this);
      parser.setRDFHandler(annotatingHandler);
    } catch (IOException e) {
      throw new SimalRepositoryException(
          "Unable to write the annotated RDF/XML file: " + e.getMessage(), e);
    }
    parser.setVerifyData(true);

    try {
      parser.parse(url.openStream(), baseURI);
      addRDFXML(annotatedFile.toURL(), baseURI);
    } catch (RDFParseException e) {
      throw new SimalRepositoryException(
          "Attempt to add unparseable RDF/XML to the repository: "
              + e.getMessage(), e);
    } catch (IOException e) {
      throw new SimalRepositoryException("Unable to read the RDF/XML file: "
          + e.getMessage(), e);
    } catch (RDFHandlerException e) {
      throw new SimalRepositoryException("Problem handling RDF data: "
          + e.getMessage(), e);
    } finally {
      if (!logger.isDebugEnabled()) {
        annotatedFile.delete();
      }
    }
  }

  /**
   * Add a new statement to the repository.
   * 
   * @param predicate
   * @param subject
   * @param object
   * @throws SimalRepositoryException
   * @throws RepositoryException
   */
  public void add(URIImpl predicate, URIImpl subject, URIImpl object)
      throws RepositoryException, SimalRepositoryException {
    getConnection().add(subject, predicate, object);
  }

  /**
   * Checks to see if the repository has been correctly initialised. If it has
   * not then an exception is thrown.
   * 
   * @throws SimalRepositoryException
   */
  private void verifyInitialised() throws SimalRepositoryException {
    if (!isInitialised()) {
      throw new SimalRepositoryException(
          "SimalRepsotory has not been initialised. Call one of the initialise methods first.");
    }
  }

  /**
   * Get a manager for concepts defined by Elmo.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public SesameManager getManager() throws SimalRepositoryException {
    verifyInitialised();
    ElmoModule module = new ElmoModule();

    // Concepts
    module.recordRole(org.openrdf.concepts.doap.Project.class);
    module.recordRole(org.openrdf.concepts.doap.Version.class);
    module.recordRole(org.openrdf.concepts.doap.Repository.class);
    module.recordRole(org.openrdf.concepts.doap.DoapResource.class);
    module.recordRole(org.openrdf.concepts.rdfs.Resource.class);
    module.recordRole(org.openrdf.concepts.foaf.Person.class);
    module.recordRole(IProject.class);

    // Behaviours
    module.recordRole(DoapResourceBehaviour.class);
    module.recordRole(DoapProjectBehaviour.class);
    module.recordRole(FoafPersonBehaviour.class);
    module.recordRole(FoafResourceBehaviour.class);
    module.recordRole(ResourceBehavior.class);

    SesameManagerFactory factory = new SesameManagerFactory(module, _repository);
    return factory.createElmoManager();
  }

  /**
   * Get a project from the repository.
   * 
   * @param qname
   *          the QName of the project to retrieve
   * @return the project, or if no project with the given QName exists Null
   * @throws SimalRepositoryException
   */
  public Project getProject(QName qname)
      throws SimalRepositoryException {
    verifyInitialised();
    return getManager().find(Project.class, qname);
  }

  /**
   * Get a person from the repository.
   * 
   * @param qname
   *          the QName of the repository to retrieve
   * @return the repository or if no repository with the given QName exists Null
   * @throws SimalRepositoryException
   */
  public FoafPersonBehaviour getPerson(QName qname)
      throws SimalRepositoryException {
    verifyInitialised();
    return getManager().find(FoafPersonBehaviour.class, qname);
  }

  public Set<DoapProjectBehaviour> getAllProjects()
      throws SimalRepositoryException {
    verifyInitialised();
    return getManager().findAll(DoapProjectBehaviour.class);
  }

  /**
   * Write an RDF/XML document representing a project. If the project does not
   * exist an empty RDF/XML document is written.
   * 
   * @param writer
   * @param qname
   * @throws SimalRepositoryException
   * @throws RepositoryException
   * @throws RDFHandlerException
   */
  public void writeXML(Writer writer, QName qname)
      throws SimalRepositoryException {
    verifyInitialised();

    org.openrdf.model.Resource resource = new URIImpl(qname.toString());
    RDFXMLPrettyWriter XMLWriter = new RDFXMLPrettyWriter(writer);
    try {
      _repository.getConnection().exportStatements(resource, (URI) null,
          (Value) null, true, XMLWriter);
    } catch (RepositoryException e) {
      throw new SimalRepositoryException("Unable to connect to the repository",
          e);
    } catch (RDFHandlerException e) {
      throw new SimalRepositoryException(
          "Unable to handle the generated RDF/XML", e);
    }
  }

  /**
   * Adds test data to the repo. be careful to only use this when the repo in
   * use is a test repository.
   * 
   * @throws SimalRepositoryException
   * 
   * @throws SimalRepositoryException
   */
  private void addTestData() throws SimalRepositoryException {
    verifyInitialised();

    try {
      addProject(SimalRepository.class.getResource(TEST_FILE_URI_NO_QNAME),
          TEST_FILE_BASE_URL);

      addProject(SimalRepository.class.getResource("testDOAP.xml"),
          TEST_FILE_BASE_URL);

      addProject(SimalRepository.class.getResource("ossWatchDOAP.xml"),
          TEST_FILE_BASE_URL);

      addRDFXML(SimalRepository.class.getClassLoader().getResource(
          CATEGORIES_RDF), TEST_FILE_BASE_URL);

      addProject(new URL(
          "http://simal.oss-watch.ac.uk/projectDetails/codegoo.rdf"),
          "http://simal.oss-watch.ac.uk");
    } catch (Exception e) {
      throw new RuntimeException(
          "Can't add the test data, there's no point in carrying on", e);
    }
  }

  /**
   * Add an RDF/XML file, other than one supported by more specialised methods,
   * such as addProject(...).
   * 
   * @param categoriesRdf
   * @param testFileBaseUrl
   * @throws SimalRepositoryException
   */
  public void addRDFXML(URL url, String baseURL)
      throws SimalRepositoryException {
    RepositoryConnection con = getConnection();
    try {
      con.add(url, baseURL, RDFFormat.RDFXML);
    } catch (RDFParseException e) {
      throw new SimalRepositoryException(
          "Attempt to add unparseable RDF/XML to the repository: "
              + e.getMessage(), e);
    } catch (RepositoryException e) {
      throw new SimalRepositoryException("Unable to access the repository: "
          + e.getMessage(), e);
    } catch (IOException e) {
      throw new SimalRepositoryException("Unable to read the RDF/XML file: "
          + e.getMessage(), e);
    } finally {
      try {
        con.close();
      } catch (RepositoryException e) {
        throw new SimalRepositoryException("Unable to close the connection: "
            + e.getMessage(), e);
      }
    }
  }

  /**
   * Get the default QName for a Project. The default QName should be used if
   * the original resource does not provide a QName.
   * 
   * @param project
   *          the project for which we need a QName
   * @return
   */
  public QName getDefaultQName(org.openrdf.concepts.doap.Project project) {
    String strQName;
    if (project.getDoapHomepages() == null
        || project.getDoapHomepages().size() == 0) {
      strQName = "http://simal.oss-watch.ac.uk/project/unkownSource/"
          + (String) project.getDoapNames().toArray()[0];
    } else {
      strQName = project.getDoapHomepages().toArray()[0].toString();
      if (!strQName.endsWith("/")) {
        strQName = strQName + "/";
      }
      strQName = strQName + (String) project.getDoapNames().toArray()[0];
    }
    return new QName(strQName);
  }

  /**
   * Get all the projects in the repository and return them in a single JSON
   * file.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public String getAllProjectsAsJSON() throws SimalRepositoryException {
    StringBuffer json = new StringBuffer("{ \"items\": [");
    Iterator<DoapProjectBehaviour> projects = getAllProjects().iterator();
    while (projects.hasNext()) {
      json.append(projects.next().toJSONRecord());
      if (projects.hasNext()) {
        json.append(",");
      }
    }
    json.append("]}");
    return json.toString();
  }

  /**
   * Return true if this repository has been successfully initialised and is
   * ready to be used, otherwise return false.
   * 
   * @return
   */
  public boolean isInitialised() {
    return _repository != null;
  }

  /**
   * Shutdown the repository cleanly.
   * 
   * @throws SimalRepositoryException
   * 
   * @throws RepositoryException
   */
  public void destroy() throws SimalRepositoryException {
    if (isInitialised()) {
      try {
        _repository.shutDown();
        _repository = null;
      } catch (RepositoryException e) {
        throw new SimalRepositoryException(
            "Unable to shutdown the repository, refusing to destroy it.");
      }
    }
  }

  /**
   * Initialise a default repository. Currently this creates a volatile
   * repository populated with test data.
   * 
   * @throws SimalRepositoryException
   * 
   * @throws SimalRepositoryException
   */
  public void initialise() throws SimalRepositoryException {
    if (_repository != null) {
      throw new SimalRepositoryException(
          "Illegal attempt to create a second SimalRepository in the same JAVA VM.");
    }

    if (isTest) {
      _repository = new SailRepository(new MemoryStore());
    } else {
      _repository = new SailRepository(
          new MemoryStore(getPersistentStoreFile()));
    }

    try {
      _repository.initialize();
    } catch (RepositoryException e) {
      throw new SimalRepositoryException("Unable to intialise the repository",
          e);
    }

    if (isTest) {
      addTestData();
    }
  }

  /**
   * Get the directory in which the Simal repository data should be saved.
   * 
   * @return
   */
  private File getPersistentStoreFile() {
    return new File(getProperty(PROPERTY_DATA_DIR));
  }

  /**
   * Set whether or not this repository is to be set up in test mode. this
   * method should be called before initialise if the default behaviour is to be
   * altered/
   * 
   * @param newValue
   *          true if this is to be a test repository
   * @throws SimalRepositoryException
   */
  public void setIsTest(boolean newValue) throws SimalRepositoryException {
    if (isInitialised() && isTest != newValue) {
      throw new SimalRepositoryException(
          "Unable to change the value of SimalRepository,isTest after initialisation.");
    }
    isTest = newValue;
  }

  /**
   * Start a transaction.
   * 
   * @throws SimalRepositoryException
   *           If there is a problem communicating with the repository
   * @throws TransactionException
   *           If a transaction is already in progress. Applications should
   *           decide whether or not to treat this as an error or to continue
   *           within the existing transaction.
   */
  public void startTransaction() throws SimalRepositoryException,
      TransactionException {
    EntityTransaction transaction = getManager().getTransaction();
    if (transaction.isActive()) {
      throw new TransactionException(
          "Attempt to start a new transaction when one is already active.");
    }
    transaction.begin();
  }

  /**
   * End a transaction.
   * 
   * @throws SimalRepositoryException
   * @throws TransactionException
   *           If no transaction is active.
   */
  public void commitTransaction() throws SimalRepositoryException,
      TransactionException {
    EntityTransaction transaction = getManager().getTransaction();
    if (!transaction.isActive()) {
      throw new TransactionException(
          "Attempt to commit a transaction when there is not one active.");
    }
    transaction.commit();
  }

  /**
   * Rollback a transaction.
   * 
   * @throws SimalRepositoryException
   * @throws TransactionException
   *           If no transaction is active
   */
  public void rollback() throws SimalRepositoryException, TransactionException {
    EntityTransaction transaction = getManager().getTransaction();
    if (!transaction.isActive()) {
      throw new TransactionException(
          "Attempt to roll back a transaction when there is not one active.");
    }
    transaction.rollback();
  }

  /**
   * Create a new project in the repository.
   * 
   * @return
   * @throws SimalRepositoryException
   *           if an error is thrown whilst communicating with the repository
   * @throws DuplicateQNameException
   *           if an entity with the given QName already exists
   */
  public Project createProject(QName qname)
      throws SimalRepositoryException, DuplicateQNameException {
    Project project = getProject(qname);
    if (project != null) {
      throw new DuplicateQNameException(
          "Attempt to create a second project with the QName " + qname);
    }
    return project;
  }

  /**
   * Remove an entity from the repository.
   * 
   * @param qname
   * @throws SimalRepositoryException
   */
  public void remove(QName qname) throws SimalRepositoryException {
    getManager().remove(getManager().find(qname));
  }

  /**
   * Create a new project ID and save the next value in the properties file.
   * 
   * @throws IOException
   * @throws FileNotFoundException
   */
  public String getNewProjectID() throws SimalRepositoryException {
    String strID = getProperty(PROPERTY_SIMAL_NEXT_PROJECT_ID, "1");
    long id = Long.parseLong(strID);
    long newId = id + 1;
    setProperty(PROPERTY_SIMAL_NEXT_PROJECT_ID, Long.toString(newId));
    try {
      save();
    } catch (Exception e) {
      logger.warn("Unable to save properties file", e);
      throw new SimalRepositoryException(
          "Unable to save properties file when creating the next project ID", e);
    }
    return strID;
  }
}

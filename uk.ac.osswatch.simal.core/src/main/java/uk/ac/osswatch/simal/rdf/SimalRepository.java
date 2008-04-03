package uk.ac.osswatch.simal.rdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.xml.namespace.QName;

import org.openrdf.elmo.ElmoManager;
import org.openrdf.elmo.ElmoModule;
import org.openrdf.elmo.ElmoQuery;
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

import uk.ac.osswatch.simal.SimalProperties;

import uk.ac.osswatch.simal.model.IDoapBugDatabase;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IDoapDownloadMirror;
import uk.ac.osswatch.simal.model.IDoapDownloadPage;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IDoapMailingList;
import uk.ac.osswatch.simal.model.IDoapRelease;
import uk.ac.osswatch.simal.model.IDoapRepository;
import uk.ac.osswatch.simal.model.IDoapScreenshot;
import uk.ac.osswatch.simal.model.IDoapWiki;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.elmo.DoapBugDatabaseBehaviour;
import uk.ac.osswatch.simal.model.elmo.DoapCategoryBehaviour;
import uk.ac.osswatch.simal.model.elmo.DoapDownloadMirrorBehaviour;
import uk.ac.osswatch.simal.model.elmo.DoapDownloadPageBehaviour;
import uk.ac.osswatch.simal.model.elmo.DoapHomepageBehaviour;
import uk.ac.osswatch.simal.model.elmo.DoapMailingListBehaviour;
import uk.ac.osswatch.simal.model.elmo.DoapProjectBehaviour;
import uk.ac.osswatch.simal.model.elmo.DoapReleaseBehaviour;
import uk.ac.osswatch.simal.model.elmo.DoapRepositoryBehaviour;
import uk.ac.osswatch.simal.model.elmo.DoapScreenshotBehaviour;
import uk.ac.osswatch.simal.model.elmo.DoapWikiBehaviour;
import uk.ac.osswatch.simal.model.elmo.FoafPersonBehaviour;
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
  public static final String DEFAULT_PROJECT_NAMESPACE_URI = "http://simal.oss-watch.ac.uk/defaultProjectNS#";
  public static final String DEFAULT_PERSON_NAMESPACE_URI = "http://simal.oss-watch.ac.uk/defaultPersonNS#";

  public static final String FOAF_NAMESPACE_URI = "http://xmlns.com/foaf/0.1/";
  public static final String FOAF_PERSON_URI = FOAF_NAMESPACE_URI + "Person";
  public static final String FOAF_KNOWS_URI = FOAF_NAMESPACE_URI + "knows";

  public static final String DOAP_NAMESPACE_URI = "http://usefulinc.com/ns/doap#";
  public static final String DOAP_PROJECT_URI = DOAP_NAMESPACE_URI + "Project";
  public static final String DOAP_MAINTAINER_URI = DOAP_NAMESPACE_URI
      + "maintainer";
  public static final String DOAP_DEVELOPER_URI = DOAP_NAMESPACE_URI
      + "developer";
  public static final String DOAP_DOCUMENTER_URI = DOAP_NAMESPACE_URI
      + "documenter";
  public static final String DOAP_HELPER_URI = DOAP_NAMESPACE_URI + "helper";
  public static final String DOAP_TESTER_URI = DOAP_NAMESPACE_URI + "tester";
  public static final String DOAP_TRANSLATOR_URI = DOAP_NAMESPACE_URI
      + "translator";

  public static final String SIMAL_NAMESPACE_URI = "http://simal.oss-watch.ac.uk/ns/0.2/simal#";
  public static final String SIMAL_URI_PROJECT_ID = SIMAL_NAMESPACE_URI
      + "projectId";
  public static final String SIMAL_URI_PERSON_ID = SIMAL_NAMESPACE_URI
      + "personId";

  public static final String RDF_NAMESPACE_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

  public final static String CATEGORIES_RDF = "categories.xml";

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
          "Attempt to add unparseable RDF/XML to the repository loaded from "
              + url.toString(), e);
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
    module.recordRole(IDoapBugDatabase.class);
    module.recordRole(IDoapCategory.class);
    module.recordRole(IDoapDownloadMirror.class);
    module.recordRole(IDoapDownloadPage.class);
    module.recordRole(IDoapHomepage.class);
    module.recordRole(IDoapRelease.class);
    module.recordRole(IDoapRepository.class);
    module.recordRole(IDoapScreenshot.class);
    module.recordRole(IDoapMailingList.class);
    module.recordRole(IDoapWiki.class);
    module.recordRole(IPerson.class);
    module.recordRole(IProject.class);

    // Behaviours
    module.recordRole(DoapBugDatabaseBehaviour.class);
    module.recordRole(DoapCategoryBehaviour.class);
    module.recordRole(DoapDownloadMirrorBehaviour.class);
    module.recordRole(DoapDownloadPageBehaviour.class);
    module.recordRole(DoapHomepageBehaviour.class);
    module.recordRole(DoapMailingListBehaviour.class);
    module.recordRole(DoapProjectBehaviour.class);
    module.recordRole(DoapReleaseBehaviour.class);
    module.recordRole(DoapRepositoryBehaviour.class);
    module.recordRole(DoapScreenshotBehaviour.class);
    module.recordRole(DoapWikiBehaviour.class);
    module.recordRole(FoafPersonBehaviour.class);

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
  public IProject getProject(QName qname) throws SimalRepositoryException {
    verifyInitialised();
    return getManager().find(IProject.class, qname);
  }

  /**
   * Get a project from the repository.
   * 
   * @param qname
   *          the QName of the project to retrieve
   * @return the project, or if no project with the given QName exists Null
   * @throws SimalRepositoryException
   */
  public IDoapCategory findCategory(QName qname)
      throws SimalRepositoryException {
    verifyInitialised();
    return getManager().designate(IDoapCategory.class, qname);
  }

  /**
   * Get a person from the repository.
   * 
   * @param qname
   *          the QName of the repository to retrieve
   * @return the repository or if no repository with the given QName exists Null
   * @throws SimalRepositoryException
   */
  public IPerson getPerson(QName qname) throws SimalRepositoryException {
    verifyInitialised();
    return getManager().find(IPerson.class, qname);
  }

  /**
   * Get a person with a given simal id.
   * 
   * @param id
   * @return
   * @throws SimalRepositoryException
   */
  public IPerson findPersonById(String id) throws SimalRepositoryException {
    String queryStr = "PREFIX foaf: <" + SimalRepository.FOAF_NAMESPACE_URI
        + "> " + "PREFIX rdf: <" + SimalRepository.RDF_NAMESPACE_URI + ">"
        + "PREFIX simal: <" + SimalRepository.SIMAL_NAMESPACE_URI + ">"
        + "SELECT DISTINCT ?person WHERE { "
        + "?project rdf:type foaf:Person . "
        + "?person simal:personId $simalId  " + "}";
    ElmoManager elmoManager = getManager();
    ElmoQuery query = elmoManager.createQuery(queryStr);
    query.setParameter("simalId", id);

    try {
      Object result = query.getSingleResult();
      IPerson person = elmoManager.designateEntity(IPerson.class, result);
      return person;
    } catch (NoResultException e) {
      return null;
    }
  }

  /**
   * Get a project with a given simal id.
   * 
   * @param id
   * @return
   * @throws SimalRepositoryException
   */
  public IProject findProjectById(String id) throws SimalRepositoryException {
    String queryStr = "PREFIX doap: <" + SimalRepository.DOAP_NAMESPACE_URI
        + "> " + "PREFIX rdf: <" + SimalRepository.RDF_NAMESPACE_URI + ">"
        + "PREFIX simal: <" + SimalRepository.SIMAL_NAMESPACE_URI + ">"
        + "SELECT DISTINCT ?project WHERE { "
        + "?project rdf:type doap:Project . "
        + "?project simal:projectId $simalId  " + "}";
    ElmoManager elmoManager = getManager();
    ElmoQuery query = elmoManager.createQuery(queryStr);
    query.setParameter("simalId", id);

    try {
      Object result = query.getSingleResult();
      IProject project = elmoManager.designateEntity(IProject.class, result);
      return project;
    } catch (NoResultException e) {
      return null;
    }
  }

  public Set<IProject> getAllProjects() throws SimalRepositoryException {
    verifyInitialised();
    return getManager().findAll(IProject.class);
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
          "Unable to change the value of SimalRepository.isTest after initialisation.");
    }
    isTest = newValue;
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
  public IProject createProject(QName qname) throws SimalRepositoryException,
      DuplicateQNameException {
    IProject project = getProject(qname);
    if (project != null) {
      throw new DuplicateQNameException(
          "Attempt to create a second project with the QName " + qname);
    }

    project = getManager().designate(IProject.class, qname);
    project.setSimalID(getNewProjectID());
    return project;
  }

  /**
   * Create a new person in the repository.
   * 
   * @return
   * @throws SimalRepositoryException
   *           if an error is thrown whilst communicating with the repository
   * @throws DuplicateQNameException
   *           if an entity with the given QName already exists
   */
  public IPerson createPerson(QName qname) throws SimalRepositoryException,
      DuplicateQNameException {
    IPerson person = getPerson(qname);
    if (person != null) {
      throw new DuplicateQNameException(
          "Attempt to create a second person with the QName " + qname);
    }

    person = getManager().designate(IPerson.class, qname);
    person.setSimalId(getNewPersonID());
    return person;
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

    /**
     * If the properties file is lost for any reason the next ID value will be
     * lost. We therefore need to perform a sanity check that this is unique.
     */
    boolean validID = false;
    while (!validID) {
      if (findProjectById(Long.toString(id)) == null) {
        validID = true;
      } else {
        id = id + 1;
      }
    }

    long newId = id + 1;
    setProperty(PROPERTY_SIMAL_NEXT_PROJECT_ID, Long.toString(newId));
    try {
      save();
    } catch (Exception e) {
      logger.warn("Unable to save properties file", e);
      throw new SimalRepositoryException(
          "Unable to save properties file when creating the next project ID", e);
    }
    return Long.toString(id);
  }

  /**
   * Create a new person ID and save the next value in the properties file.
   * 
   * @throws IOException
   * @throws FileNotFoundException
   */
  public String getNewPersonID() throws SimalRepositoryException {
    String strID = getProperty(PROPERTY_SIMAL_NEXT_PERSON_ID, "1");
    long id = Long.parseLong(strID);

    /**
     * If the properties file is lost for any reason the next ID value will be
     * lost. We therefore need to perform a sanity check that this is unique.
     */
    boolean validID = false;
    while (!validID) {
      if (findPersonById(Long.toString(id)) == null) {
        validID = true;
      } else {
        id = id + 1;
      }
    }

    long newId = id + 1;
    setProperty(PROPERTY_SIMAL_NEXT_PERSON_ID, Long.toString(newId));
    try {
      save();
    } catch (Exception e) {
      logger.warn("Unable to save properties file", e);
      throw new SimalRepositoryException(
          "Unable to save properties file when creating the next person ID", e);
    }
    return Long.toString(id);
  }
}

package uk.ac.osswatch.simal.rdf;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;

import org.openrdf.elmo.ElmoModule;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.elmo.sesame.SesameManagerFactory;
import org.openrdf.model.Resource;
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

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IRCS;
import uk.ac.osswatch.simal.model.IVersion;
import uk.ac.osswatch.simal.model.elmo.Person;
import uk.ac.osswatch.simal.model.elmo.Project;
import uk.ac.osswatch.simal.model.elmo.RCS;
import uk.ac.osswatch.simal.model.elmo.Version;
import uk.ac.osswatch.simal.rdf.io.AnnotatingRDFXMLHandler;

/**
 * A class for handling common repository actions. Applications should not
 * instantiate this class but should interact with it via its static methods.
 * 
 */
public class SimalRepository {
	public static final String TEST_FILE_BASE_URL = "http://exmple.org/baseURI";
	public static final String TEST_FILE_URI_NO_QNAME = "testNoRDFAboutDOAP.xml";
	public static final String DEFAULT_NAMESPACE_URI = "http://simal/oss-watch.ac.uk/defaultNS#";
	private static SailRepository _repository;

	private SimalRepository() {
		super();
	}

	private static RepositoryConnection getConnection()
			throws SimalRepositoryException {
		return getManager().getConnection();
	}

	/**
	 * Add a an RDF file from a given URL.
	 * 
	 * @param url
	 * @throws SimalRepositoryException
	 * @throws RepositoryException
	 * @throws IOException
	 * @throws RDFParseException
	 */
	public static void addProject(URL url, String baseURI)
			throws SimalRepositoryException {
		verifyInitialised();

		RDFParser parser = new RDFXMLParser();
		// FIXME: use a proper file location
		File annotatedFile;
		try {
			annotatedFile = new File("annotatedRDFXML.xml");
			parser.setRDFHandler(new AnnotatingRDFXMLHandler(
					annotatedFile));
		} catch (IOException e) {
			throw new SimalRepositoryException(
					"Unable to write the annotated RDF/XML file: " + e.getMessage(), e);
		}
		parser.setVerifyData(true);

		RepositoryConnection con = getConnection();
		try {
			parser.parse(url.openStream(), baseURI);
			con.add(annotatedFile, baseURI, RDFFormat.RDFXML);
		} catch (RDFParseException e) {
			throw new SimalRepositoryException(
					"Attempt to add unparseable RDF/XML to the repository: " + e.getMessage(), e);
		} catch (RepositoryException e) {
			throw new SimalRepositoryException(
					"Unable to access the repository: " + e.getMessage(), e);
		} catch (IOException e) {
			throw new SimalRepositoryException(
					"Unable to read the RDF/XML file: " + e.getMessage(), e);
		} catch (RDFHandlerException e) {
			throw new SimalRepositoryException("Problem handling RDF data: " + e.getMessage(), e);
		} finally {
			try {
				con.close();
				annotatedFile.delete();
			} catch (RepositoryException e) {
				throw new SimalRepositoryException(
						"Unable to close the connection: " + e.getMessage(), e);
			}
		}
	}

	/**
	 * Checks to see if the repository has been correctly initialised. If it has
	 * not then an exception is thrown.
	 * 
	 * @throws SimalRepositoryException
	 */
	private static void verifyInitialised() throws SimalRepositoryException {
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
	private static SesameManager getManager() throws SimalRepositoryException {
		verifyInitialised();
		ElmoModule module = new ElmoModule();
		module.recordRole(org.openrdf.concepts.doap.Project.class);
		module.recordRole(org.openrdf.concepts.doap.Version.class);
		module.recordRole(org.openrdf.concepts.doap.Repository.class);
		module.recordRole(org.openrdf.concepts.foaf.Person.class);

		SesameManagerFactory factory = new SesameManagerFactory(module,
				_repository);
		return factory.createElmoManager();
	}

	/**
	 * Get a project from the repository.
	 * 
	 * @param qname
	 *            the QName of the project to retrieve
	 * @return the project, or if no project with the given QName exists Null
	 * @throws SimalRepositoryException
	 */
	public static Project getProject(QName qname)
			throws SimalRepositoryException {
		verifyInitialised();

		org.openrdf.concepts.doap.Project elmoProject = getManager().find(
				org.openrdf.concepts.doap.Project.class, qname);
		if (elmoProject == null) {
			return null;
		}
		return new Project(elmoProject);
	}

	/**
	 * Get a release version from the repository.
	 * 
	 * @param qname
	 *            the QName of the release version to retrieve
	 * @return the release version, or if no release version with the given
	 *         QName exists Null
	 * @throws SimalRepositoryException
	 */
	public static IVersion getVersion(QName qname)
			throws SimalRepositoryException {
		verifyInitialised();

		org.openrdf.concepts.doap.Version elmoVersion = getManager().find(
				org.openrdf.concepts.doap.Version.class, qname);
		if (elmoVersion == null) {
			return null;
		}
		return new Version(elmoVersion);
	}

	/**
	 * Get a version control repository from the repository.
	 * 
	 * @param qname
	 *            the QName of the repository to retrieve
	 * @return the repository or if no repository with the given QName exists
	 *         Null
	 * @throws SimalRepositoryException
	 * @throws SimalRepositoryException
	 */
	public static IRCS getRCS(QName qname) throws SimalRepositoryException {
		verifyInitialised();

		org.openrdf.concepts.doap.Repository elmoRepo = getManager().find(
				org.openrdf.concepts.doap.Repository.class, qname);
		if (elmoRepo == null) {
			return null;
		}
		return new RCS(elmoRepo);
	}

	/**
	 * Get a person from the repository.
	 * 
	 * @param qname
	 *            the QName of the repository to retrieve
	 * @return the repository or if no repository with the given QName exists
	 *         Null
	 * @throws SimalRepositoryException
	 */
	public static IPerson getPerson(QName qname)
			throws SimalRepositoryException {
		verifyInitialised();

		org.openrdf.concepts.foaf.Person elmoPerson = getManager().find(
				org.openrdf.concepts.foaf.Person.class, qname);
		if (elmoPerson == null) {
			return null;
		}
		return new Person(elmoPerson);
	}

	public static Set<Project> getAllProjects() throws SimalRepositoryException {
		verifyInitialised();

		HashSet<Project> result = new HashSet<Project>();
		Iterator<org.openrdf.concepts.doap.Project> elmoProjects = getManager()
				.findAll(org.openrdf.concepts.doap.Project.class).iterator();
		org.openrdf.concepts.doap.Project project;
		while (elmoProjects.hasNext()) {
			project = elmoProjects.next();
			result.add(new Project(project));
		}
		return result;
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
	public static void writeXML(Writer writer, QName qname)
			throws SimalRepositoryException {
		verifyInitialised();

		Resource resource = new URIImpl(qname.toString());
		RDFXMLPrettyWriter XMLWriter = new RDFXMLPrettyWriter(writer);
		try {
			_repository.getConnection().exportStatements(resource,
					(URI) null, (Value) null, true, XMLWriter);
		} catch (RepositoryException e) {
			throw new SimalRepositoryException(
					"Unable to connect to the repository", e);
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
	private static void addTestData() throws SimalRepositoryException {
		verifyInitialised();

		try {
			addProject(SimalRepository.class
					.getResource(TEST_FILE_URI_NO_QNAME), TEST_FILE_BASE_URL);
		} catch (SimalRepositoryException e2) {
			// Yes, we expected that
			// This is here so that the relevant test will fail
			// if this entity makes it into the repo.
		}

		// Local test files
		try {
			addProject(SimalRepository.class.getResource("testDOAP.xml"),
					TEST_FILE_BASE_URL);

			addProject(SimalRepository.class.getResource("ossWatchDOAP.xml"),
					TEST_FILE_BASE_URL);

			addProject(new URL(
					"http://simal.oss-watch.ac.uk/projectDetails/codegoo.rdf"),
					"http://simal.oss-watch.ac.uk");
		} catch (Exception e) {
			throw new RuntimeException(
					"Can't add the test data, there's no point in carrying on",
					e);
		}
	}

	/**
	 * Get the default QName for a Project. The default QName should be used if
	 * the original resource does not provide a QName.
	 * 
	 * @param project
	 *            the project for which we need a QName
	 * @return
	 */
	public static QName getDefaultQName(
			org.openrdf.concepts.doap.Project project) {
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
	public static String getAllProjectsAsJSON() throws SimalRepositoryException {
		StringBuffer json = new StringBuffer("{ \"items\": [");
		Iterator<Project> projects = getAllProjects().iterator();
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
	public static boolean isInitialised() {
		return _repository != null;
	}

	/**
	 * Shutdown the repository cleanly.
	 * 
	 * @throws SimalRepositoryException
	 * 
	 * @throws RepositoryException
	 */
	public static void destroy() throws SimalRepositoryException {
		verifyInitialised();

		try {
			_repository.shutDown();
			_repository = null;
		} catch (RepositoryException e) {
			throw new SimalRepositoryException(
					"Unable to shutdown the repository, refusing to destroy it.");
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
	public static void initialise() throws SimalRepositoryException {
		if (_repository != null) {
			throw new SimalRepositoryException(
					"Illegal attempt to create a second SimalRepository in the same JAVA VM.");
		}

		_repository = new SailRepository(new MemoryStore());
		try {
			_repository.initialize();
		} catch (RepositoryException e) {
			throw new SimalRepositoryException(
					"Unable to intialise the repository", e);
		}

		addTestData();
	}
}

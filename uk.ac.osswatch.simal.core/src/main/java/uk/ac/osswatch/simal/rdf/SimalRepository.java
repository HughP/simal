package uk.ac.osswatch.simal.rdf;

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

import uk.ac.osswatch.simal.model.elmo.Project;
import uk.ac.osswatch.simal.rdf.io.ValidatingRDFXMLHandler;

/**
 * A class for handling common repository actions.
 * 
 */
public class SimalRepository {
	private static SailRepository _repository;

	/**
	 * Create a default repository. Currently this creates a volatile repository
	 * populaed with test data.
	 * 
	 * @throws SimalRepositoryException
	 */
	public SimalRepository() throws SimalRepositoryException {
		_repository = new SailRepository(new MemoryStore());
		try {
			_repository.initialize();
		} catch (RepositoryException e) {
			throw new SimalRepositoryException(
					"Unable to intialise the repository", e);
		}

		addTestData();
	}

	public RepositoryConnection getConnection() {
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
	public void addProject(URL url, String baseURI)
			throws SimalRepositoryException {
		RDFParser parser = new RDFXMLParser();
		parser.setRDFHandler(new ValidatingRDFXMLHandler());
		parser.setVerifyData(true);

		RepositoryConnection con = this.getConnection();
		try {
			try {
				parser.parse(url.openStream(), baseURI);
			} catch (RDFHandlerException e) {
				if (e.getMessage().equals(
						ValidatingRDFXMLHandler.NO_QNAME_PRESENT)) {
					throw new SimalRepositoryException(
							"The RDF/XML representation does not appear to have a QName, Simal will not handle anonymous projects",
							e);
				} else {
					throw new SimalRepositoryException(
							"Unable to access the repository", e);
				}
			}
			con.add(url, baseURI, RDFFormat.RDFXML);
		} catch (RDFParseException e) {
			throw new SimalRepositoryException(
					"Attempt to add unparseable RDF/XML to the repository", e);
		} catch (RepositoryException e) {
			throw new SimalRepositoryException(
					"Unable to access the repository", e);
		} catch (IOException e) {
			throw new SimalRepositoryException(
					"Unable to read the RDF/XML file", e);
		} finally {
			try {
				con.close();
			} catch (RepositoryException e) {
				throw new SimalRepositoryException(
						"Unable to close the connection", e);
			}
		}
	}

	/**
	 * Get a manager for concepts defined by Elmo.
	 * 
	 * @return
	 */
	public SesameManager getManager() {
		ElmoModule module = new ElmoModule();
		module.recordRole(org.openrdf.concepts.doap.Project.class);

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
	 */
	public Project getProject(QName qname) {
		org.openrdf.concepts.doap.Project elmoProject = getManager().find(
				org.openrdf.concepts.doap.Project.class, qname);
		if (elmoProject == null) {
			return null;
		}
		return new Project(elmoProject);
	}

	public Set<Project> getAllProjects() {
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
	public void writeXML(Writer writer, QName qname)
			throws SimalRepositoryException {
		getProject(qname);
		RDFXMLPrettyWriter XMLWriter = new RDFXMLPrettyWriter(writer);
		try {
			_repository.getConnection().exportStatements((Resource) null,
					(URI) null, (Value) null, true, XMLWriter);
		} catch (RepositoryException e) {
			throw new SimalRepositoryException(
					"Unable to connect to the repository", e);
		} catch (RDFHandlerException e) {
			throw new SimalRepositoryException(
					"Unable to handle the generated RDF/XML", e);
		}
	}

	public void close() {
		getManager().close();
	}

	/**
	 * Adds test data to the repo. be careful to only use this when the repo in
	 * use is a test repository.
	 * 
	 * @throws SimalRepositoryException
	 */
	private void addTestData() {
		try {
			addProject(SimalRepository.class
					.getResource("testNoRDFAboutDOAP.xml"),
					"http://exmple.org/baseURI");
		} catch (SimalRepositoryException e2) {
			// Yes, we expected that
			// This is here so that the relevant test will fail
			// if this entity makes it into the repo.
		}

		// Local test files
		try {
			addProject(SimalRepository.class.getResource("testDOAP.xml"),
					"http://exmple.org/baseURI");

			addProject(SimalRepository.class.getResource("ossWatchDOAP.xml"),
					"http://exmple.org/baseURI");

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
	 * Get all the projects in the repository and return them in a
	 * single JSON file.
	 * 
	 * @return
	 */
	public String getAllProjectsAsJSON() {
		StringBuffer json = new StringBuffer("{ \"projects\": [");
		Iterator<Project> projects = this.getAllProjects().iterator();
		while (projects.hasNext()) {
			json.append(projects.next().toJSON(true));
			if (projects.hasNext()) {
				json.append(",");
			}
		}
		json.append("]}");
		return json.toString();
	}
}

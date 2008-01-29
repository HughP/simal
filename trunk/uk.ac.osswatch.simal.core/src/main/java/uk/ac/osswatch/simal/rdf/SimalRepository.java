package uk.ac.osswatch.simal.rdf;

import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
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
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;
import org.openrdf.sail.memory.MemoryStore;

import uk.ac.osswatch.simal.model.elmo.Project;

/**
 * A class for handling common repository actions.
 * 
 */
public class SimalRepository {
	private static SailRepository _repository;

	/**
	 * Create a default repository.
	 * Currently this creates a volatile repository populaed with
	 * test data.
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

	public RepositoryConnection getConnection() throws SimalRepositoryException {
		try {
			return _repository.getConnection();
		} catch (RepositoryException e) {
			throw new SimalRepositoryException(
					"Unable to get a connection to the repository", e);
		}
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
		RepositoryConnection con = this.getConnection();
		try {
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
		org.openrdf.concepts.doap.Project elmoProject = getManager().find(org.openrdf.concepts.doap.Project.class, qname);
		if (elmoProject == null) {
			return null;
		}
		return new Project(elmoProject);
	}
	
	public Set<Project> getAllProjects() {
		HashSet<Project> result = new HashSet<Project>();
		Iterator<org.openrdf.concepts.doap.Project> elmoProjects = getManager().findAll(org.openrdf.concepts.doap.Project.class).iterator();
		while (elmoProjects.hasNext()) {
			result.add(new Project(elmoProjects.next()));
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
	public void writeXML(Writer writer, QName qname) throws SimalRepositoryException {
		SesameManager manager = getManager();
		manager.find(Project.class, qname);
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
		manager.close();
	}

	public void close() {
		getManager().close();
	}
	
	/**
	 * Adds test data to the repo. be careful to only use this when
	 * the repo in use is a test repository.
	 * 
	 * @throws SimalRepositoryException
	 */
	private void addTestData() throws SimalRepositoryException {
		// Local test files
		addProject(SimalRepository.class.getResource("testDOAP.xml"), "http://exmple.org/baseURI");
		addProject(SimalRepository.class.getResource("testNoRDFAboutDOAP.xml"), "http://exmple.org/baseURI");
		try {
			// WebPA project file
			addProject(new URL("http://webpaproject.lboro.ac.uk/doap.rdf"), "http://webpaproject.lboro.ac.uk");
			// Add OSS Watch from Simal demo site
			addProject(new URL("http://simal.oss-watch.ac.uk/projectDetails/ossWatch.rdf"), "http://simal.oss-watch.ac.uk");
		} catch (MalformedURLException e) {
			throw new SimalRepositoryException("Unable to add WebPA as test data", e);
		}
	}
}

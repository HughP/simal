package uk.ac.osswatch.simal.model.jcr;
/*
 * Copyright 2007 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import javax.jcr.ItemExistsException;
import javax.jcr.LoginException;
import javax.jcr.PathNotFoundException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.manager.impl.ObjectContentManagerImpl;
import org.apache.jackrabbit.ocm.query.Filter;
import org.apache.jackrabbit.ocm.query.Query;
import org.apache.jackrabbit.ocm.query.QueryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.hp.hpl.jena.vocabulary.RDF;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.model.ModelSupport;
import uk.ac.osswatch.simal.rdf.AbstractSimalRepository;
import uk.ac.osswatch.simal.rdf.Doap;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalNamespaceContext;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;
import uk.ac.osswatch.simal.service.IPersonService;
import uk.ac.osswatch.simal.service.IProjectService;

public class JcrSimalRepository extends AbstractSimalRepository {
    public static final Logger logger = LoggerFactory
      .getLogger(JcrSimalRepository.class);

	private static final String JCR_MAPPING = "jcr/mapping.xml";
	private static ISimalRepository instance;
	private Session session = null;
	private ObjectContentManager ocm = null;

	private TransientRepository repository;

	/**
	 * Use getInstance instead.
	 */
	private JcrSimalRepository() throws SimalRepositoryException {
		super();
	}
	
	/**
	 * Get the ObectContentManager for this repository. The OCM manages
	 * mappings between POJOs and the repository.
	 */
	public ObjectContentManager getObjectContentManager() {
		return ocm;
	}

	/**
	 * Get the SimalRepository object. Note that only one of these can exist in
	 * a single virtual machine.
	 * 
	 * @return
	 * @throws SimalRepositoryException
	 */
	public static ISimalRepository getInstance()
			throws SimalRepositoryException {
		if (instance == null) {
			instance = new JcrSimalRepository();
		}
		return instance;
	}

	public void add(String data) throws SimalRepositoryException {
		throw new UnsupportedOperationException();

	}

	/**
	 * FIXME: this method should be in the ProjectService class
	 */
	public void addProject(Document doc, URL url, String baseURI)
			throws SimalRepositoryException {
		NodeList projects = doc.getElementsByTagNameNS(Doap.NS, "Project");
		if (projects.getLength() == 0) {
			throw new SimalRepositoryException("Unable to add a project from the supplied document");
		} else if (projects.getLength() > 1) {
			throw new SimalRepositoryException("There is more than one project defined in the supplied document");
		}

		IProjectService projectService = SimalRepositoryFactory.getProjectService();
		IPersonService personService = SimalRepositoryFactory.getPersonService();
		
		Node projectNode = projects.item(0);
		Node about = projectNode.getAttributes().getNamedItemNS(RDF.getURI(), "about");
		String id = projectService.getNewProjectID();
		String uri;
		if (about != null) {
		    uri = about.getTextContent();
		} else {
			uri = RDFUtils.getDefaultProjectURI(id);
		}
		IProject project;
		try {
			project = projectService.createProject(uri);
			project.setSimalID(id);
		} catch (DuplicateURIException e) {
			throw new SimalRepositoryException("The project already exists, currently we don't knwo how to merge data. URI = " + uri, e);
		}
		
		XPathFactory xpFactory = XPathFactory.newInstance();
		XPath xpath = xpFactory.newXPath();
		xpath.setNamespaceContext(new SimalNamespaceContext());
		try {
			Object value = xpath.evaluate("//doap:name", projectNode);
			project.addName((String)value);
			
			value = xpath.evaluate("//doap:shortDesc", projectNode);
			project.addName((String)value);
			
			value = xpath.evaluate("//doap:description", projectNode);
			project.addName((String)value);
			
			value = xpath.evaluate("//doap:developer/foaf:Person", projectNode, XPathConstants.NODESET);
			for (int i =0; ((NodeList)value).getLength() > i; i ++) {
				Node node = ((NodeList)value).item(i);
				IPerson person = personService.createFromFoaf(node);
			}
		} catch (XPathExpressionException e) {
			throw new SimalRepositoryException("Problem with XPath experession", e);
		}
				
		projectService.save(project);
	}

	public void addRDFXML(URL url, String baseURL)
			throws SimalRepositoryException {
		throw new UnsupportedOperationException();

	}

	public void addRDFXML(Document doc) throws SimalRepositoryException {
		throw new UnsupportedOperationException();

	}

	public boolean containsOrganisation(String uri) {
		throw new UnsupportedOperationException();
	}

	// TODO: this method should be in a PersonService class
	public boolean containsPerson(String uri) {
		QueryManager queryManager = ocm.getQueryManager(); 
		Filter filter = queryManager.createFilter(Person.class); 
		filter.addEqualTo("URI", uri);
		 
		Query query = queryManager.createQuery(filter); 
		Person person = (Person) ocm.getObject(query); 
		return person != null;
	}

	/**
	 * @deprecated use ProjectService.containsProject() instead
	 */
	public boolean containsProject(String uri) {
		throw new UnsupportedOperationException();
	}

	// TODO: this method should be in a ResourceService class
	public boolean containsResource(String uri) {
		QueryManager queryManager = ocm.getQueryManager(); 
		Filter filter = queryManager.createFilter(Resource.class); 
		filter.addEqualTo("URI", uri);
		 
		Query query = queryManager.createQuery(filter); 
		Resource resource = (Resource) ocm.getObject(query); 
		return resource != null;
	}

	public IDoapCategory createCategory(String uri)
			throws SimalRepositoryException, DuplicateURIException {
		throw new UnsupportedOperationException();
		
	}

	public IDoapHomepage createHomepage(String uri)
			throws SimalRepositoryException, DuplicateURIException {
		throw new UnsupportedOperationException();
		
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
	    
	    IPerson person= new Person(getEntityID(personID));
	  
	    ocm.insert(person);
	    
	    return person;
	}

	public IProject createProject(String uri) throws SimalRepositoryException,
			DuplicateURIException {
		throw new UnsupportedOperationException();
		
	}

	public void destroy() throws SimalRepositoryException {
		session.logout();
		((org.apache.jackrabbit.api.JackrabbitRepository)repository).shutdown();
	}

	public Set<IPerson> filterPeopleByName(String filter) {
		throw new UnsupportedOperationException();
		
	}

	public Set<IProject> filterProjectsByName(String filter) {
		throw new UnsupportedOperationException();
		
	}

	public Set<IProject> filterProjectsBySPARQL(String queryStr) {
		throw new UnsupportedOperationException();
		
	}

	public IDoapCategory findCategoryById(String id)
			throws SimalRepositoryException {
		throw new UnsupportedOperationException();
		
	}

	public IPerson findPersonById(String id) throws SimalRepositoryException {
		throw new UnsupportedOperationException();
		
	}

	public IPerson findPersonBySeeAlso(String seeAlso)
			throws SimalRepositoryException {
		throw new UnsupportedOperationException();
		
	}

	public IPerson findPersonBySha1Sum(String sha1sum)
			throws SimalRepositoryException {
		throw new UnsupportedOperationException();
		
	}

	public IProject findProjectByHomepage(String homepage)
			throws SimalRepositoryException {
		throw new UnsupportedOperationException();
		
	}

	public IProject findProjectById(String id) throws SimalRepositoryException {
		throw new UnsupportedOperationException();
		
	}

	public IProject findProjectBySeeAlso(String seeAlso)
			throws SimalRepositoryException {
		throw new UnsupportedOperationException();
		
	}

	public Set<IDoapCategory> getAllCategories()
			throws SimalRepositoryException {
		throw new UnsupportedOperationException();
		
	}

	public Set<IPerson> getAllPeople() throws SimalRepositoryException {
		throw new UnsupportedOperationException();
		
	}

	public String getAllPeopleAsJSON() throws SimalRepositoryException {
		throw new UnsupportedOperationException();
		
	}

	@SuppressWarnings("unchecked")
	public Set<IProject> getAllProjects() throws SimalRepositoryException {
		QueryManager queryManager = ocm.getQueryManager();

		Filter filter = queryManager.createFilter(Project.class);
		Query query = queryManager.createQuery(filter);
		Collection<IProject> result = ocm.getObjects(query);
		return new HashSet<IProject>(result);
	}

	public String getAllProjectsAsJSON() throws SimalRepositoryException {
		throw new UnsupportedOperationException();
		
	}

	public IDoapCategory getCategory(String uri)
			throws SimalRepositoryException {
		throw new UnsupportedOperationException();
		
	}

	public IDoapHomepage getHomepage(String uri) {
		throw new UnsupportedOperationException();
		
	}

	public IDoapCategory getOrCreateCategory(String uri)
			throws SimalRepositoryException {
		throw new UnsupportedOperationException();
		
	}

	public IProject getOrCreateProject(String uri)
			throws SimalRepositoryException {
		throw new UnsupportedOperationException();
		
	}
	
	public IPerson getPerson(String uri) throws SimalRepositoryException {
		throw new UnsupportedOperationException();
		
	}

	public IProject getProject(String uri) throws SimalRepositoryException {
		throw new UnsupportedOperationException();
		
	}

	public IResource getResource(String uri) {
		throw new UnsupportedOperationException();
		
	}

	public void initialise(String directory) throws SimalRepositoryException {
        if (isTest) {
			try {
				repository = new TransientRepository();
			} catch (IOException e) {
				throw new SimalRepositoryException("Unable to create repository", e);
			}
	        
			try {
				session = repository.login(
				        new SimpleCredentials("username", "password".toCharArray()));
				String[] files = {
						ISimalRepository.class.getClassLoader().getResource(JCR_MAPPING).getFile()
					  };

				ocm = new ObjectContentManagerImpl(session, files);
			} catch (LoginException e) {
				throw new SimalRepositoryException("Unable to login to repository", e);
			} catch (RepositoryException e) {
	
				throw new SimalRepositoryException("Unable to access repository", e);
			}
	        
			initialiseRepositoryStructure();
		    initialised = true;

			try {
				ModelSupport.addTestData(this);
		    } catch (Exception e) {
				throw new SimalRepositoryException("Unable to add test data", e);
		    }
        } else {
			try {
				// FIXME: repos that are not test ones should not be transient
				repository = new TransientRepository();
			} catch (IOException e) {
				throw new SimalRepositoryException("Unable to create repository", e);
			}
	        
			try {
				session = repository.login(
				        new SimpleCredentials("username", "password".toCharArray()));
				String[] files = {
						ISimalRepository.class.getClassLoader().getResource(JCR_MAPPING).getFile()
					  };

				ocm = new ObjectContentManagerImpl(session, files);
			} catch (LoginException e) {
				throw new SimalRepositoryException("Unable to login to repository", e);
			} catch (RepositoryException e) {
	
				throw new SimalRepositoryException("Unable to access repository", e);
			}
			
			initialiseRepositoryStructure();
		    initialised = true;
        }
	}

	private void initialiseRepositoryStructure()
			throws SimalRepositoryException {
		try {
			try {
				session.getRootNode().addNode("person");
			} catch (ItemExistsException e) {
				logger.warn("Attempt to create project node that already exists");
			}
			try {
				session.getRootNode().addNode("project");
			} catch (ItemExistsException e) {
				logger.warn("Attempt to create project node that already exists");
			}
			try {
				session.getRootNode().addNode("rcs");
			} catch (ItemExistsException e) {
				logger.warn("Attempt to create rcs node that already exists");
			}
		} catch (ItemExistsException e) {
			logger.warn("Attempt to create a node that already exists");
		} catch (Exception e) {
			throw new SimalRepositoryException("Unable to add root node", e);
		}
	}

	public void removeAllData() {
		throw new UnsupportedOperationException();
	}

	public void writeBackup(Writer writer) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Get a JCR node with the path relative to the root of this repository.
	 * @param relPath
	 * @return
	 * @throws SimalRepositoryException 
	 * @throws SimalRepositoryException 
	 * @throws RepositoryException 
	 * @throws PathNotFoundException 
	 */
	public javax.jcr.Node getNode(String relPath) throws SimalRepositoryException {
		try {
			return session.getRootNode().getNode(relPath);
		} catch (PathNotFoundException e) {
			StringTokenizer tk = new StringTokenizer(relPath, "/");
			String path = "";
			javax.jcr.Node node = null;
			while (tk.hasMoreTokens()) {
				path = path + tk.nextToken();
				try {
					node = session.getRootNode().addNode(path);
				} catch (ItemExistsException e1) {
					// we can ignore this since we are just building the path
				} catch (PathNotFoundException e1) {
					// we can ignore this since we are just building the path
				} catch (RepositoryException e1) {
					throw new SimalRepositoryException("Unable to create the ID node", e1);
				}
				path = path + '/';
			}
			return node;
		} catch (RepositoryException e1) {
			throw new SimalRepositoryException("Unable to create the ID node", e1);
		}
	}

}

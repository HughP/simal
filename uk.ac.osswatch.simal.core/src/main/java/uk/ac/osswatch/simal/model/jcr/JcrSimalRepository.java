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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;

import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.manager.impl.ObjectContentManagerImpl;
import org.apache.jackrabbit.core.TransientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IOrganisation;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.model.ModelSupport;
import uk.ac.osswatch.simal.model.simal.SimalOntology;
import uk.ac.osswatch.simal.rdf.AbstractSimalRepository;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;
import uk.ac.osswatch.simal.service.jena.JenaSimalRepository;

public class JcrSimalRepository extends AbstractSimalRepository {
    public static final Logger logger = LoggerFactory
      .getLogger(JcrSimalRepository.class);

	private static final String JCR_MAPPING = "jcr/mapping.xml";
	private static ISimalRepository instance;
	private Session session;

	private ObjectContentManager ocm;

	/**
	 * Use getInstance instead.
	 */
	private JcrSimalRepository() throws SimalRepositoryException {
		super();
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
		// TODO Auto-generated method stub

	}

	public void addProject(Document doc, URL url, String baseURI)
			throws SimalRepositoryException {
		// TODO Auto-generated method stub

	}

	public void addRDFXML(URL url, String baseURL)
			throws SimalRepositoryException {
		// TODO Auto-generated method stub

	}

	public void addRDFXML(Document doc) throws SimalRepositoryException {
		// TODO Auto-generated method stub

	}

	public boolean containsOrganisation(String uri) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean containsPerson(String uri) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean containsProject(String uri) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean containsResource(String uri) {
		// TODO Auto-generated method stub
		return false;
	}

	public IDoapCategory createCategory(String uri)
			throws SimalRepositoryException, DuplicateURIException {
		// TODO Auto-generated method stub
		return null;
	}

	public IDoapHomepage createHomepage(String uri)
			throws SimalRepositoryException, DuplicateURIException {
		// TODO Auto-generated method stub
		return null;
	}

	public IOrganisation createOrganisation(String uri)
			throws DuplicateURIException {
		// TODO Auto-generated method stub
		return null;
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
	    
	    IPerson person;
		try {
			person = new Person(uri);
		} catch (URISyntaxException e) {
			throw new SimalRepositoryException("Unable to create a person with the URI " + uri, e);
		}
	    person.setSimalID(personID);
	    
	    ocm.insert(person);
	    
	    return person;
	}

	public IProject createProject(String uri) throws SimalRepositoryException,
			DuplicateURIException {
		// TODO Auto-generated method stub
		return null;
	}

	public void destroy() throws SimalRepositoryException {
		session.logout();
	}

	public Set<IPerson> filterPeopleByName(String filter) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IProject> filterProjectsByName(String filter) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IProject> filterProjectsBySPARQL(String queryStr) {
		// TODO Auto-generated method stub
		return null;
	}

	public IDoapCategory findCategoryById(String id)
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IPerson findPersonById(String id) throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IPerson findPersonBySeeAlso(String seeAlso)
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IPerson findPersonBySha1Sum(String sha1sum)
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IProject findProjectByHomepage(String homepage)
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IProject findProjectById(String id) throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IProject findProjectBySeeAlso(String seeAlso)
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IDoapCategory> getAllCategories()
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IOrganisation> getAllOrganisations()
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IPerson> getAllPeople() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAllPeopleAsJSON() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IProject> getAllProjects() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAllProjectsAsJSON() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IDoapCategory getCategory(String uri)
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IDoapHomepage getHomepage(String uri) {
		// TODO Auto-generated method stub
		return null;
	}

	public IDoapCategory getOrCreateCategory(String uri)
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IProject getOrCreateProject(String uri)
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IOrganisation getOrganisation(String uri)
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IPerson getPerson(String uri) throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IProject getProject(String uri) throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IResource getResource(String uri) {
		// TODO Auto-generated method stub
		return null;
	}

	public void initialise(String directory) throws SimalRepositoryException {
        Repository repository;
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
			
		    initialised = true;

			try {
				ModelSupport.addTestData(this);
		    } catch (Exception e) {
				throw new SimalRepositoryException("Unable to add test data", e);
		    }
        } else {
        	throw new SimalRepositoryException("We don't know how to implement a not test JCR repository yet");
        }
	}

	public void removeAllData() {
		// TODO Auto-generated method stub

	}

	public void writeBackup(Writer writer) {
		// TODO Auto-generated method stub

	}

}

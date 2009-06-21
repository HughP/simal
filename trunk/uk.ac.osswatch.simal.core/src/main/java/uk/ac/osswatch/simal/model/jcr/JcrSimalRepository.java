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
import java.util.Set;

import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.core.TransientRepository;
import org.w3c.dom.Document;

import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IOrganisation;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.rdf.AbstractSimalRepository;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.IProjectService;
import uk.ac.osswatch.simal.rdf.IReviewService;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class JcrSimalRepository extends AbstractSimalRepository {

	private static ISimalRepository instance;
	private Session session;

	/**
	 * Use getInstance instead.
	 */
	private JcrSimalRepository() throws SimalRepositoryException {
		super();
        Repository repository;
		try {
			repository = new TransientRepository();
		} catch (IOException e) {
			throw new SimalRepositoryException("Unable to create repository", e);
		}
        try {
			session = repository.login(
			        new SimpleCredentials("username", "password".toCharArray()));
		} catch (LoginException e) {
			throw new SimalRepositoryException("Unable to login to repository", e);
		} catch (RepositoryException e) {

			throw new SimalRepositoryException("Unable to access repository", e);
		}
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
		// TODO Auto-generated method stub
		return null;
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

	public IPerson getOrCreatePerson(String uri)
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

	public IProjectService getProjectService() {
		// TODO Auto-generated method stub
		return null;
	}

	public IResource getResource(String uri) {
		// TODO Auto-generated method stub
		return null;
	}

	public IReviewService getReviewService() {
		// TODO Auto-generated method stub
		return null;
	}

	public void initialise() throws SimalRepositoryException {
		// TODO Auto-generated method stub

	}

	public void initialise(String directory) throws SimalRepositoryException {
		// TODO Auto-generated method stub

	}

	public void removeAllData() {
		// TODO Auto-generated method stub

	}

	public void writeBackup(Writer writer) {
		// TODO Auto-generated method stub

	}

}

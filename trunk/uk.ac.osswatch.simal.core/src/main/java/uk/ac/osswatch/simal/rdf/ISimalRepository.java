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
package uk.ac.osswatch.simal.rdf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Set;

import org.w3c.dom.Document;

import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IOrganisation;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;

/**
 * A class for handling common repository actions. Applications should not
 * instantiate this class but should interact with it via its methods.
 * 
 * @refactor methods that deal with a specific part of the model (e.g. Person,
 *           Project) should not be in here they should be in a service class
 *           for the model object
 */
public interface ISimalRepository {
  public static final String FOAF_PERSON_URI = RDFUtils.FOAF_NS + "Person";
  public static final String FOAF_KNOWS_URI = RDFUtils.FOAF_NS + "knows";
  public static final String FOAF_MBOX_SHA1SUM_URI = RDFUtils.FOAF_NS
      + "mbox_sha1sum";

  public static final String DOAP_CATEGORY_URI = RDFUtils.DOAP_NS
      + "category";
  public static final String DOAP_MAINTAINER_URI = RDFUtils.DOAP_NS
      + "maintainer";
  public static final String DOAP_DEVELOPER_URI = RDFUtils.DOAP_NS
      + "developer";
  public static final String DOAP_DOCUMENTER_URI = RDFUtils.DOAP_NS
      + "documenter";
  public static final String DOAP_HELPER_URI = RDFUtils.DOAP_NS + "helper";
  public static final String DOAP_TESTER_URI = RDFUtils.DOAP_NS + "tester";
  public static final String DOAP_TRANSLATOR_URI = RDFUtils.DOAP_NS
      + "translator";
  public static final String DOAP_VERSION_URI = RDFUtils.DOAP_NS + "Version";

  public static final String SIMAL_NAMESPACE_URI = "http://oss-watch.ac.uk/ns/0.2/simal#";
  public static final String SIMAL_PROJECT_URI = SIMAL_NAMESPACE_URI + "Project";
  public static final String SIMAL_URI_PROJECT_ID = SIMAL_NAMESPACE_URI
      + "projectId";
  public static final String SIMAL_PERSON_URI = SIMAL_NAMESPACE_URI + "Person";
  public static final String SIMAL_URI_PERSON_ID = SIMAL_NAMESPACE_URI
      + "personId";

  public static final String RDF_NAMESPACE_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

  public static final String RDFS_NAMESPACE_URI = "http://www.w3.org/2000/01/rdf-schema#";
  public static final String RDFS_URI_SEE_ALSO = RDFS_NAMESPACE_URI + "seeAlso";

  public static final String XSD_NAMESPACE_URI = "http://www.w3.org/2001/XMLSchema#";

  /**
   * Add an RDF DOAP file from a given URL.
   * 
   * @param url
   * @throws SimalRepositoryException
   */
  public void addProject(URL url, String baseURI)
      throws SimalRepositoryException;
      
  /**
   * Add an RDF DOAP node.
   * 
   * @param doc
   *          the doap document
   * @param sourceURL
   *          the URL from which this document was generated
   * @param baseURI
   * @throws SimalRepositoryException
   */
  public void addProject(Document doc, URL url, String baseURI)
      throws SimalRepositoryException;
      
  
  /**
   * Get a project from the repository. If the project does not exist then
   * return null.
   * 
   * @param uri
   *          the URI of the project to retrieve
   * @return the project, or if no project with the given String exists Null
   * @throws SimalRepositoryException
   * 
   * @deprecated Use ProjectService.getProject(uri) instead
   */
  public IProject getProject(String uri) throws SimalRepositoryException;
  
  /**
   * Get an organisation from the repository. If the organisation does not exist then
   * return null.
   * 
   * @param uri
   *          the URI of the organisation to retrieve
   * @return the organisation, or if no project with the given String exists Null
   * @throws SimalRepositoryException
   */
  public IOrganisation getOrganisation(String uri) throws SimalRepositoryException;

  /**
   * Get a project from the repository. If the project does not yet exist return null.
   * 
   * @param uri
   *          the String of the category to retrieve
   * @return the category, or if no category with the given String exists Null
   * @throws SimalRepositoryException
   */
  public IDoapCategory getCategory(String uri) throws SimalRepositoryException;

  /**
   * Get a project from the repository. If the project does not yet exist it will be created.
   * 
   * @param uri
   *          the String of the category to retrieve or create
   * @return the category resource
   * @throws SimalRepositoryException
   */
  public IDoapCategory getOrCreateCategory(String uri) throws SimalRepositoryException;

  /**
   * Get a category with a given simal id.
   * 
   * @param id
   * @return
   * @throws SimalRepositoryException
   */
  public IDoapCategory findCategoryById(String id)
      throws SimalRepositoryException;

  /**
   * Get a person from the repository.
   * 
   * @param uri
   *          the String of the repository to retrieve
   * @return the repository or if no repository item with the given String
   *         exists Null
   * @throws SimalRepositoryException
   */
  public IPerson getPerson(String uri) throws SimalRepositoryException;

  /**
   * Get a person with a given Simal id.
   * 
   * @param id -
   *          world unique Simal ID
   * @return
   * @throws SimalRepositoryException
   *           if the ID is not a world unique one.
   */
  public IPerson findPersonById(String id) throws SimalRepositoryException;

  /**
   * Get a person with a given MBOX SHA1 SUM.
   * 
   * @param sha1sum
   * @return
   * @throws SimalRepositoryException
   */
  public IPerson findPersonBySha1Sum(String sha1sum)
      throws SimalRepositoryException;

  /**
   * Get a person with a given rdf:seeAlso attribute.
   * 
   * @param seeAlso
   * @return
   * @throws SimalRepositoryException
   */
  public IPerson findPersonBySeeAlso(String seeAlso)
      throws SimalRepositoryException;
  
  /**
   * Get a project with a given rdf:seeAlso value.
   * 
   * @param seeAlso
   * @return
   * @throws SimalRepositoryException
   * 
   * @deprecated use ProjectService.findProjectBySeeAlso(seeAlso) instead
   */
  public IProject findProjectBySeeAlso(String seeAlso)
      throws SimalRepositoryException;

  /**
   * Get a project with a given homepage.
   * 
   * @param homepage
   * @return
   * @throws SimalRepositoryException
   */
  public IProject findProjectByHomepage(String homepage)
      throws SimalRepositoryException;

  /**
   * Get a project with a given simal id.
   * 
   * @param id
   * @return
   * @throws SimalRepositoryException
   */
  public IProject findProjectById(String id) throws SimalRepositoryException;

  /**
   * Get all the projects known in this repository.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public Set<IProject> getAllProjects() throws SimalRepositoryException;

  /**
   * Get all the organisations known in this repository.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public Set<IOrganisation> getAllOrganisations() throws SimalRepositoryException;

  /**
   * Get all the categories known in this repository.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public Set<IDoapCategory> getAllCategories() throws SimalRepositoryException;

  /**
   * Get all the people known in this repository.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public Set<IPerson> getAllPeople() throws SimalRepositoryException;

  /**
   * Add an RDF/XML file, other than one supported by more specialised methods,
   * such as addProject(...).
   * 
   * @param categoriesRdf
   * @param testFileBaseUrl
   * @throws SimalRepositoryException
   */
  public void addRDFXML(URL url, String baseURL)
      throws SimalRepositoryException;

  /**
   * Get all the projects in the repository and return them in a single JSON
   * file.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public String getAllProjectsAsJSON() throws SimalRepositoryException;

  /**
   * Get all the people in the repository and return them in a single JSON file.
   * 
   * @return
   * @throws SimalRepositoryException
   * @throws SimalRepositoryException
   */
  public String getAllPeopleAsJSON() throws SimalRepositoryException;

  /**
   * Shutdown the repository cleanly.
   * 
   * @throws SimalRepositoryException
   * 
   * @throws RepositoryException
   */
  public void destroy() throws SimalRepositoryException;

  /**
   * Create a new project in the repository.
   * 
   * @return
   * @throws SimalRepositoryException
   *           if an error is thrown whilst communicating with the repository
   * @throws DuplicateURIException
   *           if an entity with the given String already exists
   */
  public IProject createProject(String uri) throws SimalRepositoryException,
      DuplicateURIException;

  /**
   * Create a new project category in the repository.
   * 
   * @return
   * @throws SimalRepositoryException
   *           if an error is thrown whilst communicating with the repository
   * @throws DuplicateURIException
   *           if an entity with the given String already exists
   */
  public IDoapCategory createCategory(String uri) throws SimalRepositoryException,
      DuplicateURIException;

  /**
   * Create a new person in the repository.
   * 
   * @return
   * @throws SimalRepositoryException
   *           if an error is thrown whilst communicating with the repository
   * @throws DuplicateURIException
   *           if an entity with the given String already exists
   */
  public IPerson createPerson(String uri) throws SimalRepositoryException,
      DuplicateURIException;

  /**
   * Create a new homepage in the repository.
   * 
   * @param uri a URI to identify this homepage
   * @return
   * @throws SimalRepositoryException
   *           if an error is thrown whilst communicating with the repository
   * @throws DuplicateURIException
   *           if an entity with the given String already exists
   */
  public IDoapHomepage createHomepage(String uri) throws SimalRepositoryException,
      DuplicateURIException;

  /**
   * Create a new project ID and save the next value in the properties file.
   * 
   * @throws IOException
   * @throws FileNotFoundException
   */
  public String getNewProjectID() throws SimalRepositoryException;

  /**
   * Create a new category ID and save the next value in the properties file.
   * 
   * @throws IOException
   * @throws FileNotFoundException
   */
  public String getNewCategoryID() throws SimalRepositoryException;

  /**
   * Create a new person ID and save the next local value in the properties
   * file.
   * 
   * @throws IOException
   * @throws FileNotFoundException
   */
  public String getNewPersonID() throws SimalRepositoryException;

  /**
   * Add the RDF data contained in the supplied data string.
   * 
   * @param data
   *          the RDF data to add
   * @throws SimalRepositoryException
   */
  public void add(String data) throws SimalRepositoryException;

  /**
   * Set whether or not this repository is to be set up in test mode. This
   * method should be called before initialise if the default behaviour is to be
   * altered. In test mode the repository is pre-populated with data and all
   * changes are lost once the rpository is destroyed.
   * 
   * @param newValue
   *          true if this is to be a test repository
   * @throws SimalRepositoryException
   */
  public void setIsTest(boolean newValue) throws SimalRepositoryException;

  /**
   * Return true if this repository is running in test mode.
   * 
   * @return
   * @see setIsTest
   */
  public boolean isTest();

  /**
   * Initialise a default repository. If the repository is not in test mode then
   * a database will be created in the locations set in the
   * default.simal.properties or local.simal.properties files.
   * 
   * @throws SimalRepositoryException
   */
  public void initialise() throws SimalRepositoryException;

  /**
   * Initialise a repository.
   * 
   * @param directory
   *          the directory for the database if it is a persistent repository
   *          (i.e. not a test repo). A null results in the database being
   *          stored in the locations set in the default.simal.properties or
   *          local.simal.properties files.
   * 
   * @throws SimalRepositoryException
   */
  public void initialise(String directory) throws SimalRepositoryException;

  /**
   * Return true if this repository has been successfully initialised and is
   * ready to be used, otherwise return false.
   * 
   * @return
   */
  public boolean isInitialised();

  /**
   * Test to see if a project with the given String exists.
   * 
   * @param uri
   * @return
   * 
   * @deprecated Use ProjectService.containsProject(uri) instead
   */
  public boolean containsProject(String uri);

  /**
   * Test to see if an organisation with the given String exists.
   * 
   * @param uri
   * @return
   */
  public boolean containsOrganisation(String uri); 

  /**
   * Test to see if a person with the given String exists.
   * 
   * @param uri
   * @return
   */
  public boolean containsPerson(String uri);

  /**
   * Test to see if a resource with the given URI exists.
   * 
   * @param uri
   * @return
   */
  public boolean containsResource(String uri);

  /**
   * Add all RDF/XML files found in a directory to the repository. Only files
   * with a ".xml" or ".rdf" extension will be processed. Subdirectories will be
   * ignored.
   * 
   * @throws SimalRepositoryException
   */
  public void addXMLDirectory(final String dirName)
      throws SimalRepositoryException;

  /**
   * Remove all data from this repository. Be very careful with this method. It
   * does not provide a way to retrieve the data.
   */
  public void removeAllData();

  /**
   * Get a resource of unknown type.
   * 
   * @param uri
   *          the URI of the resource
   * @return
   */
  public IResource getResource(String uri);

  /**
   * Check to see if a person already exists in the repository with the supplied
   * EMail address. If they exist return the person otherwise return null.
   * 
   * @param person
   * @return the duplicate person or null
   * @throws SimalRepositoryException
   */
  public IPerson getDuplicate(String email) throws SimalRepositoryException;

  /**
   * Given an entity ID create a unique Simal ID for referencing this entity
   * within this instance of Simal.
   * 
   * @param instanceID
   * @return
   * @throws SimalRepositoryException
   */
  public String getUniqueSimalID(String entityID)
      throws SimalRepositoryException;

  /**
   * Given a world unique Simal ID return the entity ID for the entity being
   * identified
   * 
   * @param uniqueID
   * @return
   * @throws SimalRepositoryException
   *           If the supplied ID is not a valid Simal ID or if there is a
   *           problem communicating with the repository.
   */
  public String getEntityID(String uniqueID) throws SimalRepositoryException;

  /**
   * Tests to see if the ID we have is a world unique simal ID or not.
   * 
   * @param id
   * @return
   */
  public boolean isUniqueSimalID(String is);

  /**
   * Write an N3 representation of the complete model in this repository
   * for backup purposes.
   * @param writer The writer to write the N3 to
   */
  public void writeBackup(Writer writer);

  /**
   * Return all the projects with a name that matches the
   * supplied regular expression. The filter
   * is not case sensitive.
   * 
   * 
   * @param value
   * @return
   */
  public Set<IProject> filterProjectsByName(String filter);
  
  /**
   * Find all projects returned using a SPARQL query.
   * 
   * @param queryStr
   * @return
   */
  public Set<IProject> filterProjectsBySPARQL(String queryStr);

  /**
   * Return all the people with a name that matches the
   * supplied regular expression. The filter
   * is not case sensitive.
   * 
   * 
   * @param value
   * @return
   */
  public Set<IPerson> filterPeopleByName(String filter);  
  
  /**
   * Add an RDF/XML Document.
   * 
   * @param doc
   * @throws SimalRepositoryException 
   */
  public void addRDFXML(Document doc) throws SimalRepositoryException;
  
  /**
   * Create a new organisation object.
   * 
   * @param uri the URI for this organisation
   * 
   * @return
 * @throws DuplicateURIException 
   */
  public IOrganisation createOrganisation(String uri) throws DuplicateURIException;

  /**
   * Get an homepage resources. If the resource does not yet exist return null.
   * 
   * @param uri URI of the homepage resource
   * @return
   */
  public IDoapHomepage getHomepage(String uri);

  /**
   * Get a person from the repository. If the person does not yet exist it is created.
   * 
   * @param personURI
   * @return
   * @throws SimalRepositoryException 
   */
   public IPerson getOrCreatePerson(String uri) throws SimalRepositoryException;

   /**
    * Get a project from the repository. If the project does not yet exist it is created.
    * 
    * @param personURI
    * @return
    * @throws SimalRepositoryException 
    */
    public IProject getOrCreateProject(String uri) throws SimalRepositoryException;
    
	/**
	 * Get an instance of a project service operating on this repository.
	 * @param repo
	 * @return
	 */
	public IProjectService getProjectService();

	/**
	 * Get an instance of a review service operating on this repository.
	 * @return
	 */
	public IReviewService getReviewService();

}

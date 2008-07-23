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

import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;

/**
 * A class for handling common repository actions. Applications should not
 * instantiate this class but should interact with it via its methods.
 * 
 * @refactor methods that deal with a specific part of the model 
 * (e.g. Person, Project) should not be in here they should be in a
 * service class for the model object
 */
public interface ISimalRepository {
  // FIXME: standardise names of constants
  public static final String TEST_FILE_BASE_URL = "http://exmple.org/baseURI";
  public static final String TEST_FILE_URI_NO_QNAME = "testNoRDFAboutDOAP.xml";
  public static final String TEST_FILE_URI_WITH_QNAME = "testDOAP.xml";
  public static final String TEST_FILE_REMOTE_URL = "http://svn.apache.org/repos/asf/velocity/site/site/doap_anakia.rdf";
  public static final String DEFAULT_PROJECT_NAMESPACE_URI = "http://simal.oss-watch.ac.uk/doap/";
  public static final String DEFAULT_PERSON_NAMESPACE_URI = "http://simal.oss-watch.ac.uk/foaf/";
  public static final String DEFAULT_CATEGORY_NAMESPACE_URI = "http://simal.oss-watch.ac.uk/defaultCategoryNS#";

  public static final String FOAF_NAMESPACE_URI = "http://xmlns.com/foaf/0.1/";
  public static final String FOAF_PERSON_URI = FOAF_NAMESPACE_URI + "Person";
  public static final String FOAF_KNOWS_URI = FOAF_NAMESPACE_URI + "knows";
  public static final String FOAF_MBOX_SHA1SUM_URI = FOAF_NAMESPACE_URI
      + "mbox_sha1sum";

  public static final String DOAP_NAMESPACE_URI = "http://usefulinc.com/ns/doap#";
  public static final String DOAP_PROJECT_URI = DOAP_NAMESPACE_URI + "Project";
  public static final String DOAP_CATEGORY_URI = DOAP_NAMESPACE_URI
      + "category";
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
  public static final String DOAP_VERSION_URI = DOAP_NAMESPACE_URI + "Version";

  public static final String SIMAL_NAMESPACE_URI = "http://simal.oss-watch.ac.uk/ns/0.2/simal#";
  public static final String SIMAL_URI_PROJECT_ID = SIMAL_NAMESPACE_URI
      + "projectId";
  public static final String SIMAL_URI_PERSON_ID = SIMAL_NAMESPACE_URI
      + "personId";

  public static final String RDF_NAMESPACE_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

  public static final String RDFS_NAMESPACE_URI = "http://www.w3.org/2000/01/rdf-schema#";
  public static final String RDFS_URI_SEE_ALSO = RDFS_NAMESPACE_URI + "seeAlso";

  public final static String CATEGORIES_RDF = "categories.xml";

  /**
   * Add an RDF DOAP file from a given URL.
   * 
   * @param url
   * @throws SimalRepositoryException 
   * @throws SimalRepositoryException
   */
  public void addProject(URL url, String baseURI) throws SimalRepositoryException;

  /**
   * Get a project from the repository. If the project does not
   * exist then return null.
   * 
   * @param uri
   *          the String of the project to retrieve
   * @return the project, or if no project with the given String exists Null
   * @throws SimalRepositoryException
   */
  public IProject getProject(String uri) throws SimalRepositoryException;

  /**
   * Get a project from the repository.
   * 
   * @param uri
   *          the String of the project to retrieve
   * @return the project, or if no project with the given String exists Null
   * @throws SimalRepositoryException
   */
  public IDoapCategory findCategory(String uri)
      throws SimalRepositoryException;

  /**
   * Get a person from the repository.
   * 
   * @param uri
   *          the String of the repository to retrieve
   * @return the repository or if no repository item with the given String exists
   *         Null
   * @throws SimalRepositoryException
   */
  public IPerson getPerson(String uri) throws SimalRepositoryException;

  /**
   * Get a person with a given simal id.
   * 
   * @param id
   * @return
   * @throws SimalRepositoryException
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
   * Write an RDF/XML document representing a project. If the project does not
   * exist an empty RDF/XML document is written.
   * 
   * @param writer
   * @param uri
   * @throws SimalRepositoryException
   * @throws RepositoryException
   * @throws RDFHandlerException
   */
  public void writeXML(Writer writer, String uri)
      throws SimalRepositoryException;

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
   * Create a new project ID and save the next value in the properties file.
   * 
   * @throws IOException
   * @throws FileNotFoundException
   */
  public String getNewProjectID() throws SimalRepositoryException;

  /**
   * Create a new person ID and save the next value in the properties file.
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
   * Set whether or not this repository is to be set up in test mode. this
   * method should be called before initialise if the default behaviour is to be
   * altered/
   * 
   * @param newValue
   *          true if this is to be a test repository
   * @throws SimalRepositoryException
   */
  public void setIsTest(boolean newValue) throws SimalRepositoryException;
  
  /**
   * Initialise a default repository. Currently this creates a volatile
   * repository populated with test data.
   * 
   * @throws SimalRepositoryException
   * 
   * @throws SimalRepositoryException
   */
  public void initialise() throws SimalRepositoryException;

  /**
   * Return true if this repository has been successfully initialised and is
   * ready to be used, otherwise return false.
   * 
   * @return
   */
  public boolean isInitialised();

  /**
   * Test to see if a project with the given String exists.
   * @param uri
   * @return
   */
  public boolean containsProject(String uri);

  /**
   * Test to see if a person with the given String exists.
   * @param uri
   * @return
   */
  public boolean containsPerson(String uri);

  /**
   * Test to see if a category with the given String exists.
   * @param uri
   * @return
   */
  public boolean containsCategory(String uri);
}

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
package uk.ac.osswatch.simal.model;

import java.util.HashSet;
import java.util.Set;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * Extra data provided by simal.Project objects over and above that provided by
 * doap.Project objects.
 * 
 */
public interface IProject extends IDoapResource, IProjectService {

  /**
   * Set the Simal ID for this project. This is a unique identifier within the
   * repository from which it was retrieved.
   * 
   * @return
   */
  public void setSimalID(String newID) throws SimalRepositoryException;

  /**
   * Get all the people known to be engaged with this project.
   * 
   * @return
   */
  public HashSet<IPerson> getAllPeople();

  /**
   * Get the default name for this project.
   */
  public String getName();

  /**
   * Get the default short description name for this project.
   */
  public String getShortDesc();

  /**
   * Set the default short description name for this project.
   */
  public void setShortDesc(String desc);

  /**
   * Get the default description name for this project.
   */
  public String getDescription();

  /**
   * Set the default description name for this project.
   */
  public void setDescription(String desc);

  /**
   * Get a set of categories that this project belongs to.
   */
  public Set<IDoapCategory> getCategories();

  /**
   * Get a set of homepages for this project.
   * 
   * @return
   */
  public Set<IDoapHomepage> getHomepages();

  /**
   * Get a set of old homepages for this project.
   * 
   * @return
   */
  public Set<IDoapHomepage> getOldHomepages();

  /**
   * Get all developers who work on this project.
   */
  public Set<IPerson> getDevelopers();

  /**
   * Get all documenters who work on this project.
   */
  public Set<IPerson> getDocumenters();

  /**
   * Get all helpers who work on this project.
   */
  public Set<IPerson> getHelpers();

  /**
   * Get all maintainers who work on this project.
   */
  public Set<IPerson> getMaintainers();

  /**
   * Get all testers who work on this project.
   */
  public Set<IPerson> getTesters();

  /**
   * Get all translators who work on this project.
   */
  public Set<IPerson> getTranslators();

  /**
   * Get all Issue Trackers registered for this project.
   */
  public Set<IDoapBugDatabase> getIssueTrackers();

  /**
   * Get all releases associated with this project.
   */
  public Set<IDoapRelease> getReleases();

  /**
   * Get all licences associated with this project.
   */
  public Set<IDoapLicence> getLicences();

  /**
   * Get all mailing lists associated with this project.
   */
  public Set<IDoapMailingList> getMailingLists();

  /**
   * Get all repositories associated with this project.
   */
  public Set<IDoapRepository> getRepositories();

  /**
   * Get all screen shots associated with this project.
   */
  public Set<IDoapScreenshot> getScreenshots();

  /**
   * Get all operating systems this project can be associated with.
   * 
   */
  public Set<String> getOSes();

  /**
   * Get all programming languages this project can be associated with.
   * 
   */
  public Set<String> getProgrammingLanguages();

  /**
   * Get all wikis associated with this project.
   */
  public Set<IDoapWiki> getWikis();

  /**
   * Get all download pages associated with this project.
   */
  public Set<IDoapDownloadPage> getDownloadPages();

  /**
   * Get all download mirrors associated with this project.
   */
  public Set<IDoapDownloadMirror> getDownloadMirrors();

  /**
   * Remove a person as a developer on this project. If the person is assigned
   * other roles in the project these will remain intact. This method does not
   * remove the person record from the repository.
   * 
   * @param person
   * @throws SimalRepositoryException
   */
  public void removeDeveloper(IPerson person) throws SimalRepositoryException;

  /**
   * Add a person as a developer on this project. If the person does not yet
   * exist in the repository they will be added to the repository first.
   * 
   * @param person
   */
  public void addDeveloper(IPerson person);

  /**
   * Remove a person as a maintainer on this project. If the person is assigned
   * other roles in the project these will remain intact. This method does not
   * remove the person record from the repository.
   * 
   * @param person
   * @throws SimalRepositoryException
   */
  public void removeMaintainer(IPerson person) throws SimalRepositoryException;

  /**
   * Add a person as a maintainer on this project. If the person does not yet
   * exist in the repository they will be added to the repository first.
   * 
   * @param person
   */
  public void addMaintainer(IPerson person);

  /**
   * Remove a person as a tester on this project. If the person is assigned
   * other roles in the project these will remain intact. This method does not
   * remove the person record from the repository.
   * 
   * @param person
   * @throws SimalRepositoryException
   */
  public void removeTester(IPerson person) throws SimalRepositoryException;

  /**
   * Add a person as a tester on this project. If the person dows not yet exist
   * in the repository they will be added to the repository first.
   * 
   * @param person
   */
  public void addTester(IPerson person);

  /**
   * Remove a person as a helper on this project. If the person is assigned
   * other roles in the project these will remain intact. This method does not
   * remove the person record from the repository.
   * 
   * @param person
   * @throws SimalRepositoryException
   */
  public void removeHelper(IPerson person) throws SimalRepositoryException;

  /**
   * Add a person as a helper on this project. If the person dows not yet exist
   * in the repository they will be added to the repository first.
   * 
   * @param person
   */
  public void addHelper(IPerson person);

  /**
   * Remove a person as a documenter on this project. If the person is assigned
   * other roles in the project these will remain intact. This method does not
   * remove the person record from the repository.
   * 
   * @param person
   * @throws SimalRepositoryException
   */
  public void removeDocumenter(IPerson person) throws SimalRepositoryException;

  /**
   * Add a person as a documenter on this project. If the person dows not yet
   * exist in the repository they will be added to the repository first.
   * 
   * @param person
   */
  public void addDocumenter(IPerson person);

  /**
   * Remove a person as a translator on this project. If the person is assigned
   * other roles in the project these will remain intact. This method does not
   * remove the person record from the repository.
   * 
   * @param person
   * @throws SimalRepositoryException
   */
  public void removeTranslator(IPerson person) throws SimalRepositoryException;

  /**
   * Add a person as a translator on this project. If the person dows not yet
   * exist in the repository they will be added to the repository first.
   * 
   * @param person
   */
  public void addTranslator(IPerson person);
}
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
public interface IProject extends IDoapResource {

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
   * Add a category to this project.
   * 
   * @param category the category resource
   */
  public void addCategory(IDoapCategory category);
  
  /**
   * Get a set of categories that this project belongs to.
   */
  public Set<IDoapCategory> getCategories();

  /**
   * Get a set of homepages for this project.
   * 
   * @return
   */
  public Set<IDocument> getHomepages();

  /**
   * Add a homepages for this project.
   * 
   * @param homepage - the homepage to add
   * @return
   */
  public void addHomepage(IDocument homepage);

  /**
   * Remove a homepages for this project.
   * 
   * @param homepage - the homepage to remove
   * @return
   */
  public void removeHomepage(IDocument homepage);

  /**
   * Get a set of old homepages for this project.
   * 
   * @return
   */
  public Set<IDocument> getOldHomepages();

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
   * Set the maintainers who work on this project.
   * @param maintainers
   */
  public void setMaintainers(Set<IPerson> maintainers);
  
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
  public Set<IDocument> getIssueTrackers();

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
   * Set all mailing lists associated with this project.
   */
  public void setMailingLists(Set<IDoapMailingList> lists);
  
  /**
   * Add a mailing list to the lists recorded for this project.
   * @param list
   */
  public void addMailingList(IDoapMailingList list);

  /**
   * Get all repositories associated with this project.
 * @throws SimalRepositoryException 
   */
  public Set<IDoapRepository> getRepositories() throws SimalRepositoryException;

  /**
   * Add a repository to the project..
 * @throws SimalRepositoryException 
   */
  public void addRepository(IDoapRepository rcs) throws SimalRepositoryException;

  /**
   * Get all screen shots associated with this project.
   */
  public Set<IDocument> getScreenshots();

  /**
   * Add a screenshot to the collection of screenshots.
   * 
   * @param screenshot
   */
  public void addScreenshot(IDocument screenshot);

  /**
   * Remove a screenshot from the collection of screenshots.
   * 
   * @param screenshot
   */
  public void removeScreenshot(IDocument screenshot);

  /**
   * Get all operating systems this project can be associated with.
   * 
   */
  public Set<String> getOSes();

  /**
   * Set all operating systems this project can be associated with.
   * 
   * @param Set<String> OSes
   */
  public void setOSes(Set<String> oses);
  
  /**
   * Get all programming languages this project can be associated with.
   * 
   */
  public Set<String> getProgrammingLanguages();

  /**
   * Set all programming languages this project can be associated with.
   * 
   * @param Set<String> programming languages
   */
  public void setProgrammingLanguages(Set<String> programmingLanguages);

  /**
   * Get all wikis associated with this project.
   */
  public Set<IDocument> getWikis();

  /**
   * Add a wiki to the collection of wikis.
   * 
   * @param wiki
   */
  public void addWiki(IDocument wiki);

  /**
   * Remove a wiki from the collection of wikis.
   * 
   * @param wiki
   */
  public void removeWiki(IDocument wiki);

  /**
   * Get all blogs associated with this project.
   */
  public Set<IDocument> getBlogs();

  /**
   * Get all download pages associated with this project.
   */
  public Set<IDocument> getDownloadPages();

  /**
   * Add a download page to the collection of download pages.
   * 
   * @param downloadPage
   */
  public void addDownloadPage(IDocument downloadPage);

  /**
   * Remove a download page from the collection of download pages.
   * 
   * @param downloadPage
   */
  public void removeDownloadPage(IDocument downloadPage);

  /**
   * Get all download mirrors associated with this project.
   */
  public Set<IDocument> getDownloadMirrors();

  /**
   * Add a download mirror to the collection of download mirrors.
   * 
   * @param downloadMirror
   */
  public void addDownloadMirror(IDocument downloadMirror);

  /**
   * Remove a download mirror from the collection of download mirrors.
   * 
   * @param downloadMirror
   */
  public void removeDownloadMirror(IDocument downloadMirror);

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
   * Add a person as a translator on this project. If the person does not yet
   * exist in the repository they will be added to the repository first.
   * 
   * @param person
   */
  public void addTranslator(IPerson person);

  /**
   * Get the data feeds associated with this project.
   * 
   * @return
   */
  public Set<IFeed> getFeeds();
  
  /**
   * Get a value between 0 and 100 that represents the openness of the project development model.
   * 0 is low, 100 is high. This value is only available for projects that have been manually 
   * reviewed at least once. However, even when a project had been reviewed we cannot assume that
   * the value returned is up to date.
   *  
   * @throws SimalRepositoryException thrown is there is a problem calculating the openness rating or if
   * no manual review has been conducted.
   */
 public int getOpennessRating() throws SimalRepositoryException;

  /**
   * Set the revision control repositories that are attached to the project.
   * 
   * @param repos
   * @throws SimalRepositoryException
   */
  public void setRepositories(Set<IDoapRepository> repos)
      throws SimalRepositoryException;

  /**
   * Set the homepages associated with this project.
   * 
   * @param homepages
   */
  public void setHomepages(Set<IDocument> homepages);

  /**
   * Set the issue trackers associated with this project.
   * 
   * @param homepages
   */
  public void setIssueTrackers(Set<IDocument> trackers);

  /**
   * Add an issue tracker for this project
   * 
   * @param tracker
   */
  public void addIssueTracker(IDocument tracker);

  /**
   * Remove an issue tracker from this project
   * 
   * @param tracker
   */
  public void removeIssueTracker(IDocument tracker);

  /**
   * Set the collection of releases.
   * 
   * @param releases
   */
  public void setReleases(Set<IDoapRelease> releases);

  /**
   * Add a release to the collection of releases.
   * 
   * @param release
   */
  public void addRelease(IDoapRelease release);

}

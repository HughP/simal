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
 * Behaviour for a simal.Project object
 *
 * @see uk.ac.osswatch.simal.model.elmo.DoapProjectBehaviour
 */
public interface IDoapProjectBehaviour extends IDoapResourceBehaviour {

  /**
   * Get all the people known to be engaged with this project.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public HashSet<IPerson> getAllPeople() throws SimalRepositoryException;
  
  /**
   * Get the default name for this project.
   */
  public String getName();
  
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
} 

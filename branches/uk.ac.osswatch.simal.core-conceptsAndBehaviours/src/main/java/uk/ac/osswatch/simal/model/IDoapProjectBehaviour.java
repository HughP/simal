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
   * Get the ID of this project. If no ID has been assigned
   * yet then get the next available ID from the repository 
   * and assign that.
   * @throws SimalRepositoryException 
   */
  public String getID() throws SimalRepositoryException;
  
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
  public Set<IIssueTracker> getIssueTrackers();
} 

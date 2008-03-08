package uk.ac.osswatch.simal.model;

import java.util.HashSet;
import java.util.Set;

import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * An interface for wrapping repository specific representations of a Project.
 * Other classes should not access the repository classes directly, instead they
 * should access the data through a class that implements this interface.
 * 
 * @see uk.ac.osswatch.simal.model.elmo.Project
 */
@rdf("http://simal.oss-watch.ac.uk/ns/simal#Project")
public interface IProject extends IDoapResource {

  public Set<IDoapResource> getIssueTrackers();

  public Set<IDoapResource> getCategories();

  public Set<IPerson> getDevelopers() throws SimalRepositoryException;

  public Set<IPerson> getDocumenters() throws SimalRepositoryException;

  public Set<IDoapResource> getDownloadMirrors();

  public Set<IDoapResource> getDownloadPages();

  public Set<IPerson> getHelpers() throws SimalRepositoryException;

  public Set<IDoapResource> getHomepages();

  public Set<IDoapResource> getMailingLists();

  public Set<IPerson> getMaintainers() throws SimalRepositoryException;

  public Set<IDoapResource> getOldHomepages();

  public Set<String> getOSes();

  public Set<String> getProgrammingLangauges();

  public Set<IVersion> getReleases() throws SimalRepositoryException;

  public Set<IRCS> getRepositories() throws SimalRepositoryException;

  public Set<IDoapResource> getScreenshots();

  public Set<IPerson> getTesters() throws SimalRepositoryException;

  public Set<IPerson> getTranslators() throws SimalRepositoryException;

  public Set<IDoapResource> getWikis();

  /**
   * Get all the people known to be engaged with this project.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public HashSet<IPerson> getAllPeople() throws SimalRepositoryException;
  
  /**
   * Get the Simal ID for this project. This is a unique identifier
   * within the repository from which it was retrieved.
   * 
   * @return
   * @throws SimalRepositoryException 
   */
  @rdf("http://simal.oss-watch.ac.uk/ns/simal#project-id")
  public String getID() throws SimalRepositoryException;
}

package uk.ac.osswatch.simal.model;

import java.util.HashSet;
import java.util.Set;

import uk.ac.osswatch.simal.model.elmo.Resource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * An interface for wrapping repository specific representations of a Project.
 * Other classes should not access the repository classes directly, instead they
 * should access the data through a class that implements this interface.
 * 
 * @see uk.ac.osswatch.simal.model.elmo.Project
 */
public interface IProject extends IDoapResource {

	public Set<Resource> getIssueTrackers();

	public Set<Resource> getCategories();

	public Set<IPerson> getDevelopers() throws SimalRepositoryException;

	public Set<IPerson> getDocumenters() throws SimalRepositoryException;

	public Set<Resource> getDownloadMirrors();

	public Set<Resource> getDownloadPages();

	public Set<IPerson> getHelpers() throws SimalRepositoryException;

	public Set<Resource> getHomepages();

	public Set<Resource> getMailingLists();

	public Set<IPerson> getMaintainers() throws SimalRepositoryException;

	public Set<Resource> getOldHomepages();

	public Set<String> getOSes();

	public Set<String> getProgrammingLangauges();

	public Set<IVersion> getReleases() throws SimalRepositoryException;

	public Set<IRCS> getRepositories() throws SimalRepositoryException;

	public Set<Resource> getScreenshots();

	public Set<IPerson> getTesters() throws SimalRepositoryException;

	public Set<IPerson> getTranslators() throws SimalRepositoryException;

	public Set<Resource> getWikis();

	/**
	 * Get all the people known to be engaged with this project.
	 * 
	 * @return
	 * @throws SimalRepositoryException
	 */
	public HashSet<IPerson> getAllPeople() throws SimalRepositoryException;
}

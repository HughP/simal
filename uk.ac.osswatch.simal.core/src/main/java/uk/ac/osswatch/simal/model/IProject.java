package uk.ac.osswatch.simal.model;

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

	public Set<String> getIssueTrackers();

	public Set<String> getCategories();

	public Set<IPerson> getDevelopers() throws SimalRepositoryException;

	public Set<IPerson> getDocumenters() throws SimalRepositoryException;

	public Set<String> getDownloadMirrors();

	public Set<String> getDownloadPages();

	public Set<IPerson> getHelpers() throws SimalRepositoryException;

	public Set<String> getHomepages();

	public Set<Resource> getMailingLists();

	public Set<IPerson> getMaintainers() throws SimalRepositoryException;

	public Set<String> getOldHomepages();

	public Set<String> getOSes();

	public Set<String> getProgrammingLangauges();

	public Set<IVersion> getReleases() throws SimalRepositoryException;

	public Set<IRCS> getRepositories() throws SimalRepositoryException;

	public Set<String> getScreenshots();

	public Set<IPerson> getTesters() throws SimalRepositoryException;

	public Set<IPerson> getTranslators() throws SimalRepositoryException;

	public Set<String> getWikis();
}

package uk.ac.osswatch.simal.model;

import java.util.Set;

/**
 * An interface for wrapping repository specific representations of a Project.
 * Other classes should not access the repository classes directly, instead they
 * should access the data through a class that implements this interface.
 * 
 * @see uk.ac.osswatch.simal.model.elmo.Project
 */
public interface IProject extends IDoapResource {

	public Set<String> getIssueTracker();

	public Set<String> getCategories();

	public Set<IPerson> getDevelopers();

	public Set<IPerson> getDocumentors();

	public Set<String> getDownloadMirrors();

	public Set<String> getDownloadPages();

	public Set<IPerson> getHelpers();

	public Set<String> getHomepages();

	public Set<String> getMailingLists();

	public Set<IPerson> getMaintainers();

	public Set<String> getOldHomepages();

	public Set<String> getOSes();

	public Set<String> getProgrammingLangauges();

	public Set<IVersion> getReleases();

	public Set<IRepository> getRepositories();

	public Set<String> getScreenshots();

	public Set<IPerson> getTesters();

	public Set<IPerson> getTranslators();

	public Set<String> getWikis();
}

package uk.ac.osswatch.simal.rdf;

import java.util.Set;

import uk.ac.osswatch.simal.model.IProject;


/**
 * A class for working with projects in the repository.
 * 
 * @TODO refactor appropriate methods in SimalRepository into this class
 *
 */
public interface IProjectService {
	
	/**
	 * Get all projects in the repository that have a revision control system recorded.
	 * 
	 * @return
	 */
	public Set<IProject> getProjectsWithRCS();

	/**
	 * Get all projects in the repository that have a homepage recorded.
	 * 
	 * @return
	 */
	public Set<IProject> getProjectsWithHomepage();

	/**
	 * Get all projects in the repository that have a mailing list recorded.
	 * 
	 * @return
	 */
	public Set<IProject> getProjectsWithMailingList();

	/**
	 * Get all projects in the repository that have a Maintainer recorded.
	 * 
	 * @return
	 */
	public Set<IProject> getProjectsWithMaintainer();

	/**
	 * Get all projects in the repository that have a release recorded.
	 * 
	 * @return
	 */
	public Set<IProject> getProjectsWithRelease();
	
}

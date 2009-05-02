package uk.ac.osswatch.simal.model;

import java.util.Set;

/**
 * An organisation as represented by a foaf:organisation element.
 */
public interface IOrganisation extends IFoafResource {
	
	  /**
	   * Get the default name for this organisation.
	   */
	  public String getName();

	/**
	 * Add a name to the organisation. Each organisation may have more than one name.
	 * 
	 * @param name
	 */
	public void addName(String name);

	/**
	 * Add a current poject to this organisation.
	 * 
	 * @param string
	 */
	public void addCurrentProject(String string);

	/**
	 * Get all the current projects for this organisation.
	 * 
	 * @return
	 */
	public Set<IProject> getCurrentProjects();
}

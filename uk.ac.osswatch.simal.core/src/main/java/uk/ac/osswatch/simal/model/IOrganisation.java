package uk.ac.osswatch.simal.model;

/**
 * An organisation as represented by a foaf:organisation element.
 */
public interface IOrganisation extends IFoafResource {
	
	  /**
	   * Get the default name for this organisation.
	   */
	  public String getName();
}

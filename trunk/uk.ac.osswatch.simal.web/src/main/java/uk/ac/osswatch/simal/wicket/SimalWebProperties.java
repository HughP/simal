package uk.ac.osswatch.simal.wicket;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A class for accessing and managing SimalWebProperties.
 * 
 *  @REFACTOR currently this class extends the core SimalProperties class.
 *  This means that all web related proeprties are stored in the same location
 *  as the core properties, this is not ideal. 
 *  See http://code.google.com/p/simal/issues/detail?id=118
 */
public class SimalWebProperties extends SimalProperties {
	public final static String ADMIN_USERNAME = "simal.web.admin.username";
	public final static String ADMIN_PASSWORD = "simal.web.admin.password";
	
	public SimalWebProperties() throws SimalRepositoryException {
		super();
	}
}

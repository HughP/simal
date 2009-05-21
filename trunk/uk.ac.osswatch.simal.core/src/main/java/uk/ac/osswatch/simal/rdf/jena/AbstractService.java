package uk.ac.osswatch.simal.rdf.jena;

import uk.ac.osswatch.simal.rdf.IProjectService;
import uk.ac.osswatch.simal.rdf.ISimalRepository;

public abstract class AbstractService {
	ISimalRepository repo;
	

	/**
	 * Set the repository this service is to work on.
	 * @param repo
	 */
	protected void setRepository(ISimalRepository repo) {
		this.repo = repo;
	}
	
	/**
	 * Return the repository this service is operating on.
	 */
	protected ISimalRepository getRepository() {
		return this.repo;
	}
	
}

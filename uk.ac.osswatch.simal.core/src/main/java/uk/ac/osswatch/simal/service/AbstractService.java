package uk.ac.osswatch.simal.service;

import uk.ac.osswatch.simal.rdf.ISimalRepository;

public abstract class AbstractService {
	ISimalRepository repo;
	

	/**
	 * Set the repository this service is to work on.
	 * @param simalRepository
	 */
	protected void setRepository(ISimalRepository simalRepository) {
		this.repo = simalRepository;
	}
	
	/**
	 * Return the repository this service is operating on.
	 */
	protected ISimalRepository getRepository() {
		return this.repo;
	}
	
}

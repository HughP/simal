package uk.ac.osswatch.simal.service;

import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * An interface defining the common methods in a servce object.
 * 
 */
public interface IService {

	/**
	 * Save a resource.
	 * 
	 * @param resource
	 * @throws SimalRepositoryException
	 */
	public void save(IResource resource) throws SimalRepositoryException;
}

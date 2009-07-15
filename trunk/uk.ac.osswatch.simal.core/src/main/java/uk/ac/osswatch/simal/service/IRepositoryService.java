package uk.ac.osswatch.simal.service;

import uk.ac.osswatch.simal.model.IDoapRepository;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A repository service provides methods for working with revision control repository objects.
 * 
 */
public interface IRepositoryService extends IService {

	/**
	 * Create a new Revision Control Repository.
	 * 
	 * @param uri
	 * @return
	 * @throws SimalRepositoryException
	 * @throws DuplicateURIException
	 */
	public IDoapRepository create(String uri) throws SimalRepositoryException,
			DuplicateURIException;

	/**
	 * Get a new ID for a Revision Control repository.
	 * @return
	 * @throws SimalRepositoryException
	 */
	public String getNewID() throws SimalRepositoryException;

	/**
	 * Get a revision control repository by Simal ID
	 *
	 * @param fullID
	 * @return
	 */
	public IDoapRepository getById(String fullID);

}

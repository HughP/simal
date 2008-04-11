package uk.ac.osswatch.simal.wicket.data;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.UserApplication;

/**
 * A person data provider that allows the persons to be sorted.
 * 
 */
public class SortablePersonDataProvider extends SortableFoafResourceDataProvider {
	private static final long serialVersionUID = -2975592768329164790L;

  /**
	 * Create a SortableDataProvider containing all projects in the repository
	 * 
	 * @param size
	 * @throws SimalRepositoryException 
	 */
	public SortablePersonDataProvider() throws SimalRepositoryException {
		super(UserApplication.getRepository().getAllPeople());
	}
}

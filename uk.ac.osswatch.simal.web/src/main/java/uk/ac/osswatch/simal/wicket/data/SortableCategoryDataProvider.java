package uk.ac.osswatch.simal.wicket.data;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.UserApplication;

/**
 * A project data provider that allows the projects to be sorted.
 * 
 */
public class SortableCategoryDataProvider extends SortableDoapResourceDataProvider {
	private static final long serialVersionUID = -7078982000589847543L;

	/**
	 * Create a SortableDataProvider containing all projects in the repository
	 * 
	 * @param size
	 * @throws SimalRepositoryException 
	 */
	public SortableCategoryDataProvider() throws SimalRepositoryException {
		super(UserApplication.getRepository().getAllCategories());
	}
}

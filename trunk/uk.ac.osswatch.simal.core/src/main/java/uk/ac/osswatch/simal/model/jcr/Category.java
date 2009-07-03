package uk.ac.osswatch.simal.model.jcr;

import java.util.Set;

import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class Category extends DoapResource implements IDoapCategory {
	
	public Category(String simalID) throws SimalRepositoryException {
		super(simalID);
	}

	private static final long serialVersionUID = 1L;

	public Set<IPerson> getPeople() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IProject> getProjects() {
		// TODO Auto-generated method stub
		return null;
	}

}

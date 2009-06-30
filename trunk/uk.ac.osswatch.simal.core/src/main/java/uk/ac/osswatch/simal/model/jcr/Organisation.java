package uk.ac.osswatch.simal.model.jcr;

import java.util.Set;

import uk.ac.osswatch.simal.model.IOrganisation;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class Organisation extends Resource implements IOrganisation {
	private static final long serialVersionUID = 1L;

	public Organisation(String simalID) throws SimalRepositoryException {
		super(simalID);
	}

	public void addCurrentProject(String string) {
		// TODO Auto-generated method stub

	}

	public void addName(String name) {
		// TODO Auto-generated method stub

	}

	public Set<IProject> getCurrentProjects() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}

package uk.ac.osswatch.simal.model.jena;

import uk.ac.osswatch.simal.model.IOrganisation;

public class Organisation extends Resource implements IOrganisation {

	public Organisation(com.hp.hpl.jena.rdf.model.Resource resource) {
		super(resource);
	}

	private static final long serialVersionUID = 1L;

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}

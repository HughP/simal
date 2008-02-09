package uk.ac.osswatch.simal.model.elmo;

import uk.ac.osswatch.simal.model.IResource;

public class Resource implements IResource {
	protected org.openrdf.concepts.rdfs.Resource elmoResource;

	protected Resource() {};
	
	public Resource(org.openrdf.concepts.rdfs.Resource resource) {
		this.elmoResource = resource;
	}

	public String getLabel() {
		String label = elmoResource.getRdfsLabel();
	    if (label.equals("")) {
	    	label = elmoResource.toString();
	    }
		return label;
	}

	public String getComment() {
		return elmoResource.getRdfsComment();
	}
	
	public String toString() {
		return elmoResource.toString();
	}

}

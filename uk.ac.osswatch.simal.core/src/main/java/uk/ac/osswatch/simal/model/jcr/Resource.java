package uk.ac.osswatch.simal.model.jcr;

import java.net.URI;
import java.util.Set;

import uk.ac.osswatch.simal.model.AbstractResource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class Resource extends AbstractResource {
	private static final long serialVersionUID = 1L;
	private String comment;
	private String label;
	private Set<URI> seeAlsos;
	private String simalID;
	private Set<String> sources;

	public String getComment() {
		return comment;
	}

	public String getLabel(String defaultLabel) {
		if (label != null) {
			return label;
		}
		if (defaultLabel != null) {
			return defaultLabel;
		}
		return getURI();
	}

	public Object getRepositoryResource() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<URI> getSeeAlso() {
		return seeAlsos;
	}

	public String getUniqueSimalID() throws SimalRepositoryException {
		return simalID;
	}

	public void setSimalID(String newID) throws SimalRepositoryException {
		this.simalID = newID;
	}

	public void delete() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		
	}

	public Set<String> getSources() {
		return sources;
	}

	public String toXML() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}
}

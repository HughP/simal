package uk.ac.osswatch.simal.model.elmo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class Resource implements IResource {
	protected final org.openrdf.concepts.rdfs.Resource elmoResource;

	protected Resource() {
		this.elmoResource = null;
	};

	public Resource(org.openrdf.concepts.rdfs.Resource resource) {
		this.elmoResource = resource;
	}

	/**
	 * Get the label for this resource. If the resource does not have a defined
	 * label and fetch label is set to true then attempt to find a label in the
	 * repository, otherwise use the return value of the toString() method.
	 * 
	 * @param defaultLabel
	 * @param fetchLabel
	 * @return
	 */
	public String getLabel(boolean fetchLabel) {
		return getLabel(null, fetchLabel);
	}

	/**
	 * Get the label for this resource. If the resource does not have a defined
	 * label and fetch label is set to true then attempt to find a label in the
	 * repository, otherwise use the supplied default label (if not null) or the
	 * resource return value of the toString() method.
	 * 
	 * @param defaultLabel
	 * @param fetchLabel
	 * @return
	 */
	public String getLabel(String defaultLabel, boolean fetchLabel) {
		String label = elmoResource.getRdfsLabel();
		if (label == null || label == "") {
			try {
				label = SimalRepository.getLabel(elmoResource.getQName());
			} catch (SimalRepositoryException e) {
				// Oh well, that didn't work, it'll be dealt with later in this method, 
				// but we need to log the problem.
				e.printStackTrace();
			}
		}
		if (label == null || label.equals("")) {
			if (defaultLabel != null) {
				label = defaultLabel;
			} else {
				label = elmoResource.toString();
			}
		}
		return label;
	}

	public String getComment() {
		String comment = elmoResource.getRdfsComment();
		if (comment == null) {
			comment = "";
		}
		return elmoResource.getRdfsComment();
	}

	public String toString() {
		return elmoResource.toString();
	}

	public static Set<Resource> createResourceSet(Set<Object> set) {
		Iterator<Object> elmoResources = set.iterator();
		HashSet<Resource> results = new HashSet<Resource>(set.size());
		org.openrdf.concepts.rdfs.Resource resource;
		while (elmoResources.hasNext()) {
			resource = (org.openrdf.concepts.rdfs.Resource) elmoResources
					.next();
			if (resource instanceof org.openrdf.concepts.rdfs.Resource) {
				results.add(new Resource(resource));
			} else {
				throw new IllegalArgumentException(
						"Can only create resources from elmo resources.");
			}
		}
		return results;
	}

}

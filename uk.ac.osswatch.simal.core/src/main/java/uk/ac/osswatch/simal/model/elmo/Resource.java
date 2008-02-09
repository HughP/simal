package uk.ac.osswatch.simal.model.elmo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import uk.ac.osswatch.simal.model.IResource;

public class Resource implements IResource {
	protected org.openrdf.concepts.rdfs.Resource elmoResource;

	protected Resource() {
	};

	public Resource(org.openrdf.concepts.rdfs.Resource resource) {
		this.elmoResource = resource;
	}

	public String getLabel() {
		return getLabel(null);
	}

	public String getLabel(String defaultLabel) {
		String label = elmoResource.getRdfsLabel();
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

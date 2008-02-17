package uk.ac.osswatch.simal.model.elmo;

import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class Resource implements IResource {
	protected final org.openrdf.concepts.rdfs.Resource elmoResource;
	private String cachedLabel;

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
		if (cachedLabel != null) {
			return cachedLabel;
		}
		
		cachedLabel = elmoResource.getRdfsLabel();
		if (cachedLabel == null || cachedLabel == "") {
			try {
				cachedLabel = SimalRepository.getLabel(elmoResource.getQName());
			} catch (SimalRepositoryException e) {
				// Oh well, that didn't work, it'll be dealt with later in this method, 
				// but we need to log the problem.
				e.printStackTrace();
			}
		}
		
		if (cachedLabel == null || cachedLabel.equals("")) {
			if (defaultLabel != null) {
				cachedLabel = defaultLabel;
			} else {
				cachedLabel = elmoResource.toString();
			}
		}
		return cachedLabel;
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

}

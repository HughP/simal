package uk.ac.osswatch.simal.model.elmo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;

import uk.ac.osswatch.simal.model.IDoapResource;


/**
 * This is a wrapper around an Elmo DoapResource class.
 * It should not be instatiated directly instances
 * will be created by elmo as required when a concept
 * extends DOAPResource. Wrappers for these more granular
 * classes should be created that extend this class, when such
 * wrappers are instantiated they should set the value of
 * getDoapResource() to to wrapped elmo object.
 * 
 * @see org.openrdf.concepts.doap.DoapResource
 */
public class DoapResource extends Resource implements IDoapResource {

	private static final long serialVersionUID = -7610178891247360114L;
	
	protected DoapResource() {};
	
	/**
	 * Returns the first name in the set of names contained within the Elmo
	 * Project class.
	 */
	public String getName() {
		Set<Object> names = getDoapResource().getDoapNames();
		if (names == null) {
			return "";
		}
		return (String) names.toArray()[0];
	}

	public String getShortDesc() {
		return getDoapResource().getDoapShortdesc();
	}

	/**
	 * Sets the name of the project. Note that this assumes that there is only
	 * one name. In the event of there being more than one name it throws an
	 * exception.
	 * 
	 * @throws ProjectException
	 *             if the project has more than one name
	 */
	public void setName(String name) throws ProjectException {
		if (getDoapResource().getDoapNames() != null) {
			if (getDoapResource().getDoapNames().size() != 1) {
				throw new ProjectException(
						"Cannot set*(newValue) on values with more than one existing value, use add*(newValue) instead");
			}
			Set<Object> names = getDoapResource().getDoapNames();
			names.remove(names.toArray()[0]);
			names.add(name);
		}
	}

	public void setShortDesc(String shortDesc) {
		getDoapResource().setDoapShortdesc(shortDesc);
	}

	public QName getQName() {
		return getDoapResource().getQName();
	}

	public String toString() {
		return getName();
	}

	public String toJSON(boolean asRecord) {
		StringBuffer json = new StringBuffer();
		if (!asRecord) {
			json.append("{ \"items\": [");
		}
		json.append("{");
		json.append("\"id\":\"" + getQName().getNamespaceURI() + "\",");
		json.append("\"label\":\"" + getName() + "\",");
		json.append("\"name\":\"" + getName() + "\",");
		json.append("\"shortdesc\":\"" + getShortDesc() + "\"");
		json.append("}");
		if (!asRecord) {
			json.append("]}");
		}
		return json.toString();
	}

	public String getCreated() {
		return (String) getDoapResource().getDoapCreated().toArray()[0];
	}

	public String getDescription() {
		return getDoapResource().getDoapDescription();
	}

	@SuppressWarnings("unchecked")
	public Set<String> getLicences() {
		Set<org.openrdf.concepts.rdfs.Resource> licences = (Set) getDoapResource().getDoapLicenses();
		return getResourceURIs(licences);
	}

	protected Set<String> getResourceURIs(Set<org.openrdf.concepts.rdfs.Resource> resources) {
		Iterator<org.openrdf.concepts.rdfs.Resource> itr = resources.iterator();
		org.openrdf.concepts.rdfs.Resource resource;
		String ns;
		String local;
		Set<String> result = new HashSet<String>(getDoapResource().getDoapLicenses().size());
	    while (itr.hasNext()) {
	    	resource = (org.openrdf.concepts.rdfs.Resource) itr.next();
			ns = resource.getQName().getNamespaceURI();
			local = resource.getQName().getLocalPart();
			result.add(ns + local);
	    }
		return result;
	}

	public void setCreated(String created) throws ProjectException {
		if (getDoapResource().getDoapCreated() != null) {
			if (getDoapResource().getDoapCreated().size() != 1) {
				throw new ProjectException(
						"Cannot set*(newValue) on values with more than one existing value, use add*(newValue) instead");
			}
			Set<Object> dates = getDoapResource().getDoapCreated();
			dates.remove(dates.toArray()[0]);
			dates.add(created);
		}
	}

	public void setDescription(String newDesc) {
		getDoapResource().setDoapDescription(newDesc);
	}

	protected Set<String> convertToSetOfStrings(Set<Object> sourceSet) {
		Set<String> result = new HashSet<String>(sourceSet.size());
		Iterator<Object> itr = sourceSet.iterator();
		while (itr.hasNext()) {
			result.add(itr.next().toString());
		}
		return result;
	}
	
	public org.openrdf.concepts.doap.DoapResource getDoapResource() {
		return (org.openrdf.concepts.doap.DoapResource) elmoResource;
	}

}
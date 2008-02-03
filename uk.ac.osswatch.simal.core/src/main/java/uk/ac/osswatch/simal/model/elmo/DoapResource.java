package uk.ac.osswatch.simal.model.elmo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;

import org.openrdf.concepts.rdfs.Resource;

import uk.ac.osswatch.simal.model.IDoapResource;


/**
 * This is a wrapper around an Elmo DoapResource class.
 * 
 * @see org.openrdf.concepts.doap.DoapResource
 */
public class DoapResource implements IDoapResource {

	private static final long serialVersionUID = -7610178891247360114L;
	protected org.openrdf.concepts.doap.Project elmoProject;

	public DoapResource() {
		super();
	}

	/**
	 * Returns the first name in the set of names contained within the Elmo
	 * Project class.
	 */
	public String getName() {
		return (String) elmoProject.getDoapNames().toArray()[0];
	}

	public String getShortDesc() {
		return elmoProject.getDoapShortdesc();
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
		if (elmoProject.getDoapNames() != null) {
			if (elmoProject.getDoapNames().size() != 1) {
				throw new ProjectException(
						"Cannot set*(newValue) on values with more than one existing value, use add*(newValue) instead");
			}
			Set<Object> names = elmoProject.getDoapNames();
			names.remove(names.toArray()[0]);
			names.add(name);
		}
	}

	public void setShortDesc(String shortDesc) {
		elmoProject.setDoapShortdesc(shortDesc);
	}

	public QName getQName() {
		return elmoProject.getQName();
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
		return (String) elmoProject.getDoapCreated().toArray()[0];
	}

	public String getDescription() {
		return elmoProject.getDoapDescription();
	}

	@SuppressWarnings("unchecked")
	public Set<String> getLicences() {
		Iterator<Resource> licences = ((Set) elmoProject.getDoapLicenses()).iterator();
		Resource licence;
		String ns;
		String local;
		Set<String> result = new HashSet<String>(elmoProject.getDoapLicenses().size());
	    while (licences.hasNext()) {
	    	licence = licences.next();
			ns = licence.getQName().getNamespaceURI();
			local = licence.getQName().getLocalPart();
			result.add(ns + local);
	    }
		return result;
	}

	public void setCreated(String created) throws ProjectException {
		if (elmoProject.getDoapCreated() != null) {
			if (elmoProject.getDoapCreated().size() != 1) {
				throw new ProjectException(
						"Cannot set*(newValue) on values with more than one existing value, use add*(newValue) instead");
			}
			Set<Object> dates = elmoProject.getDoapCreated();
			dates.remove(dates.toArray()[0]);
			dates.add(created);
		}
	}

	public void setDescription(String newDesc) {
		elmoProject.setDoapDescription(newDesc);
	}

}
package uk.ac.osswatch.simal.model.elmo;

import java.util.Set;

import javax.xml.namespace.QName;


/**
 * This is a wrapper around an Elmo DoapResource class.
 * 
 * @see org.openrdf.concepts.doap.DoapResource
 */
public class DoapResource {

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
						"At present the Project wrapper can only handle a single project name.");
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

}
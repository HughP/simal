package uk.ac.osswatch.simal.model.elmo;

import javax.xml.namespace.QName;

import uk.ac.osswatch.simal.model.IFoafResource;

public class FoafResource extends Resource implements IFoafResource {
	private static final long serialVersionUID = -3852417254318582808L;
	
	protected FoafResource() {
		super();
	}
	
	public FoafResource(org.openrdf.concepts.foaf.Person person) {
		super(person);
	}

	/**
	 * Create a JSON representation of this person.
	 * 
	 * @param asRecord
	 *   set to true if this is a record within another JSON file or
	 *   false if you want this to be a complete JSON file.
	 */
	public String toJSON(boolean asRecord) {
		StringBuffer json = new StringBuffer();
		if (!asRecord) {
			json.append("{ \"items\": [");
		}
		json.append("{");
		json.append("\"id\":\"" + getQName() + "\",");
		json.append("\"label\":\"" + getLabel(true) + "\",");
		json.append("}");
		if (!asRecord) {
			json.append("]}");
		}
		return json.toString();
	}

	public QName getQName() {
		return elmoResource.getQName();
	}
	
	public String toString() {
		QName qname = elmoResource.getQName();
		return qname.getNamespaceURI() + qname.getLocalPart();
	}

	public String getGivennames() {
		return getFoafResource().getFoafGivennames().toString();
	}
	
	public org.openrdf.concepts.foaf.FoafResource getFoafResource() {
		return (org.openrdf.concepts.foaf.FoafResource)elmoResource;
	}

}

package uk.ac.osswatch.simal.model.elmo;

import javax.xml.namespace.QName;

import uk.ac.osswatch.simal.model.IFoafResource;

public class FoafResource implements IFoafResource {
	private static final long serialVersionUID = -3852417254318582808L;
	protected org.openrdf.concepts.foaf.FoafResource elmoResource;
	
	public FoafResource(org.openrdf.concepts.foaf.Person person) {
		elmoResource = person;
	}

	public String toJSON(boolean asRecord) {
		StringBuffer json = new StringBuffer();
		if (!asRecord) {
			json.append("{ \"items\": [");
		}
		json.append("{");
		json.append("\"id\":\"" + getQName().getNamespaceURI() + "\",");
		json.append("\"label\":\"" + getQName() + "\",");
		json.append("}");
		if (!asRecord) {
			json.append("]}");
		}
		return json.toString();
	}

	public QName getQName() {
		return null;
	}
	
	public String toString() {
		QName qname = elmoResource.getQName();
		return qname.getNamespaceURI() + qname.getLocalPart();
	}

	public String getGivennames() {
		return elmoResource.getFoafGivennames().toString();
	}

}

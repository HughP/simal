package uk.ac.osswatch.simal.model.elmo;

import javax.xml.namespace.QName;

import uk.ac.osswatch.simal.model.IFoafResourceBehaviour;

public class FoafResourceBehaviour extends ResourceBehavior implements IFoafResourceBehaviour {
  private static final long serialVersionUID = -3852417254318582808L;

  public FoafResourceBehaviour(org.openrdf.concepts.foaf.Person person) {
    super(person);
  }

  /**
   * Create a JSON representation of this person.
   * 
   * @param asRecord
   *          set to true if this is a record within another JSON file or false
   *          if you want this to be a complete JSON file.
   */
  public String toJSON(boolean asRecord) {
    StringBuffer json = new StringBuffer();
    if (!asRecord) {
      json.append("{ \"items\": [");
    }
    json.append("{");
    json.append("\"id\":\"" + elmoEntity.getQName() + "\",");
    json.append("\"label\":\"" + getLabel() + "\",");
    json.append("}");
    if (!asRecord) {
      json.append("]}");
    }
    return json.toString();
  }
  
  public String toString() {
    QName qname = elmoEntity.getQName();
    return qname.getNamespaceURI() + qname.getLocalPart();
  }
}

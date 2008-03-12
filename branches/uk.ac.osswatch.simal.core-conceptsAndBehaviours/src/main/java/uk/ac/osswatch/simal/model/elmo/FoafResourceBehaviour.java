package uk.ac.osswatch.simal.model.elmo;

import java.util.Set;

import org.openrdf.concepts.foaf.Person;
import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.model.IFoafResourceBehaviour;

@rdf("http://xmlns.com/foaf/0.1/Person")
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
  
  private Person getFoafPerson() {
    return (Person)elmoEntity;
  }
  
  /**
   * Get the label for this person. The label for a person is derived
   * from their known names. If the person does not have any defined
   * names then the toString() method is used..
   * 
   * @return
   */
  public String getLabel() {
    Set<Object> givennames = getFoafPerson().getFoafGivennames();
    if (givennames== null) {
      return toString();
    } else {
      return (String)givennames.toArray()[0];
    }
  }
}

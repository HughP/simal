package uk.ac.osswatch.simal.model.jena;

import java.util.Set;

import com.hp.hpl.jena.rdf.model.Statement;

import uk.ac.osswatch.simal.model.Doap;
import uk.ac.osswatch.simal.model.IDoapLicence;
import uk.ac.osswatch.simal.model.IDoapResource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class DoapResource extends Resource implements IDoapResource {

  public DoapResource(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
  }

  public void addName(String name) {
    jenaResource.addLiteral(Doap.NAME, name);
  }

  public String getCreated() {
    return jenaResource.getProperty(Doap.CREATED).getString();
  }

  public String getDescription() {
    return jenaResource.getProperty(Doap.DESCRIPTION).getString().trim();
  }

  public Set<IDoapLicence> getLicences() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getName() {
    return jenaResource.getProperty(Doap.NAME).getString();
  }

  public Set<String> getNames() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getShortDesc() {
    Statement desc = jenaResource.getProperty(Doap.SHORTDESC);
    if ( desc!= null) {
      return desc.getString().trim();
    } else {
      return null;
    }
    
  }

  public void setCreated(String newCreated) throws SimalRepositoryException {
    jenaResource.removeAll(Doap.CREATED);
    jenaResource.addLiteral(Doap.CREATED, newCreated);
  }

  public void setDescription(String newDescription) {
    jenaResource.removeAll(Doap.DESCRIPTION);
    jenaResource.addLiteral(Doap.DESCRIPTION, newDescription);
  }

  public void setShortDesc(String shortDesc) {
    jenaResource.removeAll(Doap.SHORTDESC);
    jenaResource.addLiteral(Doap.SHORTDESC, shortDesc);
  }
  
  public String toString() {
    return getLabel() + " (" + getNames() + ")";
  }


  public String toJSON() throws SimalRepositoryException {
    StringBuffer json = new StringBuffer();
    json.append("{ \"items\": [");
    json.append(toJSONRecord());
    json.append("]}");
    return json.toString();
  }

  public String toJSONRecord() throws SimalRepositoryException {
    StringBuffer json = new StringBuffer();
    json.append("{");
    json.append(toJSONRecordContent());
    json.append("}");
    return json.toString();
  }

  protected String toJSONRecordContent() throws SimalRepositoryException {
    StringBuffer json = new StringBuffer();
    json.append("\"id\":\"" + getURI() + "\",");
    json.append("\"label\":\"" + getLabel() + "\",");
    json.append("\"name\":\"" + getName() + "\",");
    json.append("\"shortdesc\":\"" + getShortDesc() + "\"");
    return json.toString();
  }


}

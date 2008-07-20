package uk.ac.osswatch.simal.model.jena;

import java.util.Set;

import com.hp.hpl.jena.vocabulary.RDF;

import uk.ac.osswatch.simal.model.Doap;
import uk.ac.osswatch.simal.model.IDoapLicence;
import uk.ac.osswatch.simal.model.IDoapResource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class DoapResource extends Resource implements IDoapResource {

  public DoapResource(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
  }

  public void addName(String name) {
    // TODO Auto-generated method stub
    
  }

  public String getCreated() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getDescription() {
    // TODO Auto-generated method stub
    return null;
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
    return jenaResource.getProperty(Doap.SHORTDESC).getString().trim();
  }

  public void setCreated(String newCreated) throws SimalRepositoryException {
    // TODO Auto-generated method stub
    
  }

  public void setDescription(String newDescription) {
    // TODO Auto-generated method stub
    
  }

  public void setShortDesc(String shortDesc) {
    // TODO Auto-generated method stub
    
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

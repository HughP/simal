package uk.ac.osswatch.simal.model.jena;

import java.net.URI;
import java.net.URL;
import java.util.Set;

import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IDoapLicence;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class Category extends Resource implements IDoapCategory {


  public Category(com.hp.hpl.jena.rdf.model.Resource resource) {
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
    // TODO Auto-generated method stub
    return null;
  }

  public Set<String> getNames() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getShortDesc() {
    // TODO Auto-generated method stub
    return null;
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

}

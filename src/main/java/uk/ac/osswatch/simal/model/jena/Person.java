package uk.ac.osswatch.simal.model.jena;

import java.net.URL;
import java.util.Set;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class Person extends Resource implements IPerson {

  public Person(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
  }

  public Set<IPerson> getColleagues() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public String getEmail() {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<String> getGivennames() {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<URL> getHomepages() {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<IPerson> getKnows() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getSimalId() {
    // TODO Auto-generated method stub
    return null;
  }

  public void setSimalId(String newId) {
    // TODO Auto-generated method stub

  }

  public String toJSON(boolean asRecord) {
    // TODO Auto-generated method stub
    return null;
  }

  public String toXML() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

}

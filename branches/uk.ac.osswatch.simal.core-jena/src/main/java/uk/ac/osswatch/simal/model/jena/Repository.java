package uk.ac.osswatch.simal.model.jena;

import java.util.Set;

import uk.ac.osswatch.simal.model.IDoapRepository;

public class Repository extends DoapResource implements IDoapRepository {

  public Repository(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
  }

  public Set<String> getAnonRoots() {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<String> getBrowse() {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<String> getLocations() {
    // TODO Auto-generated method stub
    return null;
  }
}

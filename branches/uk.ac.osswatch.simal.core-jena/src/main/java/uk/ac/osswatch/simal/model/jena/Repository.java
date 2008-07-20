package uk.ac.osswatch.simal.model.jena;

import uk.ac.osswatch.simal.model.IDoapRepository;

public class Repository extends DoapResource implements IDoapRepository {

  public Repository(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
  }
}

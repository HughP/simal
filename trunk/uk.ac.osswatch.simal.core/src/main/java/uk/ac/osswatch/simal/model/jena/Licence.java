package uk.ac.osswatch.simal.model.jena;

import uk.ac.osswatch.simal.model.IDoapLicence;

public class Licence extends DoapResource implements IDoapLicence {

  public Licence(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
  }
}

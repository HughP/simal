package uk.ac.osswatch.simal.model.jena;

import java.util.HashSet;
import java.util.Set;

import com.hp.hpl.jena.rdf.model.StmtIterator;

import uk.ac.osswatch.simal.model.Doap;
import uk.ac.osswatch.simal.model.IDoapRelease;

public class Release extends DoapResource implements IDoapRelease {

  public Release(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
  }

  public Set<String> getRevisions() {
    HashSet<String> revisions = new HashSet<String>();
    StmtIterator statements = jenaResource.listProperties(Doap.REVISION);
    while (statements.hasNext()) {
      revisions.add(statements.nextStatement().getString());
    }
    return revisions;
  }

}

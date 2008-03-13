package uk.ac.osswatch.simal.model.elmo;

import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.model.IDoapRepository;
import uk.ac.osswatch.simal.model.IDoapRepositoryBehaviour;

@rdf("http://usefulinc.com/ns/doap#repository")
public class DoapRepositoryBehaviour extends DoapResourceBehaviour implements IDoapRepositoryBehaviour {

  /**
   * Create a new repository behaviour to operate on a
   * IRepository object.
   */
  public DoapRepositoryBehaviour(IDoapRepository repo) {
    super(repo);
  }

}

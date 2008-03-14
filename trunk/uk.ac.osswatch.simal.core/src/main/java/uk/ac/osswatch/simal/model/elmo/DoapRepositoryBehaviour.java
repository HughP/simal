package uk.ac.osswatch.simal.model.elmo;

import java.util.Set;

import org.openrdf.concepts.doap.Repository;
import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.model.IDoapRepository;
import uk.ac.osswatch.simal.model.IDoapRepositoryBehaviour;

@rdf("http://usefulinc.com/ns/doap#repository")
public class DoapRepositoryBehaviour extends DoapResourceBehaviour implements
    IDoapRepositoryBehaviour {

  /**
   * Create a new repository behaviour to operate on a IRepository object.
   */
  public DoapRepositoryBehaviour(IDoapRepository repo) {
    super(repo);
  }

  /**
   * Get the anonymous access repositories.
   */
  public Set<String> getAnonRoots() {
    return Utilities.convertToSetOfStrings(getRepository().getDoapAnonRoots());
  }

  private Repository getRepository() {
    return (Repository) elmoEntity;
  }

  /**
   * Get the locations for this repository.
   */
  public Set<String> getLocations() {
    return Utilities.convertToSetOfStrings(getRepository().getDoapLocations());
  }

  /**
   * Get the browseble locations for this repository.
   */
  public Set<String> getBrowse() {
    return Utilities.convertToSetOfStrings(getRepository().getDoapBrowse());
  }
}

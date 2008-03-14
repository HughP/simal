package uk.ac.osswatch.simal.model;

import java.util.Set;

/**
 * Behaviours to attach to a repository resource.
 *
 */
public interface IDoapRepositoryBehaviour extends IDoapResourceBehaviour {

  /**
   * Get the anonymous access repositories.
   */
  public Set<String> getAnonRoots();
  
  /**
   * Get the locations for this repository.
   */
  public Set<String> getLocations();

  /**
   * Get the browseble locations for this repository.
   */
  public Set<String> getBrowse();
}

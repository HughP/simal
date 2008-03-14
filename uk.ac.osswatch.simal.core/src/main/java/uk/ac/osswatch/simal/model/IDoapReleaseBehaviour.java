package uk.ac.osswatch.simal.model;

import java.util.Set;

/**
 * Behaviours to attach to a release resource.
 *
 */
public interface IDoapReleaseBehaviour extends IDoapResourceBehaviour {
  
  /**
   * Get all version identifiers for this release. A version
   * identifier is a version number. In DOAP it corresponds to
   * doap:revision.
   */
  public Set<String> getRevisions();
}

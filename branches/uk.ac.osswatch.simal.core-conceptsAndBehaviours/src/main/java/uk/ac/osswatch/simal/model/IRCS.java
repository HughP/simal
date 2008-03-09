package uk.ac.osswatch.simal.model;

import java.util.Set;

/**
 * A wrapper around a repository representation of a source code repository.
 * 
 */
public interface IRCS extends IDoapResource {
  public abstract Set<String> getAnonRoots();

  public abstract Set<String> getBrowse();

  public abstract Set<String> getLocations();
}

package uk.ac.osswatch.simal.model;

import java.net.URL;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * Behaviours to attach to a homepage resource.
 *
 */
public interface IDoapHomepageBehaviour extends IDoapResourceBehaviour{
  
  /**
   * Get the URL for this homepage.
   * @return
   * @throws SimalRepositoryException
   */
  public URL getURL() throws SimalRepositoryException;
}

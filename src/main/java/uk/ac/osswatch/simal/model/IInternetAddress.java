package uk.ac.osswatch.simal.model;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * An email object. Provides methods for accessing 
 * email details.
 */
public interface IInternetAddress {
  
  /**
   * Get the complete email address.
   * @return
   * @throws SimalRepositoryException 
   */
  public String getAddress() throws SimalRepositoryException;
  
}

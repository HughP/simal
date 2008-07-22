package uk.ac.osswatch.simal.model.jena;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.IInternetAddress;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A class for handling emails.
 *
 */
public class InternetAddress extends Resource implements IInternetAddress {
  
  private static final Logger logger = LoggerFactory.getLogger(InternetAddress.class);
  
  public InternetAddress(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
  }

  public String getAddress() throws SimalRepositoryException {
     String url = getURL().toString();
     return url;
  }
  
  public String toString() {
    try {
      return getAddress();
    } catch (SimalRepositoryException e) {
      logger.error("Unable to get email address from the repository", e);
      return ("Invalid Email Address");
    }
  }
}

package uk.ac.osswatch.simal.model;

import java.net.URI;
import java.net.URL;
import java.util.Set;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * An RDF resource.
 *
 */
public interface IResource extends IResourceService {


  /**
   * @deprecated use getURI instead 
   */
  public URL getURL() throws SimalRepositoryException;
  

  /**
   * Get the URI for this resource. Attempts to construct a URI
   * for this resource from the QName. This will often work if a resource
   * is defined by rdf:resource. If it is not possible to create an
   * URI for the resource an exception is thrown.
   * @throws SimalRepositoryException 
   */
  public URI getURI() throws SimalRepositoryException;


  /**
   * Get the label for this resource. If the resource does not have a defined
   * label return value of the toString() method.
   * 
   * @return
   */
  public String getLabel();

  /**
   * Get the label for this resource. If the resource does not have a defined
   * label use the supplied default label (if not null) or the
   * resource return value of the toString() method.
   * 
   * @param defaultLabel
   * @return
   */
  public String getLabel(String defaultLabel);

  /**
   * A human readable comment describing the resource.
   * 
   * @return
   */
  public String getComment();
  

  /**
   * Get all URIs for other descriptions for this
   * resource.
   * 
   * @return
   */
  public Set<URI> getSeeAlso();
}

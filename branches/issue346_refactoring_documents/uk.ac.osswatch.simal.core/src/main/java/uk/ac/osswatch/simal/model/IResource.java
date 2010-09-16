package uk.ac.osswatch.simal.model;

/*
 * 
 Copyright 2007 University of Oxford * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.Set;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * An RDF resource.
 * 
 */
public interface IResource extends IResourceService, Serializable {

  /**
   * @deprecated use getURI instead
   */
  public URL getURL() throws SimalRepositoryException;

  /**
   * Get the URI for this resource.
   * 
   * @throws SimalRepositoryException
   */
  public String getURI();


  /**
   * Set the URI for this resource.
   * 
   * @throws SimalRepositoryException
   */
  public void setURI(String uri);
  
  /**
   * Get the label for this resource. The label of a resource is
   * defined by (in order of priority):
   * 
   * <ul>
   *   <li>rdfs:label</li>
   *   <li>dc:title</li>
   *   <li>URI</li>
   * </ul>
   * 
   * @return
   */
  public String getLabel();

  /**
   * Get the label for this resource. If the resource does not have a defined
   * label use the supplied default label (if not null) or the resource return
   * value of the toString() method.
   * 
   * @param defaultLabel
   * @return
   */
  public String getLabel(String defaultLabel);
  
  /**
   * Set the label to use for this resource. 
   * @param label
   */
  public void setLabel(String label);
  
  /**
   * A human readable comment describing the resource.
   * 
   * @return
   */
  public String getComment();
  
  /**
   * Set the human readable comment describing this resource.
   * @param comment
   */
  public void setComment(String comment);

  /**
   * Get all URIs for other descriptions for this resource.
   * 
   * @return
   */
  public Set<URI> getSeeAlso();
  
  /**
   * Set the URIs for other descriptions of this resource.
   * @param seeAlso
   */
  public void setSeeAlso(Set<URI> seeAlso);
  
  /**
   * Add a seeAlso record to this resource. A seeAlso record points to another record that
   * describes the same entity.
   */
  public void addSeeAlso(URI uri);

  /**
   * Get the raw resource as it is stored in the repository.
   */
  public Object getRepositoryResource();

  /**
   * Set a unique Simal ID for this person. This is a world unique identifier
   * for a Simal resource. It is constructed from the value of simal.instance.id
   * property, followed by ':', followed by the repository unique identifier for
   * the resource. That is:
   * 
   * $simal.instance.id:$simal.resource.id
   * 
   * @see getSimalID()
   */
  public String getUniqueSimalID() throws SimalRepositoryException;

  /**
   * Get the Simal ID for this resource. This is a unique identifier within the
   * repository from which it was retrieved.
   * 
   * Not all resources are given a SimalID.
   * 
   * @return the Simal ID or null if none has been defined
   * @throws SimalRepositoryException
   * @see getUniqueSimalID()
   */
  public String getSimalID() throws SimalRepositoryException;
  
  /**
   * Set the Simal ID for this resource. This is a unique identifier within the
   * repository from which it was retrieved.
   * 
   * @throws SimalRepositoryException
   * 
   */
  public void setSimalID(String newID) throws SimalRepositoryException;

}

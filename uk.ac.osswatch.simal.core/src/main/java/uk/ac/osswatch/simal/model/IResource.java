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
   * @throws SimalRepositoryException 
   */
  public String getURI() throws SimalRepositoryException;
  
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
  
  /**
   * Get the raw resource as it is stored in the repository.
   */
  public Object getRepositoryResource();
}

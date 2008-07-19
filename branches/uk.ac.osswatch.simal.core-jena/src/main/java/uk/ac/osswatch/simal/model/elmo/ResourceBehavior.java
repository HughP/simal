/*
 * Copyright 2007 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.osswatch.simal.model.elmo;

import java.io.StringWriter;

import javax.xml.namespace.QName;

import org.openrdf.concepts.rdfs.Resource;
import org.openrdf.elmo.Entity;
import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.model.IResourceBehaviour;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryFactory;

/**
 * Provides functionality for a Resource object provided by Elmo.
 */
@rdf("http://www.w3.org/2000/01/rdf-schema#Resource")
public class ResourceBehavior implements IResourceBehaviour {
  protected Entity elmoEntity;

  public ResourceBehavior() {
  }

  /**
   * Create a resource behaviour for a given Elmo resource.
   * 
   * @param elmoEntity
   */
  public ResourceBehavior(Resource resource) {
    this.elmoEntity = resource;
  }

  /**
   * Create a resource behaviour for a given Entity.
   * 
   * @param elmoEntity
   */
  public ResourceBehavior(Entity elmoEntity) {
    this.elmoEntity = elmoEntity;
  }

  /**
   * Get the label for this resource. If the resource does not have a defined
   * label return value of the toString() method.
   * 
   * @return
   */
  public String getLabel() {
    return getLabel(null);
  }

  /**
   * Get the label for this resource. If the resource does not have a defined
   * label use the supplied default label (if not null) or the resource return
   * value of the QName in human readable form.
   * 
   * @param defaultLabel
   * @return
   */
  public String getLabel(String defaultLabel) {
    String label = getResource().getRdfsLabel();
    if (label == null) {
      label = defaultLabel;
    }
    if (label == null) {
      QName qname = elmoEntity.getQName();
      label = qname.getNamespaceURI() + qname.getLocalPart();
    }
    return label;
  }

  public String getComment() {
    String comment = getResource().getRdfsComment();
    if (comment == null) {
      comment = "";
    }
    return getResource().getRdfsComment();
  }

  protected Resource getResource() {
    return (Resource) elmoEntity;
  }

  /**
   * Provide a string representation of this category. This is created by
   * calling the getLabel() method.
   */
  public String toString() {
    return getLabel();
  }

  public String toJSON(boolean asRecord) {
    // TODO Auto-generated method stub
    return null;
  }
  
  /**
   * Create an XML representation of this resource.
   * @throws SimalRepositoryException 
   *
   */
  public String toXML() throws SimalRepositoryException {
    StringWriter writer = new StringWriter();
    SimalRepositoryFactory.getInstance().writeXML(writer, elmoEntity.getQName());
    return writer.toString();
  }

  public void delete() throws SimalRepositoryException {
	elmoEntity.getElmoManager().remove(elmoEntity);
  }
}

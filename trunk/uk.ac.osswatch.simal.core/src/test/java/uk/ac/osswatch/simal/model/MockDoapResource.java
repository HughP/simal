/*
 * Copyright 2010 University of Oxford 
 * 
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

package uk.ac.osswatch.simal.model;

import java.net.URI;
import java.net.URL;
import java.util.Set;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * Test class for testing IDoapResources that have nothing to do with the Jena
 * back-end. Expand when needed.
 */
public class MockDoapResource implements IDoapResource {

  private static final long serialVersionUID = 8162751003605007841L;
  
  private String name;
  
  /**
   * Return the name as set
   * 
   * @see uk.ac.osswatch.simal.model.IDoapResource#getName()
   */
  public String getName() {
    return name;
  }

  /**
   * Setter for testing purposes.
   * @param name
   */
  public void setName(String name) {
    this.name = name;
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IDoapResource#addName(java.lang.String)
   */
  public void addName(String name) {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IDoapResource#getCreated()
   */
  public String getCreated() {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IDoapResource#getDescription()
   */
  public String getDescription() {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IDoapResource#getLabel()
   */
  public String getLabel() {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IDoapResource#getLicences()
   */
  public Set<IDoapLicence> getLicences() {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IDoapResource#getNames()
   */
  public Set<String> getNames() {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IDoapResource#getShortDesc()
   */
  public String getShortDesc() {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IDoapResource#removeName(java.lang.String)
   */
  public void removeName(String name) throws SimalRepositoryException {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IDoapResource#setCreated(java.lang.String)
   */
  public void setCreated(String newCreated) throws SimalRepositoryException {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * uk.ac.osswatch.simal.model.IDoapResource#setDescription(java.lang.String)
   */
  public void setDescription(String newDescription) {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * uk.ac.osswatch.simal.model.IDoapResource#setShortDesc(java.lang.String)
   */
  public void setShortDesc(String shortDesc) {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IDoapResource#toJSON()
   */
  public String toJSON() throws SimalRepositoryException {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IDoapResource#toJSONRecord()
   */
  public String toJSONRecord() throws SimalRepositoryException {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IResource#addSeeAlso(java.net.URI)
   */
  public void addSeeAlso(URI uri) {
    // Not currently needed

  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IResource#getComment()
   */
  public String getComment() {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IResource#getLabel(java.lang.String)
   */
  public String getLabel(String defaultLabel) {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IResource#getRepositoryResource()
   */
  public Object getRepositoryResource() {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IResource#getSeeAlso()
   */
  public Set<URI> getSeeAlso() {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IResource#getSimalID()
   */
  public String getSimalID() throws SimalRepositoryException {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IResource#getURI()
   */
  public String getURI() {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IResource#getURL()
   */
  public URL getURL() throws SimalRepositoryException {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IResource#getUniqueSimalID()
   */
  public String getUniqueSimalID() throws SimalRepositoryException {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IResource#setComment(java.lang.String)
   */
  public void setComment(String comment) {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IResource#setLabel(java.lang.String)
   */
  public void setLabel(String label) {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IResource#setSeeAlso(java.util.Set)
   */
  public void setSeeAlso(Set<URI> seeAlso) {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IResource#setSimalID(java.lang.String)
   */
  public void setSimalID(String newID) throws SimalRepositoryException {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IResource#setURI(java.lang.String)
   */
  public void setURI(String uri) {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IResourceService#delete()
   */
  public void delete() throws SimalRepositoryException {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IResourceService#getSources()
   */
  public Set<String> getSources() {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IResourceService#toJSON(boolean)
   */
  public String toJSON(boolean asRecord) {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.osswatch.simal.model.IResourceService#toXML()
   */
  public String toXML() throws SimalRepositoryException {
    // Not currently needed
    throw new UnsupportedOperationException();
  }

}

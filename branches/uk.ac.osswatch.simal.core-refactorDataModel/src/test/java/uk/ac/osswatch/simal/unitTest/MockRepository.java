package uk.ac.osswatch.simal.unitTest;

/*
 * 
 * Copyright 2007 University of Oxford
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

import java.io.Writer;
import java.net.URL;
import java.util.Set;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.rdf.AbstractSimalRepository;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * @FIXME: make this a true MockObject
 * 
 */
public class MockRepository extends AbstractSimalRepository {

  public String getNewPersonID() throws SimalRepositoryException {
    StringBuilder id = new StringBuilder(SimalProperties
        .getProperty(SimalProperties.PROPERTY_SIMAL_INSTANCE_ID));
    id.append(":");
    id.append(Double.toString(Math.random()));
    return id.toString();
  }

  public void add(String data) throws SimalRepositoryException {
    // TODO Auto-generated method stub

  }

  public void addProject(URL url, String baseURI)
      throws SimalRepositoryException {
    // TODO Auto-generated method stub

  }

  public void addRDFXML(URL url, String baseURL)
      throws SimalRepositoryException {
    // TODO Auto-generated method stub

  }

  public boolean containsCategory(String uri) {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean containsPerson(String uri) {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean containsProject(String uri) {
    // TODO Auto-generated method stub
    return false;
  }

  public IPerson createPerson(String uri) throws SimalRepositoryException,
      DuplicateURIException {
    // TODO Auto-generated method stub
    return null;
  }

  public IProject createProject(String uri) throws SimalRepositoryException,
      DuplicateURIException {
    // TODO Auto-generated method stub
    return null;
  }

  public void destroy() throws SimalRepositoryException {
    // TODO Auto-generated method stub

  }

  public IDoapCategory findCategoryById(String id)
      throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public IPerson findPersonById(String id) throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public IPerson findPersonBySeeAlso(String seeAlso)
      throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public IPerson findPersonBySha1Sum(String sha1sum)
      throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public IProject findProjectByHomepage(String homepage)
      throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public IProject findProjectById(String id) throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public IProject findProjectBySeeAlso(String seeAlso)
      throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<IDoapCategory> getAllCategories() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<IPerson> getAllPeople() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public String getAllPeopleAsJSON() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<IProject> getAllProjects() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public String getAllProjectsAsJSON() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public IDoapCategory getCategory(String uri) throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public IPerson getPerson(String uri) throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public IProject getProject(String uri) throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public IResource getResource(String uri) {
    // TODO Auto-generated method stub
    return null;
  }

  public void initialise() throws SimalRepositoryException {
    // TODO Auto-generated method stub

  }

  public void initialise(String directory) throws SimalRepositoryException {
    // TODO Auto-generated method stub

  }

  public void removeAllData() {
    // TODO Auto-generated method stub

  }

  public void writeBackup(Writer writer) {
    // TODO Auto-generated method stub
  }

  public void addRDFXML(Document doc) throws SimalRepositoryException {
    // TODO Auto-generated method stub
    
  }

  public void addPerson(Element sourcePersonRoot)
      throws SimalRepositoryException, DOMException {
    // TODO Auto-generated method stub
    
  }
  
  public void addProject(Element sourceProjectRoot)
      throws SimalRepositoryException, DOMException {
    // TODO Auto-generated method stub
    
  }

  public void addProject(Document doc, URL url, String baseURI)
      throws SimalRepositoryException {
    // TODO Auto-generated method stub
    
  }

}

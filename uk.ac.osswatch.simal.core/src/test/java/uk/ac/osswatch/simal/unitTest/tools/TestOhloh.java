package uk.ac.osswatch.simal.unitTest.tools;
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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.rdf.jena.SimalRepository;

public class TestOhloh extends BaseRepositoryTest {
  static MockOhloh importer;
  String id = "ohlohtest";

  @BeforeClass
  public static void createImporter() {
    importer = new MockOhloh();
  }

  @Test
  public void testImport() throws SimalException {
    ISimalRepository repo = SimalRepository.getInstance();
    String homepage = importer.getProjectPage(id);
    int before = repo.getAllProjects().size();
    importer.addProjectToSimal(id);
    
    int after = repo.getAllProjects().size();
    assertTrue("We haven't added a project when we should", before + 1 == after);
    
    IProject project = repo.findProjectByHomepage(homepage);
    assertNotNull("Can't find a project with the correct homepage", project);
    
    assertEquals("Project name is not correct", "Simal/Ohloh Test", project.getName());
    
    Set<IPerson> developers = project.getDevelopers();
    assertTrue("Project does not appear to have correct developers - has " + developers.toString(), developers.toString().contains("Ross Gardler"));
    
    boolean hasEmail = false;
    Iterator<IPerson> itr = developers.iterator();
    while (itr.hasNext()) {
      IPerson person = itr.next();
      String emailHashes = person.getSHA1Sums().toString();
      if (emailHashes.contains("227ef4ed0d5e6b059c939bdfcf3ac14d117cffbb")) {
        hasEmail = true;
      }
    }
    assertTrue("We don't seem to have details for our expected developer", hasEmail);
    
    project.delete();
  }
  
  @Test
  public void testErrorImport() throws SimalException {
    ISimalRepository repo = SimalRepository.getInstance();
    String projectID = "error";
    int before = repo.getAllProjects().size();
    try {
      importer.addProjectToSimal(projectID);
      fail("We seem to have failed to throw an exception when we are importing an error document");
    } catch (SimalException e) {
      // Good we should have an exception
    }
    
    int after = repo.getAllProjects().size();
    assertTrue("We've added a project when we shouldn't", before == after);
  }
}

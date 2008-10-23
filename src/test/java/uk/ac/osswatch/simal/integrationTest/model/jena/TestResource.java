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
package uk.ac.osswatch.simal.integrationTest.model.jena;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.util.Set;

import org.junit.Test;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestResource extends BaseRepositoryTest {

  public TestResource() throws SimalRepositoryException {
    super();
  }

  @Test
  public void testDeleteEntity() throws SimalRepositoryException,
      URISyntaxException {
    assertNotNull(project1);
    project1.delete();
    IProject project = getRepository().findProjectBySeeAlso(
        TEST_PROJECT_SEEALSO);
    assertNull("The simal project should have been deleted", project);
    // force the repo to be rebuilt
    getRepository().destroy();
    initRepository();
  }

  @Test
  public void testToXML() throws SimalRepositoryException {
    String xml = project1.toXML();
    assertNotNull(xml);
  }

  @Test
  public void testSerialisation() throws IOException, ClassNotFoundException,
      SimalRepositoryException {
    String tmpDir = System.getProperty("java.io.tmpdir");
    String filename = tmpDir + File.separator + "SimalSerialisationTest";
    FileOutputStream fos = new FileOutputStream(filename);
    ObjectOutputStream out = new ObjectOutputStream(fos);
    out.writeObject(project1);
    out.close();

    FileInputStream fis = new FileInputStream(filename);
    ObjectInputStream in = new ObjectInputStream(fis);
    IProject project = (IProject) in.readObject();
    in.close();

    assertNotNull(project);
    assertEquals("URI after serialisation is not the same", project.getURI(),
        project1.getURI());
    assertEquals("Label after serialisation is not the same", project
        .getLabel(), project1.getLabel());
  }

  @Test
  public void testSources() throws SimalRepositoryException {
    Set<String> sources = project1.getSources();
    assertEquals("Incorrect number of data sources identified", 4, sources
        .size());
  }

}

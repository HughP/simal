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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestSimalProperties {

  @Test
  public void testDefaults() throws SimalRepositoryException {
    assertEquals("false", SimalProperties
        .getProperty(SimalProperties.PROPERTY_TEST));
    String dataDir = System.getProperty("user.dir");;
    assertEquals(dataDir, SimalProperties
        .getProperty(SimalProperties.PROPERTY_RDF_DATA_DIR));
  }

  @Test
  public void testLocal() throws FileNotFoundException, IOException,
      SimalRepositoryException {
    // first make sure a local.simal.properties file exists by changing a
    // value and saving the properties file
    SimalProperties.setProperty(SimalProperties.PROPERTY_UNIT_TEST, "local");
    SimalProperties.save();
    assertEquals(
        "This test will fail locally unless you have set up a local.simal.properties file",
        "local", SimalProperties
            .getProperty(SimalProperties.PROPERTY_UNIT_TEST));
  }

  @Test
  public void testGetInstanceId() throws SimalRepositoryException {
    String id = SimalProperties
        .getProperty(SimalProperties.PROPERTY_SIMAL_INSTANCE_ID);
    assertNotNull("didn't get an instance id", id);
  }

  @Test
  public void testLocalFile() throws URISyntaxException,
      SimalRepositoryException {
    String version = "Simal-UnitTests";
    URL propertiesURL = TestSimalProperties.class.getClassLoader().getResource("testData/local.unitTest.properties");
    assertNotNull("Can't find the properties file for tests", propertiesURL);
    File propsFile = new File(propertiesURL.toURI());
    SimalProperties.setLocalPropertiesFile(propsFile);
    assertEquals("We don't seem to be using the custom properties file",
        version, SimalProperties
            .getProperty(SimalProperties.PROPERTY_SIMAL_VERSION));
  }

}

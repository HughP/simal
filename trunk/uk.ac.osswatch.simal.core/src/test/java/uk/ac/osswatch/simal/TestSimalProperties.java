package uk.ac.osswatch.simal;

/*
 * 
 * Copyright 2007,2010 University of Oxford
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.integrationTest.model.repository.BaseRepositoryTest;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestSimalProperties extends BaseRepositoryTest {

  private static final String NON_EXISTENT_PROPERTY = "qoielcawqq";
  private static final String DEFAULT_VALUE = "qwoeruqwersad";

  private static final Logger LOGGER = LoggerFactory
      .getLogger(TestSimalProperties.class);
  private static final String USER_HOME = "user.home";

  @Test
  public void testSimalDefaultLocation() {
    String simalHome = System.getenv(SimalProperties.SIMAL_HOME);
    String expectedDataDir = null;
    if (simalHome != null && !"".equals(simalHome)
        && new File(simalHome).listFiles() != null) {
      LOGGER.info("Testing with SIMAL_HOME set to " + simalHome);
      expectedDataDir = simalHome;
    } else {
      LOGGER.info("Testing without SIMAL_HOME set.");
      expectedDataDir = System.getProperty("user.home");
    }
    try {
      String rdfDataDir = SimalProperties
          .getProperty(SimalProperties.PROPERTY_RDF_DATA_DIR);
      assertEquals(expectedDataDir, rdfDataDir);
    } catch (SimalRepositoryException e) {
      fail("Could not read property " + SimalProperties.PROPERTY_RDF_DATA_DIR
          + "; error: " + e.getMessage());
    }
  }

  @Test
  public void testUnwritableDir() {
    String oldUserHome = System.getProperty(USER_HOME);
    System.setProperty(USER_HOME, "C:\\non\\existing\\path");

    try {
      SimalProperties.getProperty(SimalProperties.PROPERTY_RDF_DATA_DIR);
      System.setProperty(USER_HOME, oldUserHome);
      fail("Reading property " + SimalProperties.PROPERTY_RDF_DATA_DIR
          + " should have failed.");
    } catch (Exception e) {
      if (!(e instanceof SimalRepositoryException)) {
        fail("Wrong exception thrown : " + e.getMessage());
      }
      System.setProperty(USER_HOME, oldUserHome);
    }
  }

  @Test
  public void testNonExistentProperties() {
    assertEquals(
        "Requesting non-existent property should return given default value.",
        DEFAULT_VALUE, SimalProperties.getProperty(NON_EXISTENT_PROPERTY,
            DEFAULT_VALUE));

    try {
      String value = SimalProperties.getProperty(NON_EXISTENT_PROPERTY);
      fail("Requesting non-existent property should result in exception but returned '"
          + value + "'.");
    } catch (SimalRepositoryException e) {
      assertTrue("Resulting exception should be about non-existent property.",
          (e.getMessage().indexOf(NON_EXISTENT_PROPERTY) > -1));
    }
  }

  @Test
  public void testDefaults() throws SimalRepositoryException {
    assertEquals("false", SimalProperties
        .getProperty(SimalProperties.PROPERTY_TEST));
    String dataDir = System.getProperty("user.home");
    ;
    assertEquals(dataDir, SimalProperties
        .getProperty(SimalProperties.PROPERTY_RDF_DATA_DIR));
  }

  @Test
  public void testSimalID() throws SimalRepositoryException {
    String instanceID = SimalProperties
        .getProperty(SimalProperties.PROPERTY_SIMAL_INSTANCE_ID);
    assertEquals("Got the wrong simal instance ID", "simal:test", instanceID);
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
    URL propertiesURL = TestSimalProperties.class.getClassLoader().getResource(
        "testData/local.unitTest.properties");
    assertNotNull("Can't find the properties file for tests", propertiesURL);
    File propsFile = new File(propertiesURL.toURI());
    SimalProperties.setLocalPropertiesFile(propsFile);
    assertEquals("We don't seem to be using the custom properties file",
        version, SimalProperties
            .getProperty(SimalProperties.PROPERTY_SIMAL_VERSION));
  }

}

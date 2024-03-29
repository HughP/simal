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

package uk.ac.osswatch.simal.model.jena.simal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.model.jena.simal.JenaDatabaseSupport.JenaDatabaseType;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

import com.hp.hpl.jena.db.ModelRDB;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.impl.ModelCom;

/**
 * Test all supporting databases of JenaDatabaseSupport.
 */
public class TestJenaDatabaseSupport {
  public static final Logger LOGGER = LoggerFactory
      .getLogger(JenaSimalRepository.class);

  private static final String TEST_DIR = System.getProperty("java.io.tmpdir");

  @AfterClass
  public static void removeTestDatabases() {
    deleteTestDatabase("SDB", TEST_DIR);
    deleteTestDatabase("TDB", TEST_DIR);
    deleteTestDatabase("RDB", TEST_DIR);
  }

  /**
   * Make sure the database is deleted when test is completed.
   */
  private static void deleteTestDatabase(String dbType, String directory) {
    try {
      String dbPath = JenaDatabaseSupport.constructDatabasePath(directory,
          dbType);
      File dbFolder = new File(dbPath);
      try {
        FileUtils.deleteDirectory(dbFolder);
      } catch (IOException e) {
        // TODO For now ignore failure of deleting test db; Many times some db
        // files are still locked, it's not clear how to explicitly disconnect
        // from the db. For now we delete the test folder additionally before
        // running the tests.
        LOGGER.info("Test failed to delete database from path " + dbFolder);
      }
    } catch (SimalRepositoryException e) {
      fail("Could not contruct database path from directory " + directory);
    }
  }
  
  @Test
  public void testDatabaseInitialisationSDB() {
    assertEquals(null, JenaDatabaseSupport.getDatabasePath());
    performDatabaseInitialisationTest("SDB", ModelCom.class);
  }

  @Test
  public void testDatabaseInitialisationTDB() throws SimalRepositoryException {
    performDatabaseInitialisationTest("TDB", ModelCom.class);
    assertEquals(TEST_DIR
            + System.getProperty("file.separator")
            + SimalProperties.getProperty(SimalProperties.PROPERTY_RDF_DATA_FILENAME)
            + "_TDB", JenaDatabaseSupport.getDatabasePath());
  }

  @Test
  public void testDatabaseInitialisationRDB() {
    performDatabaseInitialisationTest("RDB", ModelRDB.class);
  }

  private void performDatabaseInitialisationTest(String dbType,
      Class<? extends Model> expectedClass) {
    deleteTestDatabase(dbType, TEST_DIR);
    JenaDatabaseSupport jenaDatabaseSupport = new JenaDatabaseSupport();
    try {
      Model model = jenaDatabaseSupport.initialiseDatabase(dbType, TEST_DIR);
      assertEquals(expectedClass, model.getClass());
      assertEquals(JenaDatabaseType.valueOf(dbType), JenaDatabaseSupport.getType());
      jenaDatabaseSupport.removeAllData(model);
      model.close();
      model = null;
    } catch (SimalRepositoryException e) {
      fail("Unexpectedly failed init");
    }
  }
}

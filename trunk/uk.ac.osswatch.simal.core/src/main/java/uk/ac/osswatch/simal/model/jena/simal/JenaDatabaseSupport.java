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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.sdb.StoreDesc;
import com.hp.hpl.jena.sdb.sql.SDBConnectionDesc;
import com.hp.hpl.jena.sdb.sql.SDBExceptionSQL;
import com.hp.hpl.jena.sdb.store.DatabaseType;
import com.hp.hpl.jena.sdb.store.LayoutType;
import com.hp.hpl.jena.tdb.TDBFactory;

/**
 * Helper for Jena repository functions. Performs
 * all database type specific operations. Supports
 * types RDB, SDB and TDB. 
 */
public class JenaDatabaseSupport {
  public static final Logger LOGGER = LoggerFactory
      .getLogger(JenaSimalRepository.class);

  /**
   * Token for the type of database; determined at
   * runtime. These are all the database types that
   * Jena supports. 
   */
  public static enum JenaDatabaseType {
    RDB, SDB, TDB;
  };

  private JenaDatabaseType type = null;
  
  private StoreDesc cachedStoreDesc = null;
  
  /**
   * Intialise the Jena database with the specific type 
   * and store the database in the directory specified.
   * @param valueOf
   * @param directory
   * @throws SimalRepositoryException
   */
  public Model initialiseDatabase(String dbType, String directory)
      throws SimalRepositoryException {
    Model model = null;

    try {
      type = JenaDatabaseType.valueOf(dbType);
    } catch (IllegalArgumentException e) {
      LOGGER.warn("Illegal dbType " + dbType + "; using RDB");
      type = JenaDatabaseType.RDB;
    }

    switch (type) {
    case SDB:
      Store store = initialiseSDBStore(directory, dbType);
      model = SDBFactory.connectDefaultModel(store);
      //model = SDBFactory.connectDefaultModel(generateStoreDescSdb());
      break;
    case TDB:
      String tdbUrl = constructDatabasePath(directory, dbType);
      LOGGER.debug("Creating TDB database with URL " + tdbUrl);
      model = TDBFactory.createModel(tdbUrl);

      break;
    case RDB:
    default:
      model = initialiseRdbModel(directory, dbType);
    }

    return model;
  }

  /**
   * Specific initialisation for RDB.
   * @param directory
   * @param dbType
   * @return
   * @throws SimalRepositoryException
   */
  private Model initialiseRdbModel(String directory, String dbType)
      throws SimalRepositoryException {
    initialiseDerbyDatabase();

    String jdbcUrl = "jdbc:derby:" + constructDatabasePath(directory, dbType)
        + ";create=true";
    String jdbcUser = "";
    String jdbcPassword = "";
    String jdbcType = "Derby";

    // Create database connection
    LOGGER.debug("Creating RDB database with URL " + jdbcUrl);
    IDBConnection conn = new DBConnection(jdbcUrl, jdbcUser, jdbcPassword, jdbcType);
    ModelMaker maker = ModelFactory.createModelRDBMaker(conn);

    // create or open the default model
    Model model = maker.createDefaultModel(); 

    return model;
  }

  /** 
   * Generates the Store description that contains all 
   * parameters needed to connect to the store. Only used
   * for SDB; result can be cached to reconnect to the store.
   * @param dbUrl
   * @return
   */
  private StoreDesc generateStoreDescSdb(String dbUrl) {
    StoreDesc storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesHash, DatabaseType.Derby);
    
    SDBConnectionDesc sdbConnDesc = SDBConnectionDesc.blank();
    sdbConnDesc.setJdbcURL(dbUrl);
    sdbConnDesc.setUser("");
    sdbConnDesc.setPassword("");
    
    storeDesc.connDesc = sdbConnDesc;

    return storeDesc; 
  }

  /**
   * Specific initialisation for the SDB store. The store
   * is used for Jena databases of type SDB to connect to
   * or generate a Model.
   * 
   * @param directory
   * @return
   * @throws SimalRepositoryException
   */
  private Store initialiseSDBStore(String directory, String dbType)
      throws SimalRepositoryException {
    initialiseDerbyDatabase();

    String dbUrl = "jdbc:derby:" + constructDatabasePath(directory, dbType)
        + ";create=true";
    StoreDesc storeDesc = generateStoreDescSdb(dbUrl);
    Store store = SDBFactory.connectStore(storeDesc);

    try {
      store.getSize();
      LOGGER.info("Connected to existing SDB store");
    } catch (SDBExceptionSQL e) {
      LOGGER.info("Creating new SDB store because it does not exist;");
      store.getTableFormatter().format();
    }
    this.cachedStoreDesc = storeDesc;
    return store;
  }

  /**
   * Constructs the database path; this is used for all types
   * of Jena databases.
   * @param directory
   * @param dbType
   * @return
   * @throws SimalRepositoryException
   */
  protected static String constructDatabasePath(String directory, String dbType)
      throws SimalRepositoryException {
    StringBuffer databasePath = new StringBuffer();

    if (directory != null && !directory.equals("")) {
      databasePath.append(directory);
    } else {
      databasePath.append(SimalProperties
          .getProperty(SimalProperties.PROPERTY_RDF_DATA_DIR));
    }

    databasePath.append(System.getProperty("file.separator"));
    databasePath.append(SimalProperties
        .getProperty(SimalProperties.PROPERTY_RDF_DATA_FILENAME));
    databasePath.append('_');
    databasePath.append(dbType);

    return databasePath.toString();
  }

  /**
   * Simple initialiser to make sure the Derby driver is loaded.
   * @throws SimalRepositoryException
   */
  private static void initialiseDerbyDatabase() throws SimalRepositoryException {
    String className = "org.apache.derby.jdbc.EmbeddedDriver";

    try {
      Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new SimalRepositoryException("Unable to find derby driver", e);
    }
  }

  /**
   * Removes all the data from the model. Performs the deletion
   * specific to the type of database, as determined at runtime.
   * @param model
   */
  public Model removeAllData(Model model) {
    Model newModel = null;

    switch (type) {
    case SDB:
      Store store = SDBFactory.connectStore(cachedStoreDesc);
      store.getTableFormatter().truncate();
      newModel = SDBFactory.connectDefaultModel(store);
      break;
    case TDB:
    case RDB:
    default:
      model.removeAll();
      newModel = model;
    }

    return newModel;
  }

}

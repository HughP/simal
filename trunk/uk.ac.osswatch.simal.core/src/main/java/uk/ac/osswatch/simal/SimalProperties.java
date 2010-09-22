/*
 * Copyright 2007-2010 University of Oxford
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
package uk.ac.osswatch.simal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * For accessing the properties of a Simal Repository.
 * 
 */
public class SimalProperties {
  private static final Logger logger = LoggerFactory
      .getLogger(SimalProperties.class);

  private static final String DEFAULT_PROPERTIES_FILE = "default.simal.properties";

  public static final String PROPERTY_RDF_DATA_DIR = "simal.repository.dir";
  public static final String PROPERTY_RDF_BACKUP_DIR = "simal.repository.backup.dir";
  public static final String PROPERTY_RDF_DATA_FILENAME = "simal.repository.filename";
  public static final String PROPERTY_TEST = "simal.test";
  public static final String PROPERTY_SIMAL_INSTANCE_ID = "simal.instance.id";
  public static final String PROPERTY_SIMAL_VERSION = "simal.version";
  public static final String PROPERTY_SIMAL_NEXT_BUG_DATABASE_ID = "simal.nextBugDatabaseID";
  public static final String PROPERTY_SIMAL_NEXT_CATEGORY_ID = "simal.nextCategoryID";
  public static final String PROPERTY_SIMAL_NEXT_HOMEPAGE_ID = "simal.nextProjectID";
  public static final String PROPERTY_SIMAL_NEXT_ORGANISATION_ID = "simal.nextOrganisatonID";
  public static final String PROPERTY_SIMAL_NEXT_PROJECT_ID = "simal.nextProjectID";
  public static final String PROPERTY_SIMAL_NEXT_PERSON_ID = "simal.nextPersonID";
  public static final String PROPERTY_SIMAL_NEXT_RCS_ID = "simal.nextRCSID";
  public static final String PROPERTY_SIMAL_NEXT_REVIEW_ID = "simal.nextReviewID";
  public static final String PROPERTY_SIMAL_NEXT_RELEASE_ID = "simal.nextReleaseID";
  public static final String PROPERTY_SIMAL_DOAP_FILE_STORE = "simal.doap.filestore";
  public static final String PROPERTY_SIMAL_DB_TYPE = "simal.db.type";

  public static final String PROPERTY_LOCAL_PROPERTIES_LOCATION = "simal.local.properties.path";

  public static final String PROPERTY_UNIT_TEST = "simal.unitTest";

  public static final String PROPERTY_REST_BASEURL = "simal.rest.baseurl";

  public static final String PROPERTY_USER_WEBAPP_BASEURL = "simal.user.webapp.baseurl";

  public static final String PROPERTY_ADD_PROJECT_WIDGET_TITLE = "simal.widget.addproject.name";

  public static final String PROPERTY_OHLOH_API_KEY = "ohloh.api.key";

  public static final String PROPERTY_GOOGLE_AJAX_FEED_API_KEY = "google.ajax.feed.api.key";

  public static final String PROPERTY_SIMAL_HOMEPAGELABELS_PATH = "simal.homepagelabels.filename";

  protected static final String SIMAL_HOME = "SIMAL_HOME";

  private static Properties defaultProps;
  private static Properties localProps;

  private static File propsFile;

  static {
    try {
      initProperties();
    } catch (SimalRepositoryException e) {
      logger.error(e.getMessage());
    }
  }

  public SimalProperties() throws SimalRepositoryException {
    initProperties();
  }

  private static String getDefaultFileLocation() {
    String fileLocation = System.getenv(SIMAL_HOME);

    if (fileLocation != null && !"".equals(fileLocation)) {
      File testFile = new File(fileLocation);
      if (!testFile.isDirectory() || testFile.listFiles() == null) {
        String msg = "Default directory " + fileLocation
            + " from environment variable " + SIMAL_HOME + " is not writable.";
        logger.warn(msg);
        fileLocation = null;
      }
    }

    if (fileLocation == null || "".equals(fileLocation)) {
      fileLocation = System.getProperty("user.home");
      logger.info("Generated default file location " + fileLocation + " from system property user.home");
    }

    if (fileLocation != null && !"".equals(fileLocation)) {
      File testFile = new File(fileLocation);
      if (!testFile.isDirectory() || testFile.listFiles() == null) {
        String msg = "Generated default directory " + fileLocation
            + " is not writable.";
        logger.warn(msg);
        fileLocation = null;
      }
    }

    return fileLocation;
  }

  /**
   * Initialise the properties by reading the default properties file.
   * 
   * @throws SimalRepositoryException
   */
  protected static void initProperties() throws SimalRepositoryException {
    URL defaultsLocation = null;
    try {
      defaultsLocation = SimalProperties.class.getClassLoader().getResource(
          DEFAULT_PROPERTIES_FILE);
      defaultProps = new Properties();
      defaultProps.load(defaultsLocation.openStream());
    } catch (Exception e) {
      String msg = "Unable to load default properties file from "
          + defaultsLocation;
      throw new SimalRepositoryException(msg, e);
    }

    File propsFile = getLocalPropertiesFile();
    if (propsFile.exists()) {
      localProps = getProperties(propsFile);
    } else {
      logger.debug("No local.simal.properties file was found");
      localProps = new Properties();
    }
    localProps.putAll(System.getProperties());
  }

  /**
   * Get the location of the local properties file.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public static File getLocalPropertiesFile() throws SimalRepositoryException {
    if (propsFile == null) {
      String workingDir = getDefaultFileLocation();
      propsFile = new File(workingDir + File.separator
          + getProperty(PROPERTY_LOCAL_PROPERTIES_LOCATION));
    }
    return propsFile;
  }

  /**
   * Delete the local properties file.
   * 
   * @return true is deleted, otherwise false
   * @throws SimalRepositoryException
   */
  public static boolean deleteLocalProperties() throws SimalRepositoryException {
    return getLocalPropertiesFile().delete();
  }

  /**
   * Get a properties file from a given location.
   */
  private static Properties getProperties(File propsFile)
      throws SimalRepositoryException {
    Properties props = new Properties();
    InputStream in = null;
    try {
      in = new FileInputStream(propsFile);
      props.load(in);
    } catch (IOException e) {
      throw new SimalRepositoryException("Unable to load properties file: "
          + propsFile, e);
    } finally {
      try {
        if (in != null) {
          in.close();
        }
      } catch (IOException e) {
        throw new SimalRepositoryException("Unable to close properties file: "
            + propsFile, e);
      }
    }
    return props;
  }

  /**
   * Get the default property value for the supplied key if there is a mechanism
   * for the specified key to determine one.
   * 
   * @param key
   * @return default value for key or null if no default is registered.
   */
  private static String getDefaultPropertyValue(String key) {
    String value = null;

    if (key.equals(PROPERTY_SIMAL_INSTANCE_ID)) {
      try {
        if (SimalRepositoryFactory.getInstance().isTest()) {
          value = "simal:test";
        } else {
          value = UUID.randomUUID().toString();
        }
      } catch (SimalRepositoryException e) {
        value = UUID.randomUUID().toString();
      }
      setProperty(PROPERTY_SIMAL_INSTANCE_ID, value);
    } else if (key.equals(PROPERTY_RDF_DATA_DIR)) {
      value = getDefaultFileLocation();
    } else if (key.equals(PROPERTY_RDF_BACKUP_DIR)) {
      value = getDefaultFileLocation();
      if (!value.endsWith(File.separator)) {
        value += File.separator;
      }
      value += "backup";
    }

    return value;
  }

  /**
   * Get a property value. Will check for an internally registered default value
   * if now value can be found.
   * 
   * @param key
   *          the name of the property value to retrieve
   * @throws SimalRepositoryException
   *           if the properties cannot be initialised or no value can be found.
   */
  public static String getProperty(String key) throws SimalRepositoryException {
    String value = getProperty(key, null);

    if (value == null) {
      value = getDefaultPropertyValue(key);

      if (value == null) {
        StringBuilder sb = new StringBuilder("The property '");
        sb.append(key);
        sb.append("' has not been set in either local.simal.properties, ");
        sb
            .append("default.simal.properties files or the SimalProperties class");

        String msg = sb.toString();
        logger.warn(msg);

        throw new SimalRepositoryException(msg);
      }
    }

    return value;
  }

  /**
   * Get a property value from the local properties file. If there is no value
   * from the local properties it will get it from the default properties file.
   * If there is no value from the default properties file it will return the
   * supplied default.
   * 
   * @param key
   * @param defaultValue
   * @return
   */
  public static String getProperty(String key, String defaultValue) {
    if (defaultProps == null) {
      try {
        initProperties();
      } catch (SimalRepositoryException e) {
        logger.error(e.getMessage());
        return defaultValue;
      }
    }

    String value = null;
    if (localProps != null) {
      value = localProps.getProperty(key);
    }

    if (value == null) {
      value = defaultProps.getProperty(key, defaultValue);
    }

    return (value != null) ? value : defaultValue;
  }

  /**
   * Set a property
   * 
   * @param key
   * @param value
   */
  public static void setProperty(String key, String value) {
    if (localProps == null) {
      localProps = new Properties();
    }
    localProps.setProperty(key, value);
    logger.info("{} set to {}", new String[] { key, value });
  }

  /**
   * Save any properties that have either been loaded from the the local
   * properties file or have been changed in this run.
   * 
   * @throws SimalRepositoryException
   */
  public static void save() throws SimalRepositoryException {
    String comments = "Simal Properties";
    File propsFile = getLocalPropertiesFile();
    FileOutputStream out = null;
    try {
      if (!propsFile.exists()) {
        boolean created = propsFile.createNewFile();
        if (!created) {
          throw new SimalRepositoryException(
              "Unable to create properties file: " + propsFile.toString());
        }
      }
      out = new FileOutputStream(propsFile);
      localProps.store(out, comments);
    } catch (IOException e) {
      throw new SimalRepositoryException("Failed to save properties file to "
          + propsFile, e);
    } finally {
      try {
        if (out != null) {
          out.close();
        }
      } catch (IOException e) {
        throw new SimalRepositoryException("Unable to close properties file: "
            + propsFile, e);
      }
    }
  }

  /**
   * Get a property as an Integer.
   * 
   * @param name
   * @return
   * @throws SimalRepositoryException
   *           The properties cannot be initialised or the property value is not
   *           an Integer.
   */
  public static Integer getIntegerProperty(String name)
      throws NumberFormatException, SimalRepositoryException {
    return Integer.valueOf(getProperty(name));
  }

  /**
   * Set the location of the local properties file.
   * 
   * @param file
   * @throws SimalRepositoryException
   */
  public static void setLocalPropertiesFile(File file)
      throws SimalRepositoryException {
    propsFile = file;
    initProperties();
  }
}

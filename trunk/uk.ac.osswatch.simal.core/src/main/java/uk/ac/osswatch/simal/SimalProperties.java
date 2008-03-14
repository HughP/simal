package uk.ac.osswatch.simal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

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
  public static final String PROPERTY_DATA_DIR = "simal.repository.dir";
  public static final String PROPERTY_TEST = "simal.test";
  public static final String PROPERTY_SIMAL_VERSION = "simal.version";
  public static final String PROPERTY_SIMAL_NEXT_PROJECT_ID = "simal.nextProjectID";

  public static final String PROPERTY_LOCAL_PROPERTIES_LOCATION = "simal.local.properties.path";

  public static final String PROPERTY_UNIT_TEST = "simal.unitTest";

  private static Properties defaultProps;
  private static Properties localProps;

  public SimalProperties() throws SimalRepositoryException {
    initProperties();
  }

  /**
   * Initialise the properties by reading the default properties file.
   * 
   * @throws SimalRepositoryException
   */
  public void initProperties() throws SimalRepositoryException {
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
  }

  /**
   * Get the location of the local properties file.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  private static File getLocalPropertiesFile() throws SimalRepositoryException {
    File propsFile;
    String workingDir = System.getProperty("user.dir");
    propsFile = new File(workingDir + File.separator
        + getProperty(PROPERTY_LOCAL_PROPERTIES_LOCATION));
    return propsFile;
  }

  /**
   * Get a properties file from a given location.
   */
  private Properties getProperties(File propsFile)
      throws SimalRepositoryException {
    Properties props = new Properties();
    try {
      InputStream in = new FileInputStream(propsFile);
      props.load(in);
    } catch (Exception e) {
      throw new SimalRepositoryException("Unable to load properties file: "
          + propsFile, e);
    }
    return props;
  }

  /**
   * Get a property value
   * 
   * @param key
   *          the name of the property value to retrieve
   */
  public static String getProperty(String key) {
    return getProperty(key, null);
  }

  /**
   * Get a property value. If no value is available then return the supplied
   * default.
   * 
   * @param key
   * @param default
   * @return
   */
  public static String getProperty(String key, String defaultValue) {
    String value = null;
    if (localProps != null) {
      value = localProps.getProperty(key);
    }
    if (value == null) {
      value = defaultProps.getProperty(key, defaultValue);
    }
    if (value == null) {
      value = "The property '"
          + key
          + "' has not been set in either local.simal.properties, default.simal.properties files or the SimalProperties class";
      logger.warn(value);
    }
    return value;
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
    try {
      if (!propsFile.exists()) {
        propsFile.createNewFile();
      }
      localProps.store(new FileOutputStream(propsFile), comments);
    } catch (IOException e) {
      throw new SimalRepositoryException("Failed to save properties file to "
          + propsFile, e);
    }
  }
}

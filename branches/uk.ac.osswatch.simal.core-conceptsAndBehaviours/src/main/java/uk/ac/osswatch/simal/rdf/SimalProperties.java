package uk.ac.osswatch.simal.rdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

  private static Properties props;

  public SimalProperties() throws SimalRepositoryException {
    initProperties();
  }

  /**
   * Initialise the properties by reading the default properties file.
   * 
   * @throws SimalRepositoryException
   */
  public void initProperties() throws SimalRepositoryException {
    props = new Properties();
    URL defaultLocation = getPropertiesFileURL();
    if (defaultLocation == null) {
      throw new SimalRepositoryException(
          "Unable to find the default properties file");
    }
    try {
      props.load(defaultLocation.openStream());
    } catch (Exception e) {
      throw new SimalRepositoryException("Unable to load default properties", e);
    }
  }

  private static URL getPropertiesFileURL() {
    return SimalRepository.class.getClassLoader().getResource(
        DEFAULT_PROPERTIES_FILE);
  }

  /**
   * Get a property value
   * 
   * @param property
   *          the name of the property value to retrieve
   */
  public String getProperty(String property) {
    return props.getProperty(property, "The property '" + property
        + "' has not been set");
  }

  /**
   * Get a property value. If no value is available then return the supplied
   * default.
   * 
   * @param key
   * @param default
   * @return
   */
  public static String getProperty(String key, String value) {
    return props.getProperty(key, value);
  }

  /**
   * Set a property
   * 
   * @param key
   * @param value
   */
  public static void setProperty(String key, String value) {
    props.setProperty(key, value);
  }

  public static void save() throws FileNotFoundException, IOException {
    String comments = "Simal Properties";
    File propsFile;
    try {
      propsFile = new File(getPropertiesFileURL().toURI());
      if (!propsFile.exists()) {
        propsFile.createNewFile();
      }
      props.store(new FileOutputStream(propsFile), comments);
    } catch (URISyntaxException e) {
      // Should never happen as we have a system generated URL
      logger.error("Unable to save properties", e);
    }
  }
}

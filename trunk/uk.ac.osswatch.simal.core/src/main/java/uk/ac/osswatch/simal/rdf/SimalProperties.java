package uk.ac.osswatch.simal.rdf;

import java.net.URL;
import java.util.Properties;

/**
 * For accessing the properties of a Simal Repository.
 * 
 */
public class SimalProperties {
	private static final String DEFAULT_PROPERTIES_FILE = "default.simal.properties";
	public static final String PROPERTY_DATA_DIR = "simal.repository.dir";
	public static final String PROPERTY_TEST = "simal.test";
	public static final String PROPERTY_SIMAL_VERSION = "simal.version";

	private Properties props;

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
		URL defaultLocation = SimalRepository.class.getClassLoader().getResource(
				DEFAULT_PROPERTIES_FILE);
		if (defaultLocation == null) {
			throw new SimalRepositoryException("Unable to find the default properties file");
		}
		try {
			props.load(defaultLocation.openStream());
		} catch (Exception e) {
			throw new SimalRepositoryException(
					"Unable to load default properties", e);
		}
	}

	/**
	 * Get a property value
	 * 
	 * @param property
	 *            the name of the property value to retrieve
	 */
	public String getProperty(String property) {
		return props.getProperty(property, "The property '" + property
				+ "' has not been set");
	}
}

package uk.ac.osswatch.simal.model;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A licence under which a outputs may be released.
 *
 */
public class Licence implements Serializable {
	private static final long serialVersionUID = -6862699160551265257L;

	private String resourceURL;

	public Licence() {
	}
	
	public Licence(URL url) {
		setResourceURL(url);
	}


	/**
	 * Get the rdf resource this licence refers to.
	 * @param resourceURL
	 */
	public String getResourceURL() {
		return resourceURL;
	}

	/**
	 * Set the rdf resource this licence refers to.
	 * @param resourceURL
	 */
	public void setResourceURL(URL resourceURL) {
		this.resourceURL = resourceURL.toExternalForm();
	}
	
	/**
	 * Set the rdf resource this category refers to.
	 * @param resourceURL
	 */
	public void setResourceURL(String url) throws MalformedURLException {
		setResourceURL(new URL(url));
	}
	
}

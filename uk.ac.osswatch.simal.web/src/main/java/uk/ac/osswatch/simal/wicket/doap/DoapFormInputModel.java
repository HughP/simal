package uk.ac.osswatch.simal.wicket.doap;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.wicket.IClusterable;

/**
 * An input model for managing a DOAP form.
 * 
 */
public class DoapFormInputModel implements IClusterable {
	private static final long serialVersionUID = -9089647575258232806L;
	private URL sourceURL;

	public DoapFormInputModel() {
		try {
			sourceURL = new URL("http://wicket.apache.org");
		} catch (MalformedURLException e) {
			// can't happen since URL is hard coded
			e.printStackTrace();
		}
	}

	/**
	 * Get the source URL for the data in this DOAP form.
	 */
	public URL getSourceURL() {
		return sourceURL;
	}

	/**
	 * Set the source URL for the data in this DOAP form.
	 */
	public void setSourceURL(URL sourceURL) {
		this.sourceURL = sourceURL;
	}
}

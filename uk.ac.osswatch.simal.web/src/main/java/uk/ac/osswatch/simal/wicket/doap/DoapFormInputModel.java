package uk.ac.osswatch.simal.wicket.doap;

import java.net.URL;

import org.apache.wicket.IClusterable;

/**
 * An input model for managing a DOAP form.
 * 
 */
public class DoapFormInputModel implements IClusterable {
	private static final long serialVersionUID = -9089647575258232806L;
	private URL sourceURL;
	private String name;
	private String shortDesc;
	private String description;

	/**
	 * Get the full description for this project.
	 * @return
	 */
	public String getDescription() {
    return description;
  }

	/**
	 * Set the full description for this project.
	 * @param description
	 */
  public void setDescription(String description) {
    this.description = description;
  }

  public DoapFormInputModel() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
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

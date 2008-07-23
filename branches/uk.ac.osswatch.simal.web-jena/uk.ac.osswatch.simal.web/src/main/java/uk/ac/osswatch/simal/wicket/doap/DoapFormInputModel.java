package uk.ac.osswatch.simal.wicket.doap;
/*
 * Copyright 2008 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");   *
 * you may not use this file except in compliance with the License.  *
 * You may obtain a copy of the License at                           *
 *                                                                   *
 *   http://www.apache.org/licenses/LICENSE-2.0                      *
 *                                                                   *
 * Unless required by applicable law or agreed to in writing,        *
 * software distributed under the License is distributed on an       *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY            *
 * KIND, either express or implied.  See the License for the         *
 * specific language governing permissions and limitations           *
 * under the License.                                                *
 */


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
	private String rawRDF;

	/**
	 * If the form contains raw RDF get that RDF content.
	 * @return
	 */
	public String getRawRDF() {
    return rawRDF;
  }

	/**
	 * Set the raw RDF content in the form.
	 * @param rawRDF
	 */
  public void setRawRDF(String rawRDF) {
    this.rawRDF = rawRDF;
  }

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

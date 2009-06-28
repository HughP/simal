package uk.ac.osswatch.simal.model;
/*
 * Copyright 2007 University of Oxford
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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.lang.StringEscapeUtils;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public abstract class AbstractResource implements IResource {
	private static final long serialVersionUID = 1L;
	protected String uri;
	
	public AbstractResource() {
		super();
	}

	/**
	 * Create a new resource.
	 * 
 	 * @param simalID - the id of this resource in this server. Note this is not a world unique identifier.
	 * @throws SimalRepositoryException 
	 * @throws URISyntaxException
	 */
	public AbstractResource(String simalID) throws SimalRepositoryException {
		if (!simalID.startsWith("/")) {
			simalID = "/" + simalID;
		}
		setPath(simalID);
		setSimalID(simalID);
	}

	public String getLabel() {
		return getLabel(null);
	}

	public String getURI() {
		return uri;
	}

	/**
	 * @deprecated use getURI()
	 */
	public URL getURL() throws SimalRepositoryException {
		try {
			return new URL(getURI());
		} catch (MalformedURLException e) {
			throw new SimalRepositoryException(
					"Unable to create an URL for resource, this method is deprecated, use getURI() instead",
					e);
		}
	}

	public String toJSON(boolean asRecord) {
		StringBuffer json = new StringBuffer();
		if (!asRecord) {
			json.append("{ \"items\": [");
		}
		json.append("{");
		json.append("\"id\":\"" + getURI() + "\",");
		json
				.append("\"label\":\""
						+ StringEscapeUtils.escapeJavaScript(getLabel().trim())
						+ "\",");
		json.append("}");
		if (!asRecord) {
			json.append("]}");
		}
		return json.toString();
	}

	public String toString() {
		return getLabel();
	}

    public String getSimalID() throws SimalRepositoryException {
      return getUniqueSimalID();
    }
    

	/** The JCR Path of this object **/
    String path;
    public String getPath() {
    	return path;
    }
    
    protected void setPath(URI uri) {
    	path = "/" + uri.getHost() + uri.getPath();
    	path = "/myFolder";
    }
    
    public void setPath(String path) {
    	this.path = path;
    }

}

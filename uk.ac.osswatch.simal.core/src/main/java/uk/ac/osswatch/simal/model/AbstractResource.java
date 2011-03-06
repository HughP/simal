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
import java.net.URL;

import org.apache.commons.lang.StringEscapeUtils;

import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.xmloutput.impl.BaseXMLWriter;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;

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

	public void setURI(String uri) {
		this.uri = uri;
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

	public String toJSON() throws SimalRepositoryException {
		StringBuffer json = new StringBuffer();
	  json.append("{ \"items\": [");
		json.append("{");
		json.append("\"id\":\"" + getURI() + "\",");
		json
				.append("\"label\":\""
						+ StringEscapeUtils.escapeJavaScript(getLabel().trim())
						+ "\",");
		json.append("}");
		json.append("]}");
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
    	// path = "/" + uri.getHost() + uri.getPath();
    	path = "/myFolder";
    }
    
    public void setPath(String path) {
    	this.path = path;
    }

    /**
     * Set namespace prefixes for more readable XML output.
     * @param writer
     * @return same writer with prefixes set.
     */
    protected RDFWriter setNsPrefixes(BaseXMLWriter writer) {
      writer.setNsPrefix("", RDFUtils.DOAP_NS);
      writer.setNsPrefix(RDFUtils.SIMAL_PREFIX, RDFUtils.SIMAL_NS);
      writer.setNsPrefix(RDFUtils.FOAF_PREFIX, RDFUtils.FOAF_NS);
      writer.setNsPrefix(RDFUtils.DC_PREFIX, RDFUtils.DC_NS);
      writer.setNsPrefix(RDFUtils.RDF_PREFIX, RDFUtils.RDF_NS);
      writer.setNsPrefix(RDFUtils.RDFS_PREFIX, RDFUtils.RDFS_NS);

      return writer;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((path == null) ? 0 : path.hashCode());
      result = prime * result + ((uri == null) ? 0 : uri.hashCode());
      return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      AbstractResource other = (AbstractResource) obj;
      if (path == null) {
        if (other.path != null)
          return false;
      } else if (!path.equals(other.path))
        return false;
      if (uri == null) {
        if (other.uri != null)
          return false;
      } else if (!uri.equals(other.uri))
        return false;
      return true;
    }

}

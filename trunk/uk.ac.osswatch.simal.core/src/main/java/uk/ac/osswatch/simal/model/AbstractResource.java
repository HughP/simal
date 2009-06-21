package uk.ac.osswatch.simal.model;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringEscapeUtils;


import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public abstract class AbstractResource implements IResource {
	private static final long serialVersionUID = 1L;
	protected String uri;

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

}

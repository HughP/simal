package uk.ac.osswatch.simal.model.jena;

/*
 * 
 Copyright 2007 University of Oxford * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.jena.SimalRepository;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.ResourceUtils;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class Resource implements IResource {
  private static final long serialVersionUID = -10828811166985970L;

  private static final Logger logger = LoggerFactory.getLogger(Resource.class);

  /**
   * A copy of the jena resource in use. This should never be referenced directly
   * as it is transient and may not have been reinstated after serialisation.
   * Instead use the getJenaResource() method which will reinstate the object
   * when necessary.
   */
  transient private com.hp.hpl.jena.rdf.model.Resource jenaResource;
  String uri;

  public Resource(com.hp.hpl.jena.rdf.model.Resource resource) {
    setJenaResource(resource);
  }

  /**
   * The JENA resource object is transient (since it should be serialised to the
   * repository). Therefore we need to capture the URI as well as the resource
   * itself. This method will do both for us.
   * 
   * @param resource
   */
  private void setJenaResource(com.hp.hpl.jena.rdf.model.Resource resource) {
    jenaResource = resource;
    uri = resource.getURI();
  }
  
  /**
   * If we have a copy of the JENA resource then return it.
   * If not then we must have deserialised this object from
   * somewhere other than the repository, therefore we need
   * to grab the JENA resource from the repository and return
   * that instead.
   * 
   * @return
   */
  protected com.hp.hpl.jena.rdf.model.Resource getJenaResource() {
    if (jenaResource == null) {
      try {
        ISimalRepository repo = SimalRepository.getInstance();
        return ((SimalRepository)repo).getJenaResource(getURI());
      } catch (SimalRepositoryException e) {
        logger.warn("Unable to get hold of the repository, but we are already running, this should be impossible", e);
        return null;
      }
    } else {
      return jenaResource; 
    }
  }

  public String getComment() {
    Statement commentStatement = getJenaResource().getProperty(RDFS.comment);
    String comment;
    if (commentStatement == null) {
      comment = "";
    } else {
      comment = commentStatement.getString();
    }
    return comment;
  }

  public String getLabel() {
    return getLabel(null);
  }

  public String getLabel(String defaultLabel) {
    String label;
    Statement labelStatement = getJenaResource().getProperty(RDFS.label);
    if (labelStatement == null) {
      label = defaultLabel;
    } else {
      label = labelStatement.getString();
    }
    if (label == null) {
      label = getJenaResource().getURI();
    }
    return label;
  }

  public Set<URI> getSeeAlso() {
    StmtIterator itr = getJenaResource().listProperties(RDFS.seeAlso);
    Set<URI> uris = new HashSet<URI>();
    while (itr.hasNext()) {
      try {
        uris.add(new URI(itr.nextStatement().getResource().getURI()));
      } catch (Exception e) {
        logger.warn("Unable to generate URI for seeAlso - ignoring", e);
      }
    }
    return uris;
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

  public void delete() throws SimalRepositoryException {
    getJenaResource().removeProperties();
    Model model = getJenaResource().getModel();
    Property o = model.createProperty("http://usefulinc.com/ns/doap#Project");
    model.remove(jenaResource, RDF.type, o);
  }

  public String toJSON(boolean asRecord) {
    StringBuffer json = new StringBuffer();
    if (!asRecord) {
      json.append("{ \"items\": [");
    }
    json.append("{");
    json.append("\"id\":\"" + getJenaResource().getURI() + "\",");
    json.append("\"label\":\"" + StringEscapeUtils.escapeJavaScript(getLabel().trim()) + "\",");
    json.append("}");
    if (!asRecord) {
      json.append("]}");
    }
    return json.toString();
  }

  public String toXML() throws SimalRepositoryException {
    Model model = ResourceUtils.reachableClosure(getJenaResource());
    RDFWriter writer = model.getWriter("RDF/XML-ABBREV");
    StringWriter sw = new StringWriter();
    writer.write(model, sw, null);
    return sw.toString();
  }

  public String toString() {
    return getLabel();
  }

  public Object getRepositoryResource() {
    return jenaResource;
  }

  public String getSimalID() throws SimalRepositoryException {
    return null;
  }

  public void setSimalID(String newID) {
    logger.warn("Attempt to set the Simal ID on a base resource, must override the setSimalID method in the model.");
  }

  public Set<String> getSources() {
    HashSet<String> result = new HashSet<String>();
    StmtIterator sources = jenaResource.listProperties(RDFS.seeAlso);
    while (sources.hasNext()) {
      Statement stmt = sources.nextStatement();
      result.add(stmt.getObject().toString());
    }
    return result;
  }
}

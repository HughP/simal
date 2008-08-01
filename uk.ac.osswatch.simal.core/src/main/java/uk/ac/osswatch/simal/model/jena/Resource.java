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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class Resource implements IResource {
  private static final long serialVersionUID = -10828811166985970L;

  private static final Logger logger = LoggerFactory.getLogger(Resource.class);

  protected com.hp.hpl.jena.rdf.model.Resource jenaResource ;
  
  public Resource(com.hp.hpl.jena.rdf.model.Resource resource) {
    jenaResource = resource;
  }
  
  public String getComment() {
    Statement commentStatement = jenaResource.getProperty(RDFS.comment);
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
    Statement labelStatement = jenaResource.getProperty(RDFS.label);
    if (labelStatement == null) {
      label = defaultLabel;
    } else {
      label = labelStatement.getString();
    }
    if (label == null) {
      label = jenaResource.getURI();
    }
    return label;
  }

  public Set<URI> getSeeAlso() {
    StmtIterator itr = jenaResource.listProperties(RDFS.seeAlso);
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
    return jenaResource.getURI();
  }

  public URL getURL() throws SimalRepositoryException {
    try {
      return new URL(getURI());
    } catch (MalformedURLException e) {
      throw new SimalRepositoryException("Unable to create an URL for resource, this method is deprecated, use getURI() instead", e);
    }
  }

  public void delete() throws SimalRepositoryException {
    jenaResource.removeProperties();
    Model model = jenaResource.getModel();
    Property o = model.createProperty( "http://usefulinc.com/ns/doap#Project" );
    model.remove(jenaResource, RDF.type, o);
  }

  public String toJSON(boolean asRecord) {
    StringBuffer json = new StringBuffer();
    if (!asRecord) {
      json.append("{ \"items\": [");
    }
    json.append("{");
    json.append("\"id\":\"" + jenaResource.getURI() + "\",");
    json.append("\"label\":\"" + getLabel() + "\",");
    json.append("}");
    if (!asRecord) {
      json.append("]}");
    }
    return json.toString();
  }

  public String toXML() throws SimalRepositoryException {
    Model model = jenaResource.getModel();
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


}

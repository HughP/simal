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
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.AbstractResource;
import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.model.jena.simal.JenaSimalRepository;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.ResourceUtils;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.xmloutput.impl.BaseXMLWriter;

public class Resource extends AbstractResource {
  private static final long serialVersionUID = -10828811166985970L;

  private static final Logger logger = LoggerFactory.getLogger(Resource.class);

  /**
   * A copy of the jena resource in use. This should never be referenced
   * directly as it is transient and may not have been reinstated after
   * serialisation. Instead use the getJenaResource() method which will
   * reinstate the object when necessary.
   */
  transient private com.hp.hpl.jena.rdf.model.Resource jenaResource;

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
    setURI(resource.getURI());
  }

  /**
   * If we have a copy of the JENA resource then return it. If not then we must
   * have deserialised this object from somewhere other than the repository,
   * therefore we need to grab the JENA resource from the repository and return
   * that instead.
   * 
   * @return
   */
  protected com.hp.hpl.jena.rdf.model.Resource getJenaResource() {
    if (jenaResource == null) {
      try {
        ISimalRepository repo = SimalRepositoryFactory.getInstance();
        return ((JenaSimalRepository) repo).getJenaResource(getURI());
      } catch (SimalRepositoryException e) {
        logger
            .warn(
                "Unable to get hold of the repository, but we are already running, this should be impossible",
                e);
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

  public String getLabel(String defaultLabel) {
    String label = null;
    Statement labelStatement = getJenaResource().getProperty(RDFS.label);
    if (labelStatement != null) {
      label = labelStatement.getString();
    } else {
      labelStatement = getJenaResource().getProperty(DC.title);
      if (labelStatement != null) {
        label = labelStatement.getString();
      }
    }
    
    if (label == null){
      label = defaultLabel; 
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

  public void delete() throws SimalRepositoryException {
    delete(false);
  }

  protected void delete(boolean cascading) {
    Model model = getJenaResource().getModel();
    
    if (cascading) {
      StmtIterator seeAlsos = getJenaResource().listProperties(RDFS.seeAlso);
      while (seeAlsos.hasNext()) {
        String seeAlso = seeAlsos.nextStatement().getObject().toString();
        com.hp.hpl.jena.rdf.model.Resource resource = model.getResource(seeAlso);
        resource.removeProperties();
        model.removeAll(resource, null, null);
      }
    }
    
    getJenaResource().removeProperties();
    model.removeAll(getJenaResource(), null, null);
  }
  
  public Object getRepositoryResource() {
    return getJenaResource();
  }

  /**
   * Base resources do not have a Simal ID. This method will always return null
   * unless a class overrides it.
   */
  public String getUniqueSimalID() throws SimalRepositoryException {
    return null;
  }

  public void setSimalID(String newID) throws SimalRepositoryException {
    logger
        .warn("Attempt to set the Simal ID on a base resource, must override the setSimalID method in the model.");
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

  /**
   * Get the literal value of the supplied property by looking in 
   * the default entity (the Simal Entity) and then working through
   * the fallback options (the seeAlso entities).
   * 
   * @param property
   * @return
   */
  protected String getLiteralValue(Property property) {
    Statement value = getJenaResource().getProperty(property);
    if (value == null) {
      StmtIterator seeAlsos = getJenaResource().listProperties(RDFS.seeAlso);
      while (seeAlsos.hasNext() && value == null) {
        String seeAlso = seeAlsos.nextStatement().getObject().toString();
        com.hp.hpl.jena.rdf.model.Resource resource = getJenaResource().getModel().getResource(seeAlso);
        value = resource.getProperty(property);
      }
    }
    if (value != null) {
      return value.getString().trim();
    } else {
      return null;
    }
  }

  public String toXML() throws SimalRepositoryException {
    Model model = ResourceUtils.reachableClosure(getJenaResource());
    RDFWriter writer = model.getWriter("RDF/XML-ABBREV");
    if(writer instanceof BaseXMLWriter) {
      writer = setNsPrefixes((BaseXMLWriter) writer);
    }
    StringWriter sw = new StringWriter();
    writer.write(model, sw, null);

    return sw.toString();
  }

  /**
   * Get all properties of a given type attached to this 
   * resource and those resources linked via rdfs:seealso.
   * 
   * @param property
   * @return
   */
  protected List<Statement> listProperties(Property property) {
    return listProperties(property, true);
  }
  
  protected List<Statement> listProperties(Property property, boolean cascading) {
    List<Statement> props = getJenaResource().listProperties(property).toList();
    
    if (cascading) {
      StmtIterator seeAlsos = getJenaResource().listProperties(RDFS.seeAlso);
      while (seeAlsos.hasNext()) {
        String seeAlso = seeAlsos.nextStatement().getObject().toString();
        com.hp.hpl.jena.rdf.model.Resource resource = getJenaResource().getModel().getResource(seeAlso);
        List<Statement> seeAlsoProps = resource.listProperties(property).toList();
        props.addAll(seeAlsoProps);
      }
    }
    
    return props;
  }

  protected void addLiteralStatement(Property property, String literal) {
    getJenaResource().getModel().add(getJenaResource(), property, literal);
  }
  
  protected void replaceLiteralStatements(Property property, Set<String> literals) {
    getJenaResource().removeAll(property);
    for(String literal : literals) {
      addLiteralStatement(property, literal);
    }
  }

  protected void addResourceStatement(Property property, IResource resource) {
    Model model = getJenaResource().getModel();
    Statement statement = model.createStatement(getJenaResource(),
        property, (com.hp.hpl.jena.rdf.model.Resource) resource
            .getRepositoryResource());
    model.add(statement);
  }
  
  protected void removeLiteralStatement(Property property, String literal) {
    Model model = getJenaResource().getModel();
    Statement statement = model.createStatement(
        getJenaResource(), property, literal);
    model.remove(statement);  
  }

  protected void removeResourceStatement(Property property, IResource resource) {
    Model model = getJenaResource().getModel();
    Statement statement = model.createStatement(getJenaResource(),
        property, (com.hp.hpl.jena.rdf.model.Resource) resource
            .getRepositoryResource());
    model.remove(statement);
  }

  protected void replacePropertyStatements(Property property,
      Set<? extends IResource> resources) {
    getJenaResource().removeAll(property);
    for (IResource resource : resources) {
      addResourceStatement(property, resource);
    }
  }

  public void addSeeAlso(URI uri) {
    com.hp.hpl.jena.rdf.model.Resource resource = getJenaResource().getModel()
        .createResource(uri.toASCIIString());
    getJenaResource().addProperty(RDFS.seeAlso, resource);
  }

  public void setComment(String comment) {
    getJenaResource().removeAll(RDFS.comment);
    getJenaResource().addLiteral(RDFS.comment, comment);
  }

  public void setLabel(String label) {
    getJenaResource().removeAll(RDFS.label);
    getJenaResource().addLiteral(RDFS.label, label);
  }

  public void setSeeAlso(Set<URI> seeAlsos) {
    getJenaResource().removeAll(RDFS.seeAlso);
    for (URI literal : seeAlsos) {
      com.hp.hpl.jena.rdf.model.Resource seeAlso = ResourceFactory
          .createResource(literal.toString());
      getJenaResource().getModel()
          .add(getJenaResource(), RDFS.seeAlso, seeAlso);
    }
  }
}
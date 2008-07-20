package uk.ac.osswatch.simal.model.jena;

import java.net.URI;
import java.net.URL;
import java.util.Set;

import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class Resource implements IResource {

  protected com.hp.hpl.jena.rdf.model.Resource jenaResource ;
  
  public Resource(com.hp.hpl.jena.rdf.model.Resource resource) {
    jenaResource = resource;
  }
  
  public String getComment() {
    // TODO Auto-generated method stub
    return null;
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
    // TODO Auto-generated method stub
    return null;
  }

  public String getURI() throws SimalRepositoryException {
    return jenaResource.getURI();
  }

  public URL getURL() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public void delete() throws SimalRepositoryException {
    jenaResource.removeProperties();
    Model model = jenaResource.getModel();
    Property o = model.createProperty( "http://usefulinc.com/ns/doap#Project" );
    model.remove(jenaResource, RDF.type, o);
  }

  public String toJSON(boolean asRecord) {
    // TODO Auto-generated method stub
    return null;
  }

  public String toXML() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

}

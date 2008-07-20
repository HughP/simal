package uk.ac.osswatch.simal.model.jena;

import java.net.URI;
import java.net.URL;
import java.util.Set;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import uk.ac.osswatch.simal.model.Doap;
import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

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

  public URI getURI() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public URL getURL() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public void delete() throws SimalRepositoryException {
    // TODO Auto-generated method stub

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

package uk.ac.osswatch.simal.model.elmo;

import java.net.MalformedURLException;
import java.net.URL;

import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IDoapHomepageBehaviour;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

@rdf("http://usefulinc.com/ns/doap#homepage")
public class DoapHomepageBehaviour extends DoapResourceBehaviour implements IDoapHomepageBehaviour {

  /**
   * Create a new homepage behaviour to operate on a
   * IDoapHomepage object.
   */
  public DoapHomepageBehaviour(IDoapHomepage homepage) {
    super(homepage);
  }

  /**
   * Get the URL for this homepage.
   */
  public URL getURL() throws SimalRepositoryException {
    String ns = elmoEntity.getQName().getNamespaceURI();
    String local = elmoEntity.getQName().getLocalPart();
    try {
      return new URL(ns + local);
    } catch (MalformedURLException e) {
      throw new SimalRepositoryException("Unable to create homepage URL", e);
    }
  }
}

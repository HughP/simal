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
}

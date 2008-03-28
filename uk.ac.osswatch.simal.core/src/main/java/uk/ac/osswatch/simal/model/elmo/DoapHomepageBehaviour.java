package uk.ac.osswatch.simal.model.elmo;

import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IDoapHomepageBehaviour;

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

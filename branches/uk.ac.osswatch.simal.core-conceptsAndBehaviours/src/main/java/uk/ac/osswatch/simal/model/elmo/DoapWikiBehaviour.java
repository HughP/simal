package uk.ac.osswatch.simal.model.elmo;

import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.model.IDoapWiki;
import uk.ac.osswatch.simal.model.IDoapWikiBehaviour;

@rdf("http://usefulinc.com/ns/doap#wiki")
public class DoapWikiBehaviour extends DoapResourceBehaviour implements IDoapWikiBehaviour {

  /**
   * Create a new wiki behaviour to operate on a
   * ICategory object.
   */
  public DoapWikiBehaviour(IDoapWiki wiki) {
    super(wiki);
  }

}

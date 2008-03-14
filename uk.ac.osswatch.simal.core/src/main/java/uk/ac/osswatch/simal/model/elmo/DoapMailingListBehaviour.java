package uk.ac.osswatch.simal.model.elmo;

import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.model.IDoapMailingList;
import uk.ac.osswatch.simal.model.IDoapMailingListBehaviour;

@rdf("http://usefulinc.com/ns/doap#mailing-list")
public class DoapMailingListBehaviour extends DoapResourceBehaviour implements IDoapMailingListBehaviour {

  /**
   * Create a new issue tracker behaviour to operate on a
   * IIssueTracker object.
   */
  public DoapMailingListBehaviour(IDoapMailingList list) {
    super(list);
  }

}

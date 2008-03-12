package uk.ac.osswatch.simal.model.elmo;

import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.model.IIssueTracker;
import uk.ac.osswatch.simal.model.IIssueTrackerBehaviour;

@rdf("http://usefulinc.com/ns/doap#bug-database")
public class IssueTrackerBehaviour extends DoapResourceBehaviour implements IIssueTrackerBehaviour {

  /**
   * Create a new issue tracker behaviour to operate on a
   * IIssueTracker object.
   */
  public IssueTrackerBehaviour(IIssueTracker tracker) {
    super(tracker);
  }

}

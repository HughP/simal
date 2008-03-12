package uk.ac.osswatch.simal.model.elmo;

import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.model.IDoapBugDatabase;
import uk.ac.osswatch.simal.model.IBugDatabaseBehaviour;

@rdf("http://usefulinc.com/ns/doap#bug-database")
public class BugDatabaseBehaviour extends DoapResourceBehaviour implements IBugDatabaseBehaviour {

  /**
   * Create a new issue tracker behaviour to operate on a
   * IIssueTracker object.
   */
  public BugDatabaseBehaviour(IDoapBugDatabase tracker) {
    super(tracker);
  }

}

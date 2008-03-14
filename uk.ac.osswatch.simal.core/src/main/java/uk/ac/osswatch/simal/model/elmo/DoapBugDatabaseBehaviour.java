package uk.ac.osswatch.simal.model.elmo;

import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.model.IDoapBugDatabase;
import uk.ac.osswatch.simal.model.IDoapBugDatabaseBehaviour;

@rdf("http://usefulinc.com/ns/doap#bug-database")
public class DoapBugDatabaseBehaviour extends DoapResourceBehaviour implements IDoapBugDatabaseBehaviour {

  /**
   * Create a new issue tracker behaviour to operate on a
   * IIssueTracker object.
   */
  public DoapBugDatabaseBehaviour(IDoapBugDatabase tracker) {
    super(tracker);
  }

}

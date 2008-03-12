package uk.ac.osswatch.simal.model;

import org.openrdf.concepts.rdfs.Resource;
import org.openrdf.elmo.annotations.rdf;

/**
 * An issue tracker that is known to apply be used by a project
 * It is a resource used to define the doap:bug-database entries
 * of doap:Project.  
 *
 */
@rdf("http://usefulinc.com/ns/doap#bug-database")
public interface IDoapBugDatabase extends Resource, IBugDatabaseBehaviour {

}

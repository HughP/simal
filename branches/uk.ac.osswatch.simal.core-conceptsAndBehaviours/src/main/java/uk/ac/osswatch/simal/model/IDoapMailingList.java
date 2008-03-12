package uk.ac.osswatch.simal.model;

import org.openrdf.concepts.rdfs.Resource;
import org.openrdf.elmo.annotations.rdf;

/**
 * A mailing list that is known to be relevant to a project
 * It is a resource used to define the doap:mailing-list entries
 * of doap:Project.  
 *
 */
@rdf("http://usefulinc.com/ns/doap#mailing-list")
public interface IDoapMailingList extends Resource, IDoapMailingListBehaviour {

}

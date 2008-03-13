package uk.ac.osswatch.simal.model;

import java.util.Set;

import org.openrdf.elmo.annotations.rdf;
import org.openrdf.concepts.doap.Repository;

/**
 * A repository that is known to apply be used by a project
 * It is a resource used to define the doap:repository entries
 * of doap:Project.  
 *
 */
@rdf("http://usefulinc.com/ns/doap#repository")
public interface IDoapRepository extends Repository, IDoapRepositoryBehaviour {

}

package uk.ac.osswatch.simal.model;

import org.openrdf.concepts.rdfs.Resource;
import org.openrdf.elmo.annotations.rdf;

/**
 * A release of a project.
 * It is a resource used to define the doap:release entries
 * of doap:Project.  
 *
 */
@rdf("http://usefulinc.com/ns/doap#release")
public interface IDoapRelease extends Resource, IDoapReleaseBehaviour {

}

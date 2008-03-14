package uk.ac.osswatch.simal.model;

import org.openrdf.concepts.rdfs.Resource;
import org.openrdf.elmo.annotations.rdf;

/**
 * A wiki associated with a project.
 * It is a resource used to define the doap:wiki entries
 * of a doap:Project.  
 *
 */
@rdf("http://usefulinc.com/ns/doap#wiki")
public interface IDoapWiki extends Resource, IDoapWikiBehaviour {

}

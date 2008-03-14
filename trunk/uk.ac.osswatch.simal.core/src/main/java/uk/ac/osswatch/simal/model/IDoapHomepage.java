package uk.ac.osswatch.simal.model;

import org.openrdf.concepts.rdfs.Resource;
import org.openrdf.elmo.annotations.rdf;

/**
 * A category is part of the project classification system.
 * It is a resource used to define the doap:category entries
 * of a doap:Project.  
 *
 */
@rdf("http://usefulinc.com/ns/doap#homepage")
public interface IDoapHomepage extends Resource, IDoapHomepageBehaviour {

}

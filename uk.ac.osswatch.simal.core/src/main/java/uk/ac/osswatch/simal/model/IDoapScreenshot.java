package uk.ac.osswatch.simal.model;

import org.openrdf.concepts.rdfs.Resource;
import org.openrdf.elmo.annotations.rdf;

/**
 * A Screenshot associated with a project.
 * It is a resource used to define the doap:screenshot entries
 * of a doap:Project.  
 *
 */
@rdf("http://usefulinc.com/ns/doap#screenshot")
public interface IDoapScreenshot extends Resource, IDoapScreenshotBehaviour {

}

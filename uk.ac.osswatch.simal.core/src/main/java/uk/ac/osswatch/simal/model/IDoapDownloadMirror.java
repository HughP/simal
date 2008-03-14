package uk.ac.osswatch.simal.model;

import org.openrdf.concepts.rdfs.Resource;
import org.openrdf.elmo.annotations.rdf;

/**
 * A download mirror associated with a project.
 * It is a resource used to define the doap:download-mirror entries
 * of a doap:Project.  
 *
 */
@rdf("http://usefulinc.com/ns/doap#download-mirror")
public interface IDoapDownloadMirror extends Resource, IDoapDownloadMirrorBehaviour {

}

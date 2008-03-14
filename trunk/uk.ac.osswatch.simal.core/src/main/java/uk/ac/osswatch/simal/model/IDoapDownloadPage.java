package uk.ac.osswatch.simal.model;

import org.openrdf.concepts.rdfs.Resource;
import org.openrdf.elmo.annotations.rdf;

/**
 * A download page associated with a project.
 * It is a resource used to define the doap:download-page entries
 * of a doap:Project.  
 *
 */
@rdf("http://usefulinc.com/ns/doap#download-page")
public interface IDoapDownloadPage extends Resource, IDoapDownloadPageBehaviour {

}

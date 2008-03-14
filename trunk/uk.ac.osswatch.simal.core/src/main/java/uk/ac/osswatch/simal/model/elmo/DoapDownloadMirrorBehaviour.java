package uk.ac.osswatch.simal.model.elmo;

import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.model.IDoapDownloadMirror;
import uk.ac.osswatch.simal.model.IDoapDownloadMirrorBehaviour;

@rdf("http://usefulinc.com/ns/doap#download-mirror")
public class DoapDownloadMirrorBehaviour extends DoapResourceBehaviour implements IDoapDownloadMirrorBehaviour {

  /**
   * Create a new download mirror behaviour to operate on a
   * ICategory object.
   */
  public DoapDownloadMirrorBehaviour(IDoapDownloadMirror mirror) {
    super(mirror);
  }

}

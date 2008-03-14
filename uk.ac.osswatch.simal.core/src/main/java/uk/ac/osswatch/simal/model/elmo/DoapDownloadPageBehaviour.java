package uk.ac.osswatch.simal.model.elmo;

import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.model.IDoapDownloadPage;
import uk.ac.osswatch.simal.model.IDoapDownloadPageBehaviour;

@rdf("http://usefulinc.com/ns/doap#download-page")
public class DoapDownloadPageBehaviour extends DoapResourceBehaviour implements IDoapDownloadPageBehaviour {

  /**
   * Create a new download page behaviour to operate on a
   * ICategory object.
   */
  public DoapDownloadPageBehaviour(IDoapDownloadPage page) {
    super(page);
  }

}

package uk.ac.osswatch.simal.model.elmo;

import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.model.IDoapScreenshot;
import uk.ac.osswatch.simal.model.IDoapScreenshotBehaviour;

@rdf("http://usefulinc.com/ns/doap#screenshot")
public class DoapScreenshotBehaviour extends DoapResourceBehaviour implements IDoapScreenshotBehaviour {

  /**
   * Create a new Screenshot behaviour to operate on a
   * ICategory object.
   */
  public DoapScreenshotBehaviour(IDoapScreenshot screenshot) {
    super(screenshot);
  }

}

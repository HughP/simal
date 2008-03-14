package uk.ac.osswatch.simal.model.elmo;

import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IDoapCategoryBehaviour;

@rdf("http://usefulinc.com/ns/doap#category")
public class DoapCategoryBehaviour extends DoapResourceBehaviour implements IDoapCategoryBehaviour {

  /**
   * Create a new category behaviour to operate on a
   * ICategory object.
   */
  public DoapCategoryBehaviour(IDoapCategory category) {
    super(category);
  }

}

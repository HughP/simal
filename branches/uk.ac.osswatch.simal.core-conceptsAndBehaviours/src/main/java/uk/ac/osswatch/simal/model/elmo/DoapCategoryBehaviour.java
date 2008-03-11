package uk.ac.osswatch.simal.model.elmo;

import org.openrdf.concepts.rdfs.Resource;
import org.openrdf.elmo.annotations.rdf;

import uk.ac.osswatch.simal.model.ICategory;
import uk.ac.osswatch.simal.model.ICategoryBehaviour;

@rdf("http://usefulinc.com/ns/doap#category")
public class DoapCategoryBehaviour extends DoapResourceBehaviour implements ICategoryBehaviour {

  /**
   * Create a new category behaviour to operate on a
   * ICategory object.
   */
  public DoapCategoryBehaviour(ICategory category) {
    super(category);
  }
  /**
   * Get a human readable label for a category.
   * If no label is defined using rdfs:label then
   * return the QName of the resource.
   * 
   * @return
   */
  public String getLabel() {
    String label = ((Resource)elmoEntity).getRdfsLabel();
    if (label == null) {
      return elmoEntity.getQName().toString();
    } else {
      return label;
    }
    
  }

}

package uk.ac.osswatch.simal.wicket.data;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

import uk.ac.osswatch.simal.model.IFoafResourceBehaviour;
import uk.ac.osswatch.simal.model.IPerson;

/**
 * A FOAF resource data provider that allows the FOAF Resources to be sorted.
 * 
 */
public class SortableFoafResourceDataProvider extends SortableDataProvider {
  private static final long serialVersionUID = -6674850425804180338L;

  public static final String SORT_PROPERTY_LABEL = "label";
  public static final String SORT_PROPERTY_EMAIL = "email";

  /**
   * The set of FoafResources we are providing access to.
   */
  private Set<IFoafResourceBehaviour> resources;

  /**
   * Create a data provider for the supplied resources.
   */
  @SuppressWarnings("unchecked")
  public SortableFoafResourceDataProvider(
      Set<? extends IFoafResourceBehaviour> resources) {
    this.resources = (Set<IFoafResourceBehaviour>) resources;
  }

  @Override
  public void setSort(SortParam param) {
    if (validateSortProperty(param.getProperty())) {
      super.setSort(param);
    } else {
      throw new RuntimeException("Set an unknown sort property: "
          + param.getProperty());
    }
  }

  @Override
  public void setSort(String property, boolean isAscending) {
    if (validateSortProperty(property)) {
      super.setSort(property, isAscending);
    } else {
      throw new RuntimeException("Set an unknown sort property: " + property);
    }
  }

  /**
   * Ensures a sort property is valid, that is IDoapResourceBehaviours can be
   * sorted by it.
   * 
   * @param property
   * @return
   */
  private boolean validateSortProperty(String property) {
    return property.equals(SORT_PROPERTY_LABEL);
  }

  public Iterator<IFoafResourceBehaviour> iterator(int first, int count) {
    IFoafResourceBehaviourComparator comparator = new IFoafResourceBehaviourComparator();
    TreeSet<IFoafResourceBehaviour> treeSet = new TreeSet<IFoafResourceBehaviour>(
        comparator);
    treeSet.addAll(resources);
    TreeSet<IFoafResourceBehaviour> result = new TreeSet<IFoafResourceBehaviour>(
        comparator);
    int idx = 0;
    Iterator<IFoafResourceBehaviour> all = treeSet.iterator();
    IFoafResourceBehaviour current;
    while (all.hasNext() && idx - (first + count) < 0) {
      current = all.next();
      if (idx >= first) {
        result.add(current);
      }
      idx++;
    }
    return result.iterator();
  }

  /**
   * Get an iterator over all resources. The resources will be sorted
   * accordingly.
   * 
   * @return
   */
  public Iterator<IFoafResourceBehaviour> iterator() {
    return iterator(0, resources.size());
  }

  public IModel model(Object object) {
    if (!(object instanceof IPerson)) {
      throw new IllegalArgumentException(
          "sortableFoafResourceDataProvider only works for Person models - should it work for more? Your help appreciated.");
    }
    return new DetachablePersonModel((IPerson) object);
  }

  public int size() {
    return resources.size();
  }

  private class IFoafResourceBehaviourComparator implements
      Comparator<IFoafResourceBehaviour> {

    public int compare(IFoafResourceBehaviour resource1,
        IFoafResourceBehaviour resource2) {
      if (resource1.equals(resource2)) {
        return 0;
      }

      int result = 0;

      String sortField;
      if (getSort() == null) {
        sortField = SORT_PROPERTY_LABEL;
      } else {
        sortField = getSort().getProperty();
      }
      if (sortField.equals(SORT_PROPERTY_LABEL)) {
        String name1 = (String) resource1.getLabel();
        String name2 = (String) resource2.getLabel();
        result = name1.compareTo(name2);
      } else if (sortField.equals(SORT_PROPERTY_EMAIL)
          && resource1 instanceof IPerson && resource2 instanceof IPerson) {
        String email1 = ((IPerson)resource1).getEmail();
        String email2 = ((IPerson)resource2).getEmail();
        if (email1 == null) {
          result = 1;
        } else if (email2 == null) {
          result = -1;
        } else {
          result = email1.compareTo(email2);
        }
      }
      if (result == 0) {
        result = 1;
      }
      return result;
    }
  }
}

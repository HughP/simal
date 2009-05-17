package uk.ac.osswatch.simal.wicket.data;

/*
 * Copyright 2008 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");   *
 * you may not use this file except in compliance with the License.  *
 * You may obtain a copy of the License at                           *
 *                                                                   *
 *   http://www.apache.org/licenses/LICENSE-2.0                      *
 *                                                                   *
 * Unless required by applicable law or agreed to in writing,        *
 * software distributed under the License is distributed on an       *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY            *
 * KIND, either express or implied.  See the License for the         *
 * specific language governing permissions and limitations           *
 * under the License.                                                *
 */

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IDoapResource;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A DOAP resource data provider that allows the DOAP Resources to be sorted.
 * 
 */
public class SortableDoapResourceDataProvider extends
    SortableDataProvider<IResource> {
  private static final long serialVersionUID = -6674850425804180338L;
  private static final Logger logger = LoggerFactory
      .getLogger(SortableDoapResourceDataProvider.class);

  public static final String SORT_PROPERTY_NAME = "name";
  public static final String SORT_PROPERTY_SHORTDESC = "shortDesc";

  /**
   * The set of DoapResources we are providing access to.
   */
  private Set<IDoapResource> resources;

  /**
   * Create a data provider for the supplied resources.
   */
  @SuppressWarnings("unchecked")
  public SortableDoapResourceDataProvider(Set<? extends IDoapResource> resources) {
    this.resources = (Set<IDoapResource>) resources;
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
  protected boolean validateSortProperty(String property) {
    return property.equals(SORT_PROPERTY_NAME)
        || property.equals(SORT_PROPERTY_SHORTDESC);
  }

  public Iterator<IDoapResource> iterator(int first, int count) {
    Comparator<IDoapResource> comparator = getComparator();
    TreeSet<IDoapResource> treeSet = new TreeSet<IDoapResource>(comparator);
    treeSet.addAll(resources);
    TreeSet<IDoapResource> result = new TreeSet<IDoapResource>(comparator);
    int idx = 0;
    Iterator<IDoapResource> all = treeSet.iterator();
    IDoapResource current;
    while (all.hasNext() && idx - (first + count) < 0) {
      current = all.next();
      if (idx >= first) {
        result.add(current);
      }
      idx++;
    }
    return result.iterator();
  }

  protected Comparator<IDoapResource> getComparator() {
    DoapResourceBehaviourComparator comparator = new DoapResourceBehaviourComparator();
    return comparator;
  }

  /**
   * Get an iterator over all resources. The resources will be sorted
   * accordingly.
   * 
   * @return
   */
  public Iterator<IDoapResource> iterator() {
    return iterator(0, resources.size());
  }

  @SuppressWarnings("unchecked")
  public IModel<IResource> model(IResource object) {
    try {
      if (object instanceof IProject) {
        return new DetachableProjectModel((IProject) object);
      } else if (object instanceof IDoapCategory) {
        return new DetachableCategoryModel((IDoapCategory) object);
      } else if (object instanceof IDoapHomepage) {
        return new DetachableHomepageModel((IDoapHomepage) object);
      } else {
        throw new IllegalArgumentException(
            "sortableDoapResourceDataProvider only works for Project, Homepage an Category models - should it work for more? Your help appreciated.");
      }
    } catch (SimalRepositoryException e) {
      logger.warn("Error reading from repository", e);
      return new Model("Error");
    }

  }

  public int size() {
    return resources.size();
  }

  class DoapResourceBehaviourComparator implements Comparator<IDoapResource>,
      Serializable {
    private static final long serialVersionUID = 1044456562070022248L;

    public int compare(IDoapResource resource1, IDoapResource resource2) {
      if (resource1.equals(resource2)) {
        return 0;
      }

      int result = 0;

      String sortField;
      if (getSort() == null) {
        sortField = SORT_PROPERTY_NAME;
      } else {
        sortField = getSort().getProperty();
      }
      if (sortField.equals(SORT_PROPERTY_NAME)) {
        String name1 = (String) resource1.getName();
        String name2 = (String) resource2.getName();
        result = name1.compareTo(name2);
      } else if (sortField.equals(SORT_PROPERTY_SHORTDESC)) {
        String desc1 = resource1.getShortDesc();
        String desc2 = resource2.getShortDesc();
        if (desc1 == null) {
          result = 1;
        } else if (desc2 == null) {
          result = -1;
        } else {
          result = desc1.compareTo(desc2);
        }
      }
      if (result == 0) {
        result = 1;
      }
      return result;
    }
  }

  /**
   * Update the data in this provider to only include those supplied.
   * 
   * @param resources
   */
  @SuppressWarnings("unchecked")
  public void resetData(Set<? extends IDoapResource> resources) {
    this.resources = (Set<IDoapResource>) resources;
  }
}

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

import uk.ac.osswatch.simal.model.IFoafResource;
import uk.ac.osswatch.simal.model.IInternetAddress;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A FOAF resource data provider that allows the FOAF Resources to be sorted.
 * 
 */
public class SortableFoafResourceDataProvider extends
    SortableDataProvider<IFoafResource> {
  private static final long serialVersionUID = -6674850425804180338L;
  private static final Logger logger = LoggerFactory
      .getLogger(SortableFoafResourceDataProvider.class);

  public static final String SORT_PROPERTY_LABEL = "label";
  public static final String SORT_PROPERTY_EMAIL = "email";

  /**
   * The set of FoafResources we are providing access to.
   */
  private Set<IFoafResource> resources;

  /**
   * Create a data provider for the supplied resources.
   */
  @SuppressWarnings("unchecked")
  public SortableFoafResourceDataProvider(Set<? extends IFoafResource> resources) {
    this.resources = (Set<IFoafResource>) resources;
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

  public Iterator<IFoafResource> iterator(int first, int count) {
    IFoafResourceBehaviourComparator comparator = new IFoafResourceBehaviourComparator();
    TreeSet<IFoafResource> treeSet = new TreeSet<IFoafResource>(comparator);
    treeSet.addAll(resources);
    TreeSet<IFoafResource> result = new TreeSet<IFoafResource>(comparator);
    int idx = 0;
    Iterator<IFoafResource> all = treeSet.iterator();
    IFoafResource current;
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
  public Iterator<IFoafResource> iterator() {
    return iterator(0, resources.size());
  }

  @SuppressWarnings("unchecked")
  public IModel<IFoafResource> model(IFoafResource object) {
    if (!(object instanceof IPerson)) {
      throw new IllegalArgumentException(
          "sortableFoafResourceDataProvider only works for Person models - should it work for more? Your help appreciated.");
    }
    try {
      return new DetachablePersonModel((IPerson) object);
    } catch (SimalRepositoryException e) {
      logger.warn("Unable to read person object from repository", e);
      return new Model("Error");
    }
  }

  public int size() {
    return resources.size();
  }

  /**
   * Update the data in this provider to only include those supplied.
   * 
   * @param resources
   */
  @SuppressWarnings("unchecked")
  public void resetData(Set<? extends IFoafResource> resources) {
    this.resources = (Set<IFoafResource>) resources;
  }

  private class IFoafResourceBehaviourComparator implements
      Comparator<IFoafResource>, Serializable {
    private static final long serialVersionUID = -8067880844650642351L;

    public int compare(IFoafResource resource1, IFoafResource resource2) {
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
        String email1;
        String email2;
        Set<IInternetAddress> internetAdd = ((IPerson) resource1).getEmail();
        email1 = ((IInternetAddress) internetAdd.toArray()[0]).getAddress();
        internetAdd = ((IPerson) resource2).getEmail();
        email2 = ((IInternetAddress) internetAdd.toArray()[0]).getAddress();
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

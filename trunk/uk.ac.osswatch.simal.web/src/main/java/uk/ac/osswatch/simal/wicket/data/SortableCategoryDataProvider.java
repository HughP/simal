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
import java.util.Set;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;

import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IDoapResource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.UserApplication;

/**
 * A project data provider that allows the projects to be sorted.
 * 
 */
public class SortableCategoryDataProvider extends
    SortableDoapResourceDataProvider {
  private static final long serialVersionUID = -7078982000589847543L;
  public static final String SORT_PROPERTY_PROJECTS = "projects";
  public static final String SORT_PROPERTY_PEOPLE = "people";

  /**
   * Create a SortableDataProvider containing all categories in the repository
   * 
   * @param size
   * @throws SimalRepositoryException
   */
  public SortableCategoryDataProvider() throws SimalRepositoryException {
    super(UserApplication.getRepository().getAllCategories());
  }

  /**
   * Create a SortableDataProvider containing a supplied set of categories.
   * 
   * @param size
   * @throws SimalRepositoryException
   */
  public SortableCategoryDataProvider(Set<IDoapCategory> categories) {
    super(categories);
  }

  protected Comparator<IDoapResource> getComparator() {
    DoapCategoryComparator comparator = new DoapCategoryComparator();
    return comparator;
  }

  private class DoapCategoryComparator extends DoapResourceBehaviourComparator
      implements Comparator<IDoapResource>, Serializable {
    private static final long serialVersionUID = -6313012539097903783L;

    public int compare(IDoapResource resource1, IDoapResource resource2) {
      if (resource1.equals(resource2)) {
        return 0;
      }

      int result;
      if (getSort() != null && isCategorySpecificSort(getSort().getProperty())) {
        IDoapCategory cat1 = (IDoapCategory) resource1;
        IDoapCategory cat2 = (IDoapCategory) resource2;

        String sortField = getSort().getProperty();
        Integer num1 = 0;
        Integer num2 = 0;
        if (sortField.equals(SORT_PROPERTY_PROJECTS)) {
          num1 = cat1.getProjects().size();
          num2 = cat2.getProjects().size();
        } else if (sortField.equals(SORT_PROPERTY_PEOPLE)) {
          num1 = cat1.getPeople().size();
          num2 = cat2.getPeople().size();
        }
        result = num1.compareTo(num2);
        if (result == 0) {
          result = 1;
        }
        return result;
      } else {
        result = super.compare(resource1, resource2);
      }
      return result;
    }
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

  @Override
  protected boolean validateSortProperty(String property) {
    return isCategorySpecificSort(property)
        || super.validateSortProperty(property);
  }

  private boolean isCategorySpecificSort(String property) {
    if (property == null) {
      return false;
    } else {
      return property.equals(SORT_PROPERTY_PROJECTS)
          || property.equals(SORT_PROPERTY_PEOPLE);
    }
  }
}

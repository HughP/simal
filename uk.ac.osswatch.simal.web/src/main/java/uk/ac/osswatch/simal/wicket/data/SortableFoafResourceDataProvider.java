package uk.ac.osswatch.simal.wicket.data;

/*
 * Copyright 2008,2010 University of Oxford
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

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;

import uk.ac.osswatch.simal.model.IFoafResource;

/**
 * A FOAF resource data provider that allows the FOAF Resources to be sorted.
 * 
 */
public abstract class SortableFoafResourceDataProvider<IFoafResource> extends
    SortableDataProvider<IFoafResource> {
  private static final long serialVersionUID = -6674850425804180338L;

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
  abstract protected boolean validateSortProperty(String property);

}

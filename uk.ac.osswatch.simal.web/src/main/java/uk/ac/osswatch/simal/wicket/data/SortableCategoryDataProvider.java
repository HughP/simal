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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.UserApplication;

/**
 * A project data provider that allows the projects to be sorted.
 * 
 */
public class SortableCategoryDataProvider extends
    SortableDoapResourceDataProvider {
  private static final long serialVersionUID = -7078982000589847543L;
  private static final Logger logger = LoggerFactory
      .getLogger(SortableCategoryDataProvider.class);

  /**
   * Create a SortableDataProvider containing all projects in the repository
   * 
   * @param size
   * @throws SimalRepositoryException
   */
  public SortableCategoryDataProvider() throws SimalRepositoryException {
    super(UserApplication.getRepository().getAllCategories());
  }
}

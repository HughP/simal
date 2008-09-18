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

import java.util.Set;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.UserApplication;

/**
 * A person data provider that allows the persons to be sorted.
 * 
 */
public class SortablePersonDataProvider extends
    SortableFoafResourceDataProvider {
  private static final long serialVersionUID = -2975592768329164790L;

  /**
   * Create a SortableDataProvider containing all people in the repository
   * 
   * @param size
   * @throws SimalRepositoryException
   */
  public SortablePersonDataProvider() throws SimalRepositoryException {
    super(UserApplication.getRepository().getAllPeople());
  }

  /**
   * Create a SortableDataProvider containing all people supplied.
   * 
   * @param size
   * @throws SimalRepositoryException
   */
  public SortablePersonDataProvider(Set<IPerson> people) {
    super(people);
  }
}

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

import org.apache.wicket.model.LoadableDetachableModel;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class DetachablePersonModel extends LoadableDetachableModel {
  private static final long serialVersionUID = -9017519516676203598L;
  String uri;

  public DetachablePersonModel(IPerson person) throws SimalRepositoryException {
    this.uri = person.getURI();
  }

  public DetachablePersonModel(String uri) {
    this.uri = uri;
  }

  @Override
  protected Object load() {
    IPerson person;
    try {
      person = SimalRepositoryFactory.getPersonService().get(uri);
    } catch (SimalRepositoryException e) {
      e.printStackTrace();
      person = null;
    }
    return person;
  }

}

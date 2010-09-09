package uk.ac.osswatch.simal.wicket.data;

/*
 * Copyright 2010 University of Oxford
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IDoapResource;
import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * Generic Detachable Model for any IDoapResource.
 */
public class DetachableDoapResourceModel extends
    LoadableDetachableModel<IResource> {
  private static final long serialVersionUID = -7306164656838860938L;

  private static final Logger LOGGER = LoggerFactory
      .getLogger(DetachableDoapResourceModel.class);

  private String uri;

  public DetachableDoapResourceModel(IDoapResource resource)
      throws SimalRepositoryException {
    this.uri = resource.getURI();
  }

  public DetachableDoapResourceModel(String uri) {
    this.uri = uri;
  }

  @Override
  protected IDoapResource load() {
    IDoapResource resource = null;

    try {
      resource = SimalRepositoryFactory.getDoapResourceService().get(this.uri);
    } catch (SimalRepositoryException e) {
      LOGGER.warn("Could not retrieve DoapResource with uri " + this.uri);
    }

    return resource;
  }
}

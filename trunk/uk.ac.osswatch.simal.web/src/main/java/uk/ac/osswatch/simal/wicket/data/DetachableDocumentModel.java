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

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IncompatibleTypeException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

@SuppressWarnings("hiding")
public class DetachableDocumentModel<IDocument> extends LoadableDetachableModel<IDocument> {
  public static final Logger LOGGER = LoggerFactory
      .getLogger(DetachableDocumentModel.class);

  private static final long serialVersionUID = -9017519516676203598L;

  private String uri;

  public DetachableDocumentModel(IDocument object) {
    this.uri = ((uk.ac.osswatch.simal.model.IDocument) object).getURI();
  }

  public DetachableDocumentModel(String uri) {
    this.uri = uri;
  }

  @SuppressWarnings("unchecked") // type erasure
  protected IDocument load() {
    IDocument homepage = null;
    try {
      homepage = (IDocument) SimalRepositoryFactory.getHomepageService().getOrCreate(uri);
    } catch (SimalRepositoryException e) {
      LOGGER.warn("Could not load document: " + e.getMessage(), e);
    } catch (IncompatibleTypeException e) {
      LOGGER.warn("Document appears to be of a different type: " + e.getMessage(), e);
    }
    return homepage;
  }

}

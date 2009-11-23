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
import uk.ac.osswatch.simal.model.simal.IReview;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class DetachableReviewModel extends LoadableDetachableModel<IReview> {
  private static final long serialVersionUID = -9017519516676203598L;
  String uri;

  public DetachableReviewModel(IReview review)
      throws SimalRepositoryException {
    this.uri = review.getURI();
  }

  public DetachableReviewModel(String uri) {
    this.uri = uri;
  }

  @Override
  protected IReview load() {
    IReview review;
    try {
      review = SimalRepositoryFactory.getReviewService().getReview(uri);
    } catch (SimalRepositoryException e) {
      e.printStackTrace();
      review = null;
    }
    return review;
  }

}

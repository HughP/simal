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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.simal.IReview;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.UserApplication;

/**
 * A project data provider that allows the projects to be sorted.
 * 
 */
public class SortableReviewDataProvider extends
  SortableDataProvider<IReview> {
  private static final long serialVersionUID = 1L;
  public static final String SORT_PROPERTY_REVIEWER = "reviewer";
  private static final Logger logger = LoggerFactory
  	.getLogger(SortableReviewDataProvider.class);

  /**
   * The set of Reviews we are providing access to.
   */
  private Set<IReview> reviews;

  /**
   * Create a SortableDataProvider containing all reviews in the repository
   * 
   * @throws SimalRepositoryException
   */
  public SortableReviewDataProvider() throws SimalRepositoryException {
    this.reviews = UserApplication.getRepository().getReviewService().getReviews();
  }

  /**
   * Create a SortableDataProvider containing a supplied set of categories.
   * 
   * @param size
   * @throws SimalRepositoryException
   */
  public SortableReviewDataProvider(Set<IReview> reviews) {
    this.reviews = reviews;
  }

  protected Comparator<IReview> getComparator() {
    ReviewComparator comparator = new ReviewComparator();
    return comparator;
  }

  private static class ReviewComparator implements Comparator<IReview>, Serializable {
    private static final long serialVersionUID = 1L;

    public int compare(IReview review1, IReview review2) {
      if (review1.equals(review2)) {
        return 0;
      }
      return 0;
    }
  }

public Iterator<? extends IReview> iterator(int first, int count) {
	HashSet<IReview> result = new HashSet<IReview>();
	Object[] array = reviews.toArray();
	for (int idx = first; idx < first + count; idx = idx + 1) {
		result.add((IReview)array[idx]);
	}
    return result.iterator();
}

public IModel<IReview> model(IReview object) {
    try {
          return new DetachableReviewModel((IReview) object);
      } catch (SimalRepositoryException e) {
        logger.warn("Error reading from repository", e);
        return new Model("Error");
      }
}

public int size() {
    return reviews.size();
}
}

package uk.ac.osswatch.simal.wicket.simal;

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

import java.util.GregorianCalendar;

import org.apache.wicket.IClusterable;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.simal.IReview;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.IReviewService;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;
import uk.ac.osswatch.simal.wicket.UserApplication;

/**
 * An input model for managing a FOAF object.
 * 
 */
public class ReviewFormInputModel implements IClusterable {
  private static final long serialVersionUID = 1L;
  private IPerson reviewer;
  private IProject project;;

  public ReviewFormInputModel(IProject project, IPerson reviewer) {
	  setProject(project);
	  setReviewer(reviewer);
  }

  /**
   * Get the person who performed this review.
   * 
   * @return
   */
  public IPerson getReviewer() {
    return reviewer;
  }

  /**
   * Set the person who performed this review.
   * 
   * @return
   */
  public void setReviewer(IPerson reviewer) {
    this.reviewer = reviewer;
  }

  /**
   * Get the project that is the subject of this review.
   * 
   * @return
   */
  public IProject getProject() {
    return project;
  }

  /**
   * Set the project that is the subject of this review.
   * 
   */
  public void setProject(IProject project) {
    this.project = project;
  }
  
  /**
   * Get the review defined by this form.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public IReview getReview() throws SimalRepositoryException {
    IReviewService service = UserApplication.getRepository().getReviewService();
    String id = service.getNewReviewID();
    String uri = RDFUtils.getDefaultReviewURI(id);
    IReview review;
    try {
      review = service.create(uri);
      review.setSimalID(id);
      populateReview(review, IReview.TYPE_OPENNESS);
    } catch (DuplicateURIException e) {
      throw new SimalRepositoryException("Unable to create a new review", e);
    }
    return review;
  }

  /**
   * Populate the review by adding all the data in this form to it.
   * 
   */
  private void populateReview(IReview review, String type) {
    review.setReviewer(getReviewer());
    review.setProject(getProject());
    review.setDate(GregorianCalendar.getInstance());
    review.setType(type);
  }
}

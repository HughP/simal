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


import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.simal.IReview;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.foaf.FoafFormInputModel;
import uk.ac.osswatch.simal.wicket.panel.ReviewListPanel;

/**
 * Container for adding a new review. This is an AJAX enabled container that
 * either shows the form for entering data about a review or a command link to
 * display the form.
 */
public class AddReviewPanel extends Panel {
  private static final long serialVersionUID = 1L;
  private static final Logger logger = LoggerFactory.getLogger(AddReviewPanel.class);

  /** Visibility toggle so that either the link or the form is visible. */
  private boolean formVisible = false;
  private ReviewFormInputModel inputModel;
  TextField<String> reviewerField;
  TextField<String> projectField;
  FeedbackPanel feedback;

  private ReviewListPanel updatePanel;

  /**
   * Create a new container that will initially display the command link to show
   * the form.
   * 
   * @param wicketid
   *          the id of the HTML component to host the container
   * @param role
   *          the role of any people added using this container
   * @param project
   *          the project any new people are to be assigned to
   * @param updateContainer
   *          the container that should be updated when the person has been
   *          added (must have setOutputMarkupId(true)
   */
  public AddReviewPanel(String wicketid, IProject project, IPerson reviewer,
      ReviewListPanel updatePanel) {
    super(wicketid);
    this.updatePanel = updatePanel;
    this.inputModel = new ReviewFormInputModel(project, reviewer);
    setOutputMarkupId(true);
    add(new NewReviewLink("newLink"));
    add(new AddReviewForm("reviewForm"));
  }

  /**
   * Called when the new review link is clicked. Shows the form, and hides the
   * link.
   * 
   * @param target
   *          the request target.
   */
  void onShowReviewForm(AjaxRequestTarget target) {
    formVisible = true;
    target.addComponent(this);
  }

  /**
   * Called when the cancel link is clicked. Hides the form, and shows the link.
   * 
   * @param target
   *          the request target.
   */
  void onHideReviewForm(AjaxRequestTarget target) {
    formVisible = false;
    target.addComponent(this);
  }

  /**
   * Link for adding a person described in the form to the repository.
   * 
   */
  private final class AddReviewButton extends AjaxFallbackButton {
    private static final long serialVersionUID = -3425972816770998300L;

    private AddReviewButton(String id, Form<FoafFormInputModel> form) {
      super(id, form);
    }

    /**
     * Handle the submit request by creating the person and, where appropriate,
     * assigning them to a project.
     * 
     * @param target
     *          the request target.
     */
    @Override
    public void onSubmit(AjaxRequestTarget target, Form<?> form) {
      //FIXME: inputModel.setEmail(reviewerField.getValue());
      IReview review;
	  try {
		  review = inputModel.getReview();
		  updatePanel.addReview(review);
	      onHideReviewForm(target);
	      target.addComponent(updatePanel);
	  } catch (SimalRepositoryException e) {
		logger.error("Unable to her the review from the input model", e);
	  }
    }

    protected void onError(AjaxRequestTarget target, Form<?> form) {
      target.addComponent(feedback);
    }
  }

  /** Link for cancelling a new person action. */
  @SuppressWarnings("unchecked")
  private final class CancelLink extends AjaxFallbackLink {
    private static final long serialVersionUID = 8333095362462779919L;

    public CancelLink(String id) {
      super(id);
    }

    /**
     * When the link is clicked the form is shown and the link is hidden.
     * 
     * @param target
     *          the request target.
     */
    @Override
    public void onClick(AjaxRequestTarget target) {
      onHideReviewForm(target);
    }

  }

  /**
   * Displays a form for creating a person record. The visibility of this
   * component is mutually exclusive with the visibility of the new person link.
   */
  private final class AddReviewForm extends Form<FoafFormInputModel> {
    private static final long serialVersionUID = 2931852197898067993L;

    public AddReviewForm(String id) {
      super(id, new CompoundPropertyModel<FoafFormInputModel>(inputModel));
      setOutputMarkupId(true);

      feedback = new FeedbackPanel("feedback");
      feedback.setOutputMarkupId(true);
      add(feedback);
/*
      add(personField = new RequiredTextField<String>("name"));
      personField.add(StringValidator.minimumLength(4));

      add(emailField = new RequiredTextField<String>("email"));
      emailField.add(EmailAddressValidator.getInstance());
*/
      AjaxFormValidatingBehavior.addToAllFormComponents(this, "onkeyup",
          Duration.ONE_SECOND);

      add(new AddReviewButton("addReviewButton", this));
      add(new CancelLink("cancelLink"));
    }

    @Override
    public boolean isVisible() {
      return formVisible;
    }
  }

  /**
   * Cancel addition of a person.
   * 
   * @param target
   */
  void onCancel(AjaxRequestTarget target) {
    formVisible = false;
    target.addComponent(this);
  }

  /** Link for displaying the AddReviewForm. */
  @SuppressWarnings("unchecked")
  private final class NewReviewLink extends AjaxFallbackLink {
    private static final long serialVersionUID = 1L;

    public NewReviewLink(String id) {
      super(id);
    }

    /**
     * When the link is clicked the form is shown and the link is hidden.
     * 
     * @param target
     *          the request target.
     */
    @Override
    public void onClick(AjaxRequestTarget target) {
      onShowReviewForm(target);
    }

    @Override
    public boolean isVisible() {
      return !formVisible;
    }

  }

}
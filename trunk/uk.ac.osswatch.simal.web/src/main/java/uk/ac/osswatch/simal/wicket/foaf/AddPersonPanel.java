package uk.ac.osswatch.simal.wicket.foaf;

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
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.doap.ProjectDetailPage;

/**
 * Container for adding a new person. This is an AJAX enabled container that
 * either shows the form for entering data about a person or a command link to
 * display the form.
 */
public class AddPersonPanel extends Panel {
  private static final long serialVersionUID = 8348295085251890400L;

  public static final int HELPER = 1;
  public static final int MAINTAINER = 2;
  public static final int DEVELOPER = 4;
  public static final int DOCUMENTOR = 8;
  public static final int TESTER = 16;
  public static final int TRANSLATOR = 32;  
  
  /** Visibility toggle so that either the link or the form is visible. */
  private boolean formVisible = false;
  private FoafFormInputModel inputModel = new FoafFormInputModel();
  TextField<String> nameField;
  TextField<String> emailField;
  FeedbackPanel feedback;
  private int personRole;
  private IProject project;

  /**
   * Create a new container that will initially display the command link to show
   * the form.
   * 
   * @param wicketid
   *          the id of the HTML component to host the container
   * @param role
   *          the role of any people added using this container
   * @param project
   *          the project any new people are to be assinged to
   */
  public AddPersonPanel(String wicketid, IProject project, int role) {
    super(wicketid);
    this.personRole = role;
    this.project = project;
    setOutputMarkupId(true);
    add(new NewPersonLink("newLink"));
    add(new AddPersonForm("personForm"));
  }

  /**
   * Called when the new person link is clicked. Shows the form, and hides the
   * link.
   * 
   * @param target
   *          the request target.
   */
  void onShowPersonForm(AjaxRequestTarget target) {
    formVisible = true;
    target.addComponent(this);
  }

  /**
   * Called when the cancel link is clicked. Hides the form, and shows the link.
   * 
   * @param target
   *          the request target.
   */
  void onHidePersonForm(AjaxRequestTarget target) {
    formVisible = false;
    target.addComponent(this);
  }

  /**
   * Link for adding a person described in the form to the repository.
   * 
   */
  private final class AddPersonButton extends AjaxFallbackButton {
    private static final long serialVersionUID = -3425972816770998300L;

    private AddPersonButton(String id, Form form) {
      super(id, form);
    }

    /**
     * Handle the submit request by creating the person and,
     * where appropriate, assigning them to a project.
     * 
     * @param target
     *          the request target.
     */
    @Override
    public void onSubmit(AjaxRequestTarget target, Form form) {
      inputModel.setName(nameField.getValue());
      inputModel.setEmail(emailField.getValue());
      try {
        IPerson person = inputModel.getPerson();
        if (project != null) {
          switch (personRole) {
            case MAINTAINER: project.addMaintainer(person); break;
            case DEVELOPER: project.addDeveloper(person); break;
            case TESTER: project.addTester(person); break;
            case HELPER: project.addHelper(person); break;
            case DOCUMENTOR: project.addDocumenter(person); break;
            case TRANSLATOR: project.addTranslator(person); break;
          }
        }
      } catch (SimalRepositoryException e) {
        UserReportableException error = new UserReportableException(
            "Unable to generate a person from the given form data",
            ProjectDetailPage.class, e);
        setResponsePage(new ErrorReportPage(error));
      }
      onHidePersonForm(target);
    }

    protected void onError(AjaxRequestTarget target, Form form) {
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
      onHidePersonForm(target);
    }

  }

  /** Link for displaying the AddPersonForm. */
  @SuppressWarnings("unchecked")
  private final class NewPersonLink extends AjaxFallbackLink {
    private static final long serialVersionUID = 8333095362462779919L;

    public NewPersonLink(String id) {
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
      onShowPersonForm(target);
    }

    @Override
    public boolean isVisible() {
      return !formVisible;
    }

  }

  /**
   * Displays a form for creating a person record. The visibility of this
   * component is mutually exclusive with the visibility of the new person link.
   */
  private final class AddPersonForm extends Form<FoafFormInputModel> {
    private static final long serialVersionUID = 2931852197898067993L;

    public AddPersonForm(String id) {
      super(id, new CompoundPropertyModel<FoafFormInputModel>(inputModel));
      setOutputMarkupId(true);

      feedback = new FeedbackPanel("feedback");
      feedback.setOutputMarkupId(true);
      add(feedback);

      add(nameField = new RequiredTextField<String>("name"));
      nameField.add(StringValidator.minimumLength(4));

      add(emailField = new RequiredTextField<String>("email"));
      emailField.add(EmailAddressValidator.getInstance());

      AjaxFormValidatingBehavior.addToAllFormComponents(this, "onkeyup",
          Duration.ONE_SECOND);

      add(new AddPersonButton("addPersonButton", this));
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

}
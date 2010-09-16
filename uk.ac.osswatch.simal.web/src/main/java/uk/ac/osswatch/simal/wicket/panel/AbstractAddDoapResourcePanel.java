package uk.ac.osswatch.simal.wicket.panel;

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
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract container for adding a new DOAP resource. This is an AJAX enabled 
 * container that either shows the form in the subclass or a command link to
 * display the form.
 */
public abstract class AbstractAddDoapResourcePanel extends Panel {
  private static final long serialVersionUID = -4529601431958052094L;

  public static final Logger LOGGER = LoggerFactory
      .getLogger(AbstractAddDoapResourcePanel.class);

  /** Visibility toggle so that either the link or the form is visible. */
  private boolean formVisible = false;

  FeedbackPanel feedback;

  private Panel updatePanel;

  /**
   * Create a new container that will initially display the command link to show
   * the form.
   * 
   * @param wicketid
   *          the id of the HTML component to host the container
   * @param updatePanel
   *          the panel that should be updated when the DOAP resource has been
   *          added (must have setOutputMarkupId(true)
   * @param editingAllowed 
   */
  public AbstractAddDoapResourcePanel(String wicketid, Panel updatePanel) {
    this(wicketid, updatePanel,true);
  }
    
  public AbstractAddDoapResourcePanel(String wicketid, Panel updatePanel, boolean editingAllowed) {
    super(wicketid);
    this.updatePanel = updatePanel;
    setOutputMarkupId(true);
    NewDoapResourceLink newLink = new NewDoapResourceLink("newLink");
    newLink.setVisibilityAllowed(editingAllowed);
    add(newLink);
    add(new AddDoapResourceForm("doapResourceForm"));
  }
  
  /**
   * Processes the result when the form is submitted.
   */
  protected abstract void processAddSubmit();
  
  /**
   * Add fields to the form for a specific add-form.
   * @param addDoapResourceForm
   */
  protected abstract void addFormFields(AddDoapResourceForm addDoapResourceForm);

  /**
   * Inputmodel used for this specific form. 
   * @return
   */
  public abstract Object getInputModel();

  /**
   * Called when the new DOAP resource link is clicked. Shows the form, and hides the
   * link.
   * 
   * @param target
   *          the request target.
   */
  protected void onShowForm(AjaxRequestTarget target) {
    formVisible = true;
    target.addComponent(this);
  }

  /**
   * Called when the cancel link is clicked. Hides the form, and shows the link.
   * 
   * @param target
   *          the request target.
   */
  protected void onHideForm(AjaxRequestTarget target) {
    formVisible = false;
    target.addComponent(this);
  }

  /**
   * Link for adding the DOAP resource described in the form to the repository.
   * 
   */
  private final class AddDoapResourceButton extends AjaxFallbackButton {
    private static final long serialVersionUID = -3425972816770998300L;

    private AddDoapResourceButton(String id, Form<Object> form) {
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
      processAddSubmit();
      onHideForm(target);
      target.addComponent(updatePanel);
    }

    protected void onError(AjaxRequestTarget target, Form<?> form) {
      target.addComponent(feedback);
    }
  }

  /** Link for cancelling a new DOAP resource action. */
  @SuppressWarnings("unchecked")
  private final class CancelLink extends AjaxFallbackLink {

    private static final long serialVersionUID = -1058382750336408613L;

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
      onHideForm(target);
    }

  }

  /** Link for displaying the AddDoapResourceForm. */
  @SuppressWarnings("unchecked")
  private final class NewDoapResourceLink extends AjaxFallbackLink {
    private static final long serialVersionUID = 8333095362462779919L;

    public NewDoapResourceLink(String id) {
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
      onShowForm(target);
    }

    @Override
    public boolean isVisible() {
      return !formVisible;
    }
  }

  /**
   * Displays a form for adding a DOAP resource to the project. The visibility of
   * this component is mutually exclusive with the visibility of the new link.
   */
  public final class AddDoapResourceForm extends Form<Object> {
    private static final long serialVersionUID = 2931852197898067993L;

    public AddDoapResourceForm(String id) {
      super(id, new CompoundPropertyModel<Object>(getInputModel()));

      setOutputMarkupId(true);
      addFormFields(this);

      feedback = new FeedbackPanel("feedback");
      feedback.setOutputMarkupId(true);
      add(feedback);

      AjaxFormValidatingBehavior.addToAllFormComponents(this, "onkeyup",
          Duration.ONE_SECOND);

      add(new AddDoapResourceButton("addDoapResourceButton", this));
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
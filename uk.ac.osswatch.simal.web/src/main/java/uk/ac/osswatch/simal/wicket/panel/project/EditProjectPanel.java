package uk.ac.osswatch.simal.wicket.panel.project;

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

import java.util.Iterator;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.doap.ProjectDetailPage;
import uk.ac.osswatch.simal.wicket.panel.AddCategoryPanel;
import uk.ac.osswatch.simal.wicket.panel.CategoryListPanel;
import uk.ac.osswatch.simal.wicket.panel.ReleasesPanel;

/**
 * Container for adding a new category. This is an AJAX enabled container that
 * either shows the form for selecting a category or a command link to display
 * the form.
 */
public class EditProjectPanel extends Panel {

  private static final long serialVersionUID = -7126291323723696950L;

  public static final Logger LOGGER = LoggerFactory
      .getLogger(EditProjectPanel.class);

  private IProject project;

  private boolean loggedIn;
  private boolean isReadOnly;

  /**
   * Create a new container that will initially display the command link to show
   * the form.
   * 
   * @param wicketid
   *          the id of the HTML component to host the container
   * @param updatePanel
   *          the panel that should be updated when the category has been added
   *          (must have setOutputMarkupId(true)
   */
  public EditProjectPanel(String id, IProject project, boolean loggedIn) {
    super(id);
    this.project = project;
    this.loggedIn = loggedIn;
    this.isReadOnly = true;

    add(new EditProjectForm("editProjectForm",
        new CompoundPropertyModel<IProject>(project)));
  }

  /**
   * Add category to the project and to the existing list.
   * 
   * @see uk.ac.osswatch.simal.wicket.panel.AbstractAddPanel#processAddSubmit()
   */
  protected void processAddSubmit() {

  }

  /**
   * Get a simple repeating view. Each resource in the supplied set will be
   * represented in the list using the supplied string as a label.
   * 
   * @param labelWicketID
   *          the wicket:id of the label within each list item
   * @param resources
   *          the resources to be added to the list
   * @return
   */
  private RepeatingView getRepeatingInputs(String labelWicketID,
      Set<String> labels) {
    Iterator<String> itr = labels.iterator();
    RepeatingView repeating = new RepeatingView(labelWicketID);
    TextField<String> item;
    while (itr.hasNext()) {
      item = new TextField<String>(repeating.newChildId(), new Model<String>(
          itr.next()));
      item.add(new ReadOnlyStyleBehavior());
      repeating.add(item);
    }
    return repeating;
  }

  private class EditProjectForm extends Form<IProject> {
    private static final long serialVersionUID = 5903165424353929310L;
    private TextArea<String> description;
    private AjaxFallbackButton submitButton;

    public EditProjectForm(String id, IModel<IProject> model) {
      super(id, model);
      addFormFields();
      setOutputMarkupId(true);
    }

    private void toggleEditMode() {
      isReadOnly = !isReadOnly;
      if (isReadOnly) {
        submitButton.getModel().setObject("Edit");
      } else {
        submitButton.getModel().setObject("Save");
      }
    }

    /**
     * 
     * 
     * @see uk.ac.osswatch.simal.wicket.panel.AbstractAddPanel#addFormFields(uk.ac.
     *      osswatch.simal.wicket.panel.AbstractAddPanel.AddDoapResourceForm)
     */
    private void addFormFields() {
      description = new TextArea<String>("description");
      add(description);
      description.setOutputMarkupId(true);
      description.add(new ReadOnlyStyleBehavior());

      String[] defaultValue = { project.getDescription() };
      description.setModelValue(defaultValue);

      submitButton = new AjaxFallbackButton("editProjectActionLink",
          new Model<String>("Edit"), this) {
        private static final long serialVersionUID = 384116828159132608L;

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
          toggleEditMode();
          target.addComponent(form);
        }

        @Override
        public boolean isVisible() {
          return loggedIn;
        }

      };
      add(submitButton);

      Button cancelButton = new AjaxFallbackButton("cancelProjectActionLink",
          new Model<String>("Cancel"), this) {
        private static final long serialVersionUID = -7941561946368787393L;

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
          try {
            PageParameters projectPageParams = new PageParameters();
            projectPageParams.add("simalID", project.getSimalID());
            setResponsePage(ProjectDetailPage.class, projectPageParams);
          } catch (SimalRepositoryException e) {
            setResponsePage(new ProjectDetailPage(project));
          }
        }

        @Override
        public boolean isVisible() {
          return (loggedIn && !isReadOnly);
        }

      };
      cancelButton.setDefaultFormProcessing(false);
      add(cancelButton);

      try {
        add(new ReleasesPanel("releases", project.getReleases()));
      } catch (SimalRepositoryException e) {
        UserReportableException error = new UserReportableException(
            "Unable to get project releases from the repository",
            ProjectDetailPage.class, e);
        setResponsePage(new ErrorReportPage(error));
      }

      CategoryListPanel categoryList = new CategoryListPanel("categoryList",
          project.getCategories());
      categoryList.setOutputMarkupId(true);
      add(categoryList);
      add(new AddCategoryPanel("addCategoryPanel", project, categoryList));
      add(getRepeatingInputs("OS", project.getOSes()));
      RepeatingView allLanguages = getRepeatingInputs("programmingLanguage",
          project.getProgrammingLanguages());
      allLanguages.add(new ReadOnlyStyleBehavior());
      add(allLanguages);

    }

  }

  private class ReadOnlyStyleBehavior extends AbstractBehavior {

    private static final long serialVersionUID = 9109967060661070046L;

    public void onComponentTag(final Component component, final ComponentTag tag) {

      if (isReadOnly) {
        tag.getAttributes().put("readonly", "readonly");
        tag.getAttributes().put("class", "readonly");
      } else {
        tag.getAttributes().remove("readonly");
        tag.getAttributes().put("class", "editable");
      }
    }
  }
}
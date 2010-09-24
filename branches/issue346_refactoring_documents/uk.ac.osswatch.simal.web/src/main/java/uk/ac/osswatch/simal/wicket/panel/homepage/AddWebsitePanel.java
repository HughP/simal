package uk.ac.osswatch.simal.wicket.panel.homepage;

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

import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.validation.validator.StringValidator;
import org.apache.wicket.validation.validator.UrlValidator;

import uk.ac.osswatch.simal.model.IDocument;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.doap.ProjectDetailPage;
import uk.ac.osswatch.simal.wicket.panel.AbstractAddDoapResourcePanel;

/**
 * Container for adding a new website. This is an AJAX enabled container that
 * either shows the form for entering data about a website or a command link to
 * display the form.
 */
public class AddWebsitePanel extends AbstractAddDoapResourcePanel {
  private static final long serialVersionUID = 1L;
  
  private DoapHomepageFormInputModel inputModel;
  TextField<String> nameField;
  TextField<String> urlField;
  private IProject project;

  private HomepageListPanel updatePanel;

  /**
   * Create a new container that will initially display the command link to show
   * the form.
   * 
   * @param wicketid
   *          the id of the HTML component to host the container
   * @param project
   *          the project any new people are to be assigned to
   * @param updateContainer
   *          the container that should be updated when the website has been
   *          added (must have setOutputMarkupId(true)
   */
  public AddWebsitePanel(String wicketid, IProject project,
      HomepageListPanel updatePanel) {
    super(wicketid, updatePanel);
    this.project = project;
    this.updatePanel = updatePanel;
    setOutputMarkupId(true);
  }


  /* (non-Javadoc)
   * @see uk.ac.osswatch.simal.wicket.panel.AbstractAddDoapResourcePanel#addFormFields(uk.ac.osswatch.simal.wicket.panel.AbstractAddDoapResourcePanel.AddDoapResourceForm)
   */
  @Override
  protected void addFormFields(AddDoapResourceForm addDoapResourceForm) {

    addDoapResourceForm.add(nameField = new RequiredTextField<String>("name"));
    nameField.add(StringValidator.minimumLength(4));

    addDoapResourceForm.add(urlField = new RequiredTextField<String>("url"));
    urlField.add(new UrlValidator());
  }

  /* (non-Javadoc)
   * @see uk.ac.osswatch.simal.wicket.panel.AbstractAddDoapResourcePanel#getInputModel()
   */
  @Override
  public Object getInputModel() {
    if (inputModel == null) {
      inputModel = new DoapHomepageFormInputModel();
    }
    return inputModel;
  }

  /* (non-Javadoc)
   * @see uk.ac.osswatch.simal.wicket.panel.AbstractAddDoapResourcePanel#processAddSubmit()
   */
  @Override
  protected void processAddSubmit() {
    inputModel.setName(nameField.getValue());
    inputModel.setUrl(urlField.getValue());
    try {
      IDocument website = inputModel.getHomepage();
      if (project != null) {
        project.addHomepage(website);
      }
      updatePanel.add(website);
    } catch (SimalRepositoryException e) {
      UserReportableException error = new UserReportableException(
          "Unable to generate a website from the given form data",
          ProjectDetailPage.class, e);
      setResponsePage(new ErrorReportPage(error));
    }
  }

}
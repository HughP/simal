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

import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.validation.validator.StringValidator;
import org.apache.wicket.validation.validator.UrlValidator;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IDocument;
import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.doap.ProjectDetailPage;
import uk.ac.osswatch.simal.wicket.panel.AbstractAddDoapResourcePanel;

/**
 * Container for adding any new DOAP resource that has a name and a URL. This 
 * is an AJAX enabled container that either shows the form for entering data 
 * about the resource or a command link to display the form.
 */
public class AddIResourcePanel extends AbstractAddDoapResourcePanel<IDocument> {
  
  private static final long serialVersionUID = 2783698121394400166L;
  private IDoapResourceFormInputModel inputModel;
  TextField<String> nameField;
  TextField<String> urlField;

  //private DocumentSetPanel updatePanel;

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
  public AddIResourcePanel(String wicketid, DocumentSetPanel updatePanel) {
    super(wicketid, updatePanel);
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
      inputModel = new IDoapResourceFormInputModel();
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
      IDocument homepage = SimalRepositoryFactory.getHomepageService()
      .getOrCreate(inputModel.getUrl());
      homepage.setDefaultName(inputModel.getName());
      
      getUpdatePanel().addToDisplayList(homepage);
      getUpdatePanel().addToModel(homepage);
    } catch (SimalException e) {
      UserReportableException error = new UserReportableException(
          "Unable to generate a website from the given form data",
          ProjectDetailPage.class, e);
      setResponsePage(new ErrorReportPage(error));
    }
  }

}
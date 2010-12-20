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

import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.doap.ProjectDetailPage;
import uk.ac.osswatch.simal.wicket.panel.AbstractAddDoapResourcePanel;
import uk.ac.osswatch.simal.wicket.panel.PersonListPanel;

/**
 * Container for adding a new person. This is an AJAX enabled container that
 * either shows the form for entering data about a person or a command link to
 * display the form.
 */
public class AddPersonPanel extends AbstractAddDoapResourcePanel<IPerson> {
  private static final long serialVersionUID = 8348295085251890400L;

  public static final int HELPER = 1;
  public static final int MAINTAINER = 2;
  public static final int DEVELOPER = 4;
  public static final int DOCUMENTOR = 8;
  public static final int TESTER = 16;
  public static final int TRANSLATOR = 32;

  private FoafFormInputModel inputModel;
  TextField<String> nameField;
  TextField<String> emailField;

  private int personRole;
  private IProject project;

  private PersonListPanel updatePanel;

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
  public AddPersonPanel(String wicketid, IProject project, int role,
      PersonListPanel updatePanel) {
    super(wicketid, updatePanel);
    this.personRole = role;
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

    addDoapResourceForm.add(emailField = new RequiredTextField<String>("email"));
    emailField.add(EmailAddressValidator.getInstance());
  }

  /* (non-Javadoc)
   * @see uk.ac.osswatch.simal.wicket.panel.AbstractAddDoapResourcePanel#getInputModel()
   */
  @Override
  public Object getInputModel() {
    if (inputModel == null) {
      inputModel = new FoafFormInputModel();
    }
    return inputModel;
  }

  /* (non-Javadoc)
   * @see uk.ac.osswatch.simal.wicket.panel.AbstractAddDoapResourcePanel#processAddSubmit()
   */
  @Override
  protected void processAddSubmit() {
    inputModel.setName(nameField.getValue());
    inputModel.setEmail(emailField.getValue());
    try {
      IPerson person = inputModel.getPerson();
      if (project != null) {
        switch (personRole) {
        case MAINTAINER:
          project.addMaintainer(person);
          break;
        case DEVELOPER:
          project.addDeveloper(person);
          break;
        case TESTER:
          project.addTester(person);
          break;
        case HELPER:
          project.addHelper(person);
          break;
        case DOCUMENTOR:
          project.addDocumenter(person);
          break;
        case TRANSLATOR:
          project.addTranslator(person);
          break;
        }
      }
      updatePanel.addPerson(person);
    } catch (SimalRepositoryException e) {
      UserReportableException error = new UserReportableException(
          "Unable to generate a person from the given form data",
          ProjectDetailPage.class, e);
      setResponsePage(new ErrorReportPage(error));
    }
  }

}
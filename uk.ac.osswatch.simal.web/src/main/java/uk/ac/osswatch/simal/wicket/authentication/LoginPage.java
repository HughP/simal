package uk.ac.osswatch.simal.wicket.authentication;
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

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;
import uk.ac.osswatch.simal.service.IPersonService;
import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.UserApplication;

/**
 * A page for logging into the system. Display a login form and process the
 * login request accordingly.
 * 
 * @refactor: Issue 385 replace signin panel with org.apache.wicket.authentication.panel.SignInPanel 
 */
public class LoginPage extends BasePage {
	public static final String DUPLICATE_USERNAME_ERROR = "Username not available. Please choose another.";
	public static final String MISMATCHED_PASSWORD_ERROR = "Your passwords don't match. Please try again.";
	
  private String username;
	private String password;
	private String usernameRequested;
	private String passwordRequested;
	private String passwordConfirm;

	@SuppressWarnings("unchecked")
	public LoginPage() {
		Form form;
		add(form = new Form("loginForm", new CompoundPropertyModel(this)));
		form.add(new TextField("username"));
		form.add(new PasswordTextField("password"));
		form.add(new Button("login") {
			private static final long serialVersionUID = 1L;

			public void onSubmit() {
				SimalSession sessionData = SimalSession.get();
				try {
					sessionData.authenticate(username, password);
				} catch (SimalRepositoryException e) {
					error("Problem checking user credentials, please try again in a few minutes.");
				}
				
				if (!sessionData.isAuthenticated()) {
					error("Invalid username/password");
				} else {
				  if (!getPageMap().continueToOriginalDestination()) {
				    throw new RestartResponseException(UserApplication.get().getHomePage()); 
				  }
				}
			}
		});
		form.add(new FeedbackPanel("feedback"));
		
		// Add registartion form
    Form regForm;
    add(regForm = new Form("registrationForm", new CompoundPropertyModel(this)));
    regForm.add(new TextField("usernameRequested"));
    regForm.add(new PasswordTextField("passwordRequested"));
    regForm.add(new PasswordTextField("passwordConfirm"));
    regForm.add(new Button("register") {
      private static final long serialVersionUID = 1L;

      public void onSubmit() {
        if (passwordRequested.equals(passwordConfirm)) {
          try {
            IPersonService service = SimalRepositoryFactory.getPersonService();
            IPerson user = service.findByUsername(usernameRequested);
            if (user == null) {
              String id = SimalRepositoryFactory.getPersonService().getNewID();
              String uri = RDFUtils.getDefaultPersonURI(id);
              IPerson person = service.create(uri);
              person.setUsername(usernameRequested);
              person.setPassword(passwordRequested);
              
              SimalSession.get().authenticate(usernameRequested, passwordRequested);
            } else {
              error(DUPLICATE_USERNAME_ERROR);
            }
          } catch (SimalRepositoryException e) {
            error("Problem communicating with the repository. Please try again in a few minutes.");
          } catch (DuplicateURIException e) {
            error("Problem creating your user account. Please try again in a few minutes.");
          }
        } else {
          error(MISMATCHED_PASSWORD_ERROR);
        }
        
/*
        SimalSession sessionData = SimalSession.get();
        try {
          sessionData.authenticate(username, password);
        } catch (SimalRepositoryException e) {
          error("Problem checking user credentials, please try again in a few minutes.");
        }
        
        if (!sessionData.isAuthenticated()) {
          error("Invalid username/password");
        } else {
          getPageMap().continueToOriginalDestination();
        }
        */
        
      }
    });
	}

}

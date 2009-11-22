package uk.ac.osswatch.simal.wicket;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel; /*
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
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * A page for logging into the system. Display a login form and process the
 * login request accordingly.
 */
public class LoginPage extends BasePage {
	private String username;
	private String password;

	private static Boolean authenticated = false;

	@SuppressWarnings("unchecked")
	public LoginPage() {
		Form form;
		add(form = new Form("loginForm", new CompoundPropertyModel(this)));
		form.add(new TextField("username"));
		form.add(new PasswordTextField("password"));
		form.add(new Button("login") {
			private static final long serialVersionUID = 1L;

			public void onSubmit() {
				if (username.equals("simal") && password.equals("simal")) {
					authenticated = true;
				} else {
					authenticated = false;
				}
				if (!authenticated) {
					error("Invalid username/password");
				} else {
					info("Welcome, " + username);
				}
			}
		});
		form.add(new FeedbackPanel("feedback"));
	}

	public static boolean isAuthenticated() {
		return authenticated;
	}
}

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
package uk.ac.osswatch.simal.wicket;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;

/**
 * A page for editing settings for the web application.
 */
public class SettingsPage extends BasePage {

	final static Integer SET_OHLOH_API = 1;
	
	int field = 0;
	
	/**
	 * Create a settings page that tells the user to set a particular setting and
	 * sets the focus on that field.
	 * 
	 * @param field to set the focus on
	 */
	public SettingsPage(Integer field) {
		this.field = field;
		init();
	}
	
	public void init() {
		TextField<String> ohlohApiField = new TextField<String>("ohloh.api");
		add(ohlohApiField);

		switch (field) {
		  case 1:
		    add(new Label("message", "Please set your Ohloh API in order to import data from Ohloh."));
		    ohlohApiField.add(new FocusBehaviour());
			break;
		  default:
		    add(new Label("message", ""));
		}
	}



}


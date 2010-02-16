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
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A page for editing settings for the web application.
 */
public class SettingsPage extends BasePage {
    private static final long serialVersionUID = 1L;

	final static Integer SET_OHLOH_API = 1;
	
	int field = 0;
	static TextField<String> ohlohApiField = new TextField<String>("ohlohApiKey");
	 
    private static SettingsFormInputModel inputModel = new SettingsFormInputModel();
	  
    /**
     * Create default settings page with no field receiving focus.
     */
    public SettingsPage() {
    	super();
    	init();
    }
    
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
		add(new SettingsForm("settingsForm"));
		
		switch (field) {
		  case 1:
		    add(new Label("message", "Please set your Ohloh API in order to import data from Ohloh."));
		    ohlohApiField.add(new FocusBehaviour());
			break;
		  default:
		    add(new Label("message", ""));
		}
	}



  private static class SettingsForm extends Form<SettingsFormInputModel> {
    private static final long serialVersionUID = 1L;

    public SettingsForm(String name) {
      super(name, new CompoundPropertyModel<SettingsFormInputModel>(inputModel));

	  add(ohlohApiField);
	  String[] defaultValue = { "" };
      ohlohApiField.setModelValue(defaultValue);
    }

    @Override
    protected void onSubmit() {
      super.onSubmit();
      if (!this.hasError()) {
        String apiKey = inputModel.getOhlohApiKey();
        SimalProperties.setProperty(SimalProperties.PROPERTY_OHLOH_API_KEY, apiKey);
      }
      try {
		SimalProperties.save();
		setResponsePage(new UserHomePage());
	  } catch (SimalRepositoryException e) {
		setResponsePage(new ErrorReportPage(new UserReportableException(
	              "Unable to save properties",SettingsPage.class, e)));
	  }
    }
  }


}


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


import java.rmi.server.UID;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * A panel to display a Google Gadget (or Open Social gadget).
 */
public class GadgetPanel extends Panel {
	private static final long serialVersionUID = -5887625691251320087L;
  private UID uid;
  private String specURL;
  private String appID;
   
	
	/**
	 * Create a gadget panel.
	 * 
	 * @param wicketID - the wicket ID for the panel
	 * @param specURL - the specification of the gadget
	 * @param appID - the application ID to use in open social tokens
	 */
	public GadgetPanel(String wicketID, String specURL, String appID) {
		super(wicketID);
		this.specURL = specURL;
		uid = new UID();
		setAppID(appID);
		
		AttributeAppender attr = new AttributeAppender("id", new Model(uid.toString()), " ");
		WebMarkupContainer container = new WebMarkupContainer("gadget");
		container.add(attr);
		add(container);
	}


	/**
	 * Get a unique identifier for this gadget.
	 * @return
	 */
  public String getUid() {
    return uid.toString();
  }


  /**
   * Get the gadget specification URL
   * @return
   */
  public String getSpecURL() {
    return specURL;
  }


  /**
   * Get the application identifier to be used in open social tokens.
   * @return
   */
  public String getAppID() {
    return appID;
  }


  /**
   * Set the application identifier to be used in open social tokens.
   * @param appID
   */
  public void setAppID(String appID) {
    this.appID = appID;
  }

}


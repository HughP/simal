package uk.ac.osswatch.simal.wicket.widgets;

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
import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class Widget implements Serializable{
  
  private static final long serialVersionUID = 2625929235339451020L;

  String identifier;
	String title;
	String description;
	URL icon;
	HashMap<String, WidgetInstance> instances = new HashMap<String, WidgetInstance>();

	public Widget(String identifier, String title, String description, URL icon) {
		this.identifier = identifier;
		this.title = title;
		this.description = description;
		this.icon = icon;
	}
	
	/**
	 * Get a unique identifier for this widget type.
	 * 
	 * @return
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Get the human readable title of this widget.
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Get the location of a logo for this widget.
	 * @return
	 */
	public URL getIcon() {
		return icon;
	}
	
	/**
	 * Get the description of the widget.
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Record an instance of the given widget.
	 * 
	 * @param xml description of the instance as returned by the widget server when the widget was instantiated.
	 * @return the identifier for this instance
	 */
	public WidgetInstance addInstance(Document xml) {
		Element rootEl = xml.getDocumentElement();
		String url = getElementTextContent(rootEl, "url");
		String id = getElementTextContent(rootEl, "identifier");
		String title = getElementTextContent(rootEl, "title");
		String height = getElementTextContent(rootEl, "height");
		String width = getElementTextContent(rootEl, "width");
		WidgetInstance instance = new WidgetInstance(url, id, title, height, width, this);
		instances.put(id, instance);
		
		return instance;
	}


	private String getElementTextContent(Element rootEl, String tagName) {
	  String textContent = "";
	  
	  NodeList nodeList = rootEl.getElementsByTagName(tagName);
	  if(nodeList != null && nodeList.getLength() > 0) {
	    textContent = nodeList.item(0).getTextContent();
	  }
	   
	  return textContent;
	}
	
	/**
	 * Get all instances of a widget available in this server.
	 * @return
	 */
	public Collection<WidgetInstance> getInstances() {
		return instances.values();
	}
}

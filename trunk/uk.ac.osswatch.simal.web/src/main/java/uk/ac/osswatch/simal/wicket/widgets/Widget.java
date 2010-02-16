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
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class Widget { 
	String identifier;
	String title;
	String description;
	URL icon;
	HashMap<String, Instance> instances = new HashMap<String, Instance>();

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
	public Instance addInstance(Document xml) {
		Element rootEl = xml.getDocumentElement();
		String url = rootEl.getElementsByTagName("url").item(0).getTextContent();
		String id = rootEl.getElementsByTagName("identifier").item(0).getTextContent();
		String title = rootEl.getElementsByTagName("title").item(0).getTextContent();
		String height = rootEl.getElementsByTagName("height").item(0).getTextContent();
		String width = rootEl.getElementsByTagName("width").item(0).getTextContent();
		String maximize = rootEl.getElementsByTagName("maximize").item(0).getTextContent();
		Instance instance = new Instance(url, id, title, height, width, maximize);
		instances.put(id, instance);
		
		return instance;
	}

	/**
	 * An instance of this widget.
	 *
	 */
	public static class Instance {
		String url;

		String id;
		String title;
		String height;
		String width;
		String maximize;

		public Instance(String url, String id, String title, String height,
				String width, String maximize) {
			setId(id);
			setUrl(url);
			setTitle(title);
			setHeight(height);
			setWidth(width);
			setMaximize(maximize);
		}
		
		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getHeight() {
			return height;
		}

		public void setHeight(String height) {
			this.height = height;
		}

		public String getWidth() {
			return width;
		}

		public void setWidth(String width) {
			this.width = width;
		}

		public String getMaximize() {
			return maximize;
		}

		public void setMaximize(String maximize) {
			this.maximize = maximize;
		}
	}

	/**
	 * Get all instances of a widget available in this server.
	 * @return
	 */
	public Collection<Instance> getInstances() {
		return instances.values();
	}
}

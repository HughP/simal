package uk.ac.osswatch.simal.wicket.widgets;
/*
 * Copyright 2009 University of Oxford
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.wicket.widgets.Widget.Instance;

/**
 * A connection to a Woolkie server. This maintains the necessary data for connecting
 * to the server and provides utility methods for making common calls via the Wookie
 * REST API.
 *
 */
public class WookieServerConnection implements Serializable {
	private static final long serialVersionUID = 1L;
	private String wookieURL = "http://localhost:8888/wookie";
	private HashMap<String, Widget> widgets = new HashMap<String, Widget>();
	private static final Logger logger = LoggerFactory.getLogger(WookieServerConnection.class);
	
	
	/**
	 * Get the URL of the wookie server.
	 * 
	 * @return
	 */
	public String getURL() {
		return wookieURL;
	}

	/**
	 * Get the API key for this server.
	 * 
	 * @return
	 */
	public String getApiKey() {
		return "TEST";
	}

	/**
	 * Get the user identifier for this user and server.
	 * @return
	 */
	public String getUserID() {
		return "testuser";
	}

	/**
	 * Get the shared data key for this server.
	 * 
	 * @return
	 */
	public String getSharedDatakey() {
		return "mysharedkey";
	}

	/**
	 * Get or create an instance of a widget.
	 * @param widget
	 * @return the ID of the widget instance
	 * @throws IOException 
	 */
	public Widget.Instance getOrCreateInstance(Widget widget) throws IOException {
		StringBuffer data = new StringBuffer();
		  try {
			  data.append(URLEncoder.encode("api_key", "UTF-8"));
			  data.append("=");
			  data.append(URLEncoder.encode(getApiKey(), "UTF-8"));
			  data.append("&");
			  data.append(URLEncoder.encode("userid", "UTF-8")); 
			  data.append("=");
			  data.append(URLEncoder.encode(getUserID(), "UTF-8"));
			  data.append("&");
			  data.append(URLEncoder.encode("shareddatakey", "UTF-8")); 
			  data.append("=");
			  data.append(URLEncoder.encode(getSharedDatakey(), "UTF-8"));
			  data.append("&");
			  data.append(URLEncoder.encode("widgetid", "UTF-8")); 
			  data.append("=");
			  data.append(URLEncoder.encode(widget.getIdentifier(), "UTF-8"));
		  } catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 encoding must be supported on the server", e);
		  } 
		  
	      URL url;
		  Instance instance;
		try {
			url = new URL(getURL() + "/widgetinstances");
			URLConnection conn = url.openConnection();
	        conn.setDoOutput(true);
	        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	        wr.write(data.toString());
	        wr.flush();
	        
	        InputStream is = conn.getInputStream();
			
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			instance = widget.addInstance(db.parse(is));
			
	        wr.close();
	        is.close();
		  } catch (MalformedURLException e) {
			  throw new RuntimeException("URL for supplied Wookie Server is malformed", e);
	      } catch (ParserConfigurationException e) {
			  throw new RuntimeException("Unable to configure XML parser", e);
		} catch (SAXException e) {
			  throw new RuntimeException("Problem parsing XML from Wookie Server", e);
		} 
		
		return instance;
	}

	/**
	 * Get a set of all the available widgets in the server. 
	 * If there is an error communicating with the server return an empty set, 
	 * or the set received so far in order to allow
	 * the application to proceed. The application should display an appropriate message 
	 * in this case.
	 * 
	 * @return
	 * @throws SimalException 
	 */
	public HashMap<String, Widget> getAvailableWidgets() throws SimalException {
	    try {
	        InputStream is = new URL(getURL() + "/widgets?all=true").openStream();
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document widgetsDoc = db.parse(is);
			
			Element root = widgetsDoc.getDocumentElement();
			NodeList widgetList = root.getElementsByTagName("widget");
			for (int idx = 0; idx < widgetList.getLength(); idx = idx + 1) {
				String name, identifier, desc;
				URL icon;
				Element widgetEl = (Element)widgetList.item(idx);
				name = widgetEl.getElementsByTagName("title").item(0).getTextContent();
				if (widgets.containsKey(name)) {
					break;
				}
				desc = widgetEl.getElementsByTagName("description").item(0).getTextContent();
				try {
					Node iconElement = widgetEl.getElementsByTagName("icon").item(0);
					if (iconElement != null) {
					icon = new URL(iconElement.getTextContent());
					} else {
						throw new MalformedURLException(null);
					}
				} catch (MalformedURLException e) {
					// FIXME: Create a default URL for widget logos
					icon = new URL("http://www.oss-watch.ac.uk/images/logo2.gif");
				}
				identifier = widgetEl.getAttribute("identifier");
				
				widgets.put(name, new Widget(identifier, name, desc, icon));	
			}
		} catch (ParserConfigurationException e) {
	      throw new SimalException("Unable to create XML parser", e);
		} catch (MalformedURLException e) {
		      throw new SimalException("URL for Wookie is malformed", e);
		} catch (IOException e) {
			// return an empty set, or the set received so far in order to allow
			// the application to proceed. The application should display an 
			// appropriate message in this case.
			logger.debug("Error communicating with the widget server.", e);
			return widgets;
		} catch (SAXException e) {
		      throw new SimalException("Unable to parse the response from Wookie", e);
		}
		return widgets;
	}

}

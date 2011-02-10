package uk.ac.osswatch.simal.wicket.authentication;
/*
 * Copyright 2007 University of Oxford 
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.IPersonService;
import uk.ac.osswatch.simal.wicket.SimalWebProperties;

/**
 * Stores information about the current session. This object is to be stored in the session
 * and made available for the the application. To get the data for the current session simply
 * call getUserData().
 */
public class SimalSession extends WebSession {
	private static final long serialVersionUID = 1L;
	
	private String username;
	private Boolean authenticated = false;
	
	public SimalSession(Request request) {
		super(request);
	}
	
	/**
	 * Set whether or not the current user has been authenticated.
	 * @param authenticated
	 */
	public void setAuthenticated(Boolean authenticated) {
		this.authenticated = authenticated;
	}
	
	/**
	 * Check whether the current user has been authenticated.
	 * 
	 * @return
	 */
	public Boolean isAuthenticated() {
		return this.authenticated;
	}
	
	/**
	 * Get the username of this user.
     *
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Set the username of this user.
	 * @param username2
	 */
	private void setUsername(String newUsername) {
		this.username = newUsername;
	}
	
	/**
	 * Check the username and password, if they are valid then record the user details and set
	 * to authentication. If the username and password are not valid then we record the username,
	 * but do not set as authenticated.
	 * 
	 * @param username
	 * @param password
	 * @throws SimalRepositoryException 
	 */
	public void authenticate(String username, String password) throws SimalRepositoryException {
	  // FIXME: remove admin username and password from properties file and have the account created on startup.
		String adminUsername = SimalWebProperties.getProperty(SimalWebProperties.ADMIN_USERNAME, "simal");
		String adminPassword = SimalWebProperties.getProperty(SimalWebProperties.ADMIN_PASSWORD, "simal");
		if (username.equals(adminUsername) && password.equals(adminPassword)) {
		  setUsername(username);
		  setAuthenticated(true);
		} else {
		  IPersonService service = SimalRepositoryFactory.getPersonService();
      IPerson user = service.findByUsername(username);
      if (user == null) {
        setAuthenticated(false);
      } else {
        if (user.getPassword().equals(password)) {
          setAuthenticated(true);
          setUsername(username);
        } else {
          setAuthenticated(false);
        }
      }
		}
	}
	
	/**
	 * Get the current session.
	 * 
	 * @return
	 */
	public static SimalSession get() { 
	  Session session = WebSession.get();
	  return (SimalSession)session; 
	} 
}

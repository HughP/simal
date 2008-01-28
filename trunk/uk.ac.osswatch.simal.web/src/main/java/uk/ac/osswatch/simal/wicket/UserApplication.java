/*
 * Copyright 2007 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.osswatch.simal.wicket;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.wicket.protocol.http.WebApplication;

import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * The UserApp is the main user facing appliation. This application
 * allows users to view Simal registries.
 */
public class UserApplication extends WebApplication {
	private static SimalRepository repo;
	// When isTest is set to true the repo is populated with test data
	private static boolean isTest = true;
	
    public UserApplication() {
    	// FIXME: when we go to a non-volatile repo we need to set
    	// When isTest is set to true the repo is populated with test data
    	// isTest = false;
    }

    public void init() {
    
    }

	@Override
	@SuppressWarnings("unchecked")
	public Class getHomePage() {
		return UserHomePage.class;
	}

	/**
	 * Get the repository that stores all the data.
	 * @return
	 */
	public static SimalRepository getRepository() {
		if (repo == null) {
	    	try {
				repo = new SimalRepository();
			} catch (SimalRepositoryException e) {
				throw new RuntimeException("Unable to create repository", e);
			}
		}
		return repo;
	}

}
  
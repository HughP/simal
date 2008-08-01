package uk.ac.osswatch.simal.wicket;
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


import javax.xml.namespace.QName;

import org.apache.wicket.protocol.http.WebApplication;

import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryFactory;

/**
 * The UserApp is the main user facing appliation. This application allows users
 * to view Simal registries.
 */
public class UserApplication extends WebApplication {
	/**
	 * The qname for the project to use if no other project is specified.
	 */
	public static final String DEFAULT_PROJECT_URI = 
			"http://simal.oss-watch.ac.uk/simalTest#";

  public static final String DEFAULT_PERSON_URI = "http://simal.oss-watch.ac.uk/foaf/Jane%20Blogs%20Maintainer#Person";

	private static ISimalRepository repository;

  private static boolean isTest;

	public UserApplication() {
		init();
	}

	public void init() {
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class getHomePage() {
		return UserHomePage.class;
	}
	
	/**
	 * Get the repository for this application. If the repository
	 * has not been initialised yet then create and initialise it
	 * first.
	 * 
	 * @return
	 * @throws SimalRepositoryException
	 */
	public static ISimalRepository getRepository() throws SimalRepositoryException {
		if (repository == null) {
			repository = SimalRepositoryFactory.getInstance();
			repository.setIsTest(isTest);
			if (!repository.isInitialised()) {
				repository.initialise();
			}
		}
		return repository;
	}

	/**
	 * If IsTest is set to true then a test (in memory)
	 * repository will be used, othersiwe a real repository
	 * is usef.
	 * 
	 * @param value
	 */
  public static void setIsTest(boolean value) {
    isTest = value;
  }

}

package uk.ac.osswatch.simal.wicket;

import uk.ac.osswatch.simal.SimalProperties;
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
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A class for accessing and managing SimalWebProperties.
 * 
 *  @REFACTOR currently this class extends the core SimalProperties class.
 *  This means that all web related proeprties are stored in the same location
 *  as the core properties, this is not ideal. 
 *  See http://code.google.com/p/simal/issues/detail?id=118
 */
public class SimalWebProperties extends SimalProperties {
	public final static String ADMIN_USERNAME = "simal.web.admin.username";
	public final static String ADMIN_PASSWORD = "simal.web.admin.password";
	
	public SimalWebProperties() throws SimalRepositoryException {
		super();
	}
}

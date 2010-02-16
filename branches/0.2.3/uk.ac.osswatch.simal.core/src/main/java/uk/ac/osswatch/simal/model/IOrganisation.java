package uk.ac.osswatch.simal.model;

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
import java.util.Set;

/**
 * An organisation as represented by a foaf:organisation element.
 */
public interface IOrganisation extends IFoafResource {
	
	  /**
	   * Get the default name for this organisation.
	   */
	  public String getName();

	/**
	 * Add a name to the organisation. Each organisation may have more than one name.
	 * 
	 * @param name
	 */
	public void addName(String name);

	/**
	 * Add a current poject to this organisation.
	 * 
	 * @param string
	 */
	public void addCurrentProject(String string);

	/**
	 * Get all the current projects for this organisation.
	 * 
	 * @return
	 */
	public Set<IProject> getCurrentProjects();
}

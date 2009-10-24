package uk.ac.osswatch.simal.model.jcr;
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
import java.util.HashSet;
import java.util.Set;

import uk.ac.osswatch.simal.model.IOrganisation;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class Organisation extends Resource implements IOrganisation {
	private static final long serialVersionUID = 1L;
	private Set<String> names = new HashSet<String>();
	
	public Organisation() {
		super();
	}
	
	public Organisation(String simalID) throws SimalRepositoryException {
		super(simalID);
	}

	public void addCurrentProject(String string) {
		// TODO Auto-generated method stub

	}

	public void addName(String name) {
		this.names.add(name);
	}

	public Set<IProject> getCurrentProjects() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}

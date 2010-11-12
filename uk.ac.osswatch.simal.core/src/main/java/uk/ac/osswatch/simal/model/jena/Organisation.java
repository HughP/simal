package uk.ac.osswatch.simal.model.jena;
/*
 * 
 Copyright 2007,2010 University of Oxford * 
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
import java.util.Iterator;
import java.util.Set;

import uk.ac.osswatch.simal.model.Foaf;
import uk.ac.osswatch.simal.model.IOrganisation;
import uk.ac.osswatch.simal.model.IProject;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;

public class Organisation extends FoafResource implements IOrganisation {

	public Organisation(com.hp.hpl.jena.rdf.model.Resource resource) {
		super(resource);
	}

	private static final long serialVersionUID = 1L;

	public void addCurrentProject(String uri) {
	    Model model = getJenaResource().getModel();
	    com.hp.hpl.jena.rdf.model.Resource r = model.getResource(uri);
	    Statement statement = model.createStatement(
	        getJenaResource(),
	        FOAF.currentProject, r);
	    model.add(statement);
	}

	public Set<IProject> getCurrentProjects() {
	    Iterator<Statement> itr = listProperties(Foaf.CURRENT_PROJECT).iterator();
	    Set<IProject> projects = new HashSet<IProject>();
	    while (itr.hasNext()) {
	      projects.add(new Project(itr.next().getResource()));
	    }
	    return projects;
	}

}

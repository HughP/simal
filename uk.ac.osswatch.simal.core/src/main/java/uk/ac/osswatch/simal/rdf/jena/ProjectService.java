package uk.ac.osswatch.simal.rdf.jena;
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

import uk.ac.osswatch.simal.model.Doap;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.jena.Project;
import uk.ac.osswatch.simal.rdf.IProjectService;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * A class for working with projects in the repository.
 * 
 */
public class ProjectService extends AbstractService implements IProjectService {

	ProjectService(SimalRepository simalRepository) {
		setRepository(simalRepository);
	};
	
	public Set<IProject> getProjectsWithRCS() throws SimalRepositoryException {
		return getProjectsWith(Doap.REPOSITORY);
	}

	public Set<IProject> getProjectsWithHomepage() throws SimalRepositoryException {
		return getProjectsWith(Doap.HOMEPAGE);
	}

	public Set<IProject> getProjectsWithMaintainer() throws SimalRepositoryException {
		return getProjectsWith(Doap.MAINTAINER);
	}

	public Set<IProject> getProjectsWithMailingList() throws SimalRepositoryException {
		return getProjectsWith(Doap.MAILING_LIST);
	}

	public Set<IProject> getProjectsWithBugDatabase() throws SimalRepositoryException {
		return getProjectsWith(Doap.BUG_DATABASE);
	}

	public Set<IProject> getProjectsWithRelease() throws SimalRepositoryException {
		return getProjectsWith(Doap.RELEASE);
	}
	
	private Set<IProject> getProjectsWith(Property property) throws SimalRepositoryException {
		String queryStr = "PREFIX simal: <" + SimalRepository.SIMAL_NAMESPACE_URI
        + "> " + "PREFIX rdf: <" + SimalRepository.RDF_NAMESPACE_URI + "> "
        + "PREFIX rdfs: <" + SimalRepository.RDFS_NAMESPACE_URI + ">"
        + "PREFIX doap: <" + Doap.NS + ">"
        + "SELECT DISTINCT ?project WHERE { {" + "?project a simal:Project . "
        + "?project <" + property.getURI() + "> ?value }"
        + " UNION "
        + "{ ?doapProject a doap:Project . "
        + "?doapProject <" + property.getURI() + "> ?value . "
        + " ?project a simal:Project . "
        + " ?project rdfs:seeAlso ?doapProject } }";

	    return findProjectsBySPARQL(queryStr);
	}


	  public IProject getProject(String uri) throws SimalRepositoryException {
		if(uri.startsWith(RDFUtils.PROJECT_NAMESPACE_URI)) {
		    if (containsProject(uri)) {
		      return new Project(getRepository().getModel().getResource(uri));
		    } else {
		      return null;
		    }
		} else {
			return findProjectBySeeAlso(uri);
		}
	  }
	  
	  public boolean containsProject(String uri) {
		  Model model = getRepository().getModel();
		    Property o = model.createProperty("http://usefulinc.com/ns/doap#Project");
		    com.hp.hpl.jena.rdf.model.Resource r = model.createResource(uri);
		    Statement doap = model.createStatement(r, RDF.type, o);

		    o = model.createProperty(RDFUtils.SIMAL_PROJECT);
		    Statement simal = model.createStatement(r, RDF.type, o);
		    return model.contains(doap) || model.contains(simal);
		  }


	  public IProject findProjectBySeeAlso(String seeAlso)
	      throws SimalRepositoryException {
		String queryStr = "PREFIX simal: <" + SimalRepository.SIMAL_NAMESPACE_URI
	        + "> " + "PREFIX rdf: <" + SimalRepository.RDF_NAMESPACE_URI + "> "
	        + "PREFIX rdfs: <" + SimalRepository.RDFS_NAMESPACE_URI + ">"
	        + "SELECT DISTINCT ?project WHERE { " + "?project a simal:Project . "
	        + "?project rdfs:seeAlso <" + seeAlso + ">}";

	    Set<IProject> projects = findProjectsBySPARQL(queryStr);

	    if (projects.size() == 0) {
	    	return null;
	    } else {
	      return (IProject) projects.toArray()[0];
	    }
	  }  
	  

	  /**
	   * Find all projects returned using a SPARQL query.
	   * 
	   * @param queryStr
	   * @return
	   */
	  public Set<IProject> findProjectsBySPARQL(String queryStr) {
		Model model = getRepository().getModel();
	    Query query = QueryFactory.create(queryStr);
	    QueryExecution qe = QueryExecutionFactory.create(query, model);
	    ResultSet results = qe.execSelect();
		
	    Set<IProject> projects = new HashSet<IProject>();
	    while (results.hasNext()) {
	      QuerySolution soln = results.nextSolution();
	      RDFNode node = soln.get("project");
	      if (node.isResource()) {
	        projects.add(new Project((com.hp.hpl.jena.rdf.model.Resource) node));
	      }
	    }
	    qe.close();
	    return projects;
	  }

	public Set<IProject> getProjectsWithReview() throws SimalRepositoryException {
		String queryStr = "PREFIX simal: <" + SimalRepository.SIMAL_NAMESPACE_URI
        + "> " + "PREFIX rdf: <" + SimalRepository.RDF_NAMESPACE_URI + "> "
        + "PREFIX rdfs: <" + SimalRepository.RDFS_NAMESPACE_URI + ">"
        + "PREFIX doap: <" + Doap.NS + ">"
        + "SELECT DISTINCT ?project WHERE {" 
        + "?review a simal:Review . "
        + "?review simal:Project ?project }";
		
	    return findProjectsBySPARQL(queryStr);
	}

}

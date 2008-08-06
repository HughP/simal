package uk.ac.osswatch.simal.model.jena;

/*
 * 
 Copyright 2007 University of Oxford * 
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

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;

import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.SimalOntology;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.jena.SimalRepository;

public class Category extends DoapResource implements IDoapCategory {
  private static final long serialVersionUID = -1351331995566931903L;

  public Category(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
  }

  public Set<IProject> getProjects() {
    String queryStr = "PREFIX doap: <" + SimalRepository.DOAP_NAMESPACE_URI
        + "> " + "PREFIX rdf: <" + SimalRepository.RDF_NAMESPACE_URI + ">"
        + "PREFIX simal: <" + SimalRepository.SIMAL_NAMESPACE_URI + ">"
        + "SELECT DISTINCT ?project WHERE { "
        + "?project doap:category <" + getURI() + ">}";
    Query query = QueryFactory.create(queryStr);
    QueryExecution qe = QueryExecutionFactory.create(query, getJenaResource().getModel());
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

  public String getSimalID() {
    Statement idStatement = getJenaResource().getProperty(SimalOntology.CATEGORY_ID);
    if (idStatement != null) {
      return idStatement.getString();
    } else {
      return null;
    }
  }

  public void setSimalID(String newID) {
    getJenaResource().addLiteral(SimalOntology.CATEGORY_ID, newID);
  }

  public Set<IPerson> getPeople() throws SimalRepositoryException {
    Iterator<IProject> projects = getProjects().iterator();
    HashSet<IPerson> people = new HashSet<IPerson>();
    while(projects.hasNext()) {
      people.addAll(projects.next().getAllPeople());
    }
    return people;
  }
}

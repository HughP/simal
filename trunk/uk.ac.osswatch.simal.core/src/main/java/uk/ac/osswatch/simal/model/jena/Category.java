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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.SimalOntology;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;
import uk.ac.osswatch.simal.rdf.jena.SimalRepository;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;

public class Category extends DoapResource implements IDoapCategory {
  private static final long serialVersionUID = -1351331995566931903L;
  private static final Logger logger = LoggerFactory.getLogger(Category.class);

  public Category(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
  }

  public Set<IProject> getProjects() {
    String queryStr = "PREFIX doap: <" + RDFUtils.DOAP_NS
        + "> " + "PREFIX rdf: <" + SimalRepository.RDF_NAMESPACE_URI + ">"
        + "PREFIX simal: <" + SimalRepository.SIMAL_NAMESPACE_URI + ">"
        + "SELECT DISTINCT ?project WHERE { " + "?project doap:category <"
        + getURI() + ">}";
    Query query = QueryFactory.create(queryStr);
    QueryExecution qe = QueryExecutionFactory.create(query, getJenaResource()
        .getModel());
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

  public String getSimalID() throws SimalRepositoryException {
    String uniqueID = getUniqueSimalID();
    String id = uniqueID.substring(uniqueID.lastIndexOf("-") + 1);
    return id;
  }

  public String getUniqueSimalID() throws SimalRepositoryException {
    String id;
    Statement idStatement = getJenaResource().getProperty(
        SimalOntology.CATEGORY_ID);
    if (idStatement == null) {
      
      logger.warn("Category instance with no Simal ID - " + getURI());
      id = null;
    } else {
      id = idStatement.getString();
    }
    return id;
  }

  public void setSimalID(String newId) throws SimalRepositoryException {
    if (newId.contains(":")
        && !newId.startsWith(SimalProperties
            .getProperty((SimalProperties.PROPERTY_SIMAL_INSTANCE_ID)))) {
      throw new SimalRepositoryException("Simal ID cannot contain a '-'");
    }
    StringBuilder id = new StringBuilder(SimalProperties
        .getProperty(SimalProperties.PROPERTY_SIMAL_INSTANCE_ID));
    id.append("-");
    id.append(newId);
    logger.info("Setting simalId for " + this + " to " + id);
    getJenaResource().addLiteral(SimalOntology.CATEGORY_ID, id);
  }

  public Set<IPerson> getPeople() {
	  // FIXME: this would be much faster if it used a SPARQL query -but what would the query be?
    Iterator<IProject> projects = getProjects().iterator();
    HashMap<String, IPerson> allPeople = new HashMap<String, IPerson>();
    while (projects.hasNext()) {
      Iterator<IPerson> people = projects.next().getAllPeople().iterator();
      while (people.hasNext()) {
    	  IPerson person = people.next();
    	  if (!allPeople.containsKey(person.getURI())) {
    		  allPeople.put(person.getURI(), person);
    	  }
      }
    }
    return new HashSet<IPerson>(allPeople.values());
  }
}

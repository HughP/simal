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
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.Doap;
import uk.ac.osswatch.simal.model.Foaf;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IInternetAddress;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.SimalOntology;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class Person extends Resource implements IPerson {

  private static final Logger logger = LoggerFactory.getLogger(Person.class);

  public Person(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
  }

  public Set<IPerson> getColleagues() throws SimalRepositoryException {
    String uri = getURI();
    String queryStr = "PREFIX foaf: <" + Foaf.NS + "> " + "PREFIX doap: <"
        + Doap.NS + "> " + "PREFIX rdf: <" + ISimalRepository.RDF_NAMESPACE_URI
        + "> " + "SELECT DISTINCT ?colleague WHERE { "
        + "?project rdf:type doap:Project . " + "{?project doap:maintainer <"
        + uri + "> } UNION " + "{?project doap:developer <" + uri
        + "> } UNION " + "{?project doap:documentor <" + uri + "> } UNION "
        + "{?project doap:helper <" + uri + "> } UNION "
        + "{?project doap:tester <" + uri + "> } UNION "
        + "{?project doap:translator <" + uri + "> } . "
        + "{?project doap:developer ?colleague} UNION "
        + "{?project doap:documenter ?colleague} UNION "
        + "{?project doap:helper ?colleague} UNION"
        + "{?project doap:maintainer ?colleague} UNION"
        + "{?project doap:tester ?colleague} UNION"
        + "{?project doap:translator ?colleague} " + "}";
    logger.debug(("Executing SPARQL query:\n" + queryStr));
    Query query = QueryFactory.create(queryStr);
    QueryExecution qe = QueryExecutionFactory.create(query, getJenaResource()
        .getModel());
    ResultSet results = qe.execSelect();

    Set<IPerson> colleagues = new HashSet<IPerson>();
    IPerson colleague;
    while (results.hasNext()) {
      QuerySolution soln = results.nextSolution();
      RDFNode node = soln.get("colleague");
      if (node.isResource()) {
        colleague = new Person((com.hp.hpl.jena.rdf.model.Resource) node);
        String id = colleague.getSimalID();
        if (!id.equals(getSimalID())) {
          colleagues.add(colleague);
        }
      }
    }
    qe.close();

    return colleagues;
  }

  public Set<IInternetAddress> getEmail() {
    StmtIterator itr = getJenaResource().listProperties(Foaf.MBOX);
    Set<IInternetAddress> emails = new HashSet<IInternetAddress>();
    while (itr.hasNext()) {
      emails.add(new InternetAddress(itr.nextStatement().getResource()));
    }
    return emails;
  }

  public Set<String> getGivennames() {
    StmtIterator itr = getJenaResource().listProperties(Foaf.GIVENNAME);
    Set<String> names = new HashSet<String>();
    while (itr.hasNext()) {
      names.add(itr.nextStatement().getString());
    }
    return names;
  }

  public Set<String> getNames() {
    StmtIterator itr = getJenaResource().listProperties(Foaf.NAME);
    Set<String> names = new HashSet<String>();
    while (itr.hasNext()) {
      names.add(itr.nextStatement().getString());
    }
    return names;
  }

  public Set<String> getFirstnames() {
    StmtIterator itr = getJenaResource().listProperties(Foaf.FIRST_NAME);
    Set<String> names = new HashSet<String>();
    while (itr.hasNext()) {
      names.add(itr.nextStatement().getString());
    }
    return names;
  }

  public Set<IDoapHomepage> getHomepages() {
    StmtIterator itr = getJenaResource().listProperties(Foaf.HOMEPAGE);
    Set<IDoapHomepage> homepages = new HashSet<IDoapHomepage>();
    while (itr.hasNext()) {
      homepages.add(new Homepage(itr.nextStatement().getResource()));
    }
    return homepages;
  }

  public Set<IPerson> getKnows() {
    StmtIterator itr = getJenaResource().listProperties(Foaf.KNOWS);
    Set<IPerson> people = new HashSet<IPerson>();
    while (itr.hasNext()) {
      people.add(new Person(itr.nextStatement().getResource()));
    }
    return people;
  }

  public String getSimalID() {
    Statement idStatement = getJenaResource().getProperty(SimalOntology.PERSON_ID);
    return idStatement.getString();
  }

  public void setSimalID(String newId) {
    getJenaResource().addLiteral(SimalOntology.PERSON_ID, newId);
  }

  /**
   * Get the label for this person. The label for a person is derived from their
   * known names. If the person does not have any defined names then the
   * toString() method is used..
   * 
   * @return
   */
  public String getLabel() {
    Set<String> names = getNames();
    if (names.size() == 0) {
      names = getGivennames();
    }
    if (names == null) {
      return toString();
    } else {
      if (names.size() == 0) {
        return getURI();
      }
      return (String) names.toArray()[0];
    }
  }

  public Set<IProject> getProjects() {
    String uri = getURI();
    String queryStr = "PREFIX foaf: <" + Foaf.NS + "> " + "PREFIX doap: <"
        + Doap.NS + "> " + "PREFIX rdf: <" + ISimalRepository.RDF_NAMESPACE_URI
        + "> " + "SELECT DISTINCT ?project WHERE { "
        + "?project rdf:type doap:Project . " + "{?project doap:maintainer <"
        + uri + "> } UNION " + "{?project doap:developer <" + uri
        + "> } UNION " + "{?project doap:documentor <" + uri + "> } UNION "
        + "{?project doap:helper <" + uri + "> } UNION "
        + "{?project doap:tester <" + uri + "> } UNION "
        + "{?project doap:translator <" + uri + "> }}";
    logger.debug(("Executing SPARQL query:\n" + queryStr));
    Query query = QueryFactory.create(queryStr);
    QueryExecution qe = QueryExecutionFactory.create(query, getJenaResource()
        .getModel());
    ResultSet results = qe.execSelect();

    Set<IProject> projects = new HashSet<IProject>();
    IProject project;
    while (results.hasNext()) {
      QuerySolution soln = results.nextSolution();
      RDFNode node = soln.get("project");
      if (node.isResource()) {
        project = new Project((com.hp.hpl.jena.rdf.model.Resource) node);
        String id = project.getSimalID();
        if (!id.equals(getSimalID())) {
          projects.add(project);
        }
      }
    }
    qe.close();

    return projects;
  }

}

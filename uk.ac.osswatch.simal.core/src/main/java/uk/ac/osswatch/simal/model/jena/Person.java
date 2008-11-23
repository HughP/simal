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

import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.model.Doap;
import uk.ac.osswatch.simal.model.Foaf;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IInternetAddress;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.model.SimalOntology;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;
import uk.ac.osswatch.simal.rdf.jena.SimalRepository;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;

public class Person extends Resource implements IPerson {
  private static final long serialVersionUID = -6510798839142810644L;
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

    if (names.size() == 0) {
      names = getGivennames();
    }

    if (names.size() == 0) {
      names.add("No Name Supplied");
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

  public String getSimalID() throws SimalRepositoryException {
    String uniqueID = getUniqueSimalID();
    String id = uniqueID.substring(uniqueID.lastIndexOf(":") + 1);
    return id;
  }

  public String getUniqueSimalID() throws SimalRepositoryException {
    String id;
    Statement idStatement = getJenaResource().getProperty(
        SimalOntology.PERSON_ID);
    if (idStatement == null) {
      id = SimalRepository.getInstance().getNewPersonID();
      setSimalID(id);
    } else {
      id = idStatement.getString();
    }
    return id;
  }

  public void setSimalID(String newId) throws SimalRepositoryException {
    if (newId.contains(":")
        && !newId.startsWith(SimalProperties
            .getProperty((SimalProperties.PROPERTY_SIMAL_INSTANCE_ID)))) {
      throw new SimalRepositoryException("Simal ID cannot contain a ':'");
    }
    StringBuilder id = new StringBuilder(SimalProperties
        .getProperty(SimalProperties.PROPERTY_SIMAL_INSTANCE_ID));
    id.append(":");
    id.append(newId);
    logger.info("Setting simalId for " + this + " to " + id);
    getJenaResource().addLiteral(SimalOntology.PERSON_ID, id);
  }

  /**
   * Get the label for this person. The label for a person is derived from their
   * known names (if more than one is known then the longest is used). If the
   * person does not have any defined names then the toString() method is used..
   * 
   * @return
   */
  public String getLabel() {
    Set<String> names = getNames();
    if (names.size() == 0) {
      names = getGivennames();
      if (names.size() == 0) {
        return getURI();
      }
    }
    int maxLength = 0;
    String name = null;
    Object[] arr = names.toArray();
    for (int i = 0; i < names.size(); i++) {
      String curName = (String) arr[i];
      if (curName.length() > maxLength) {
        name = curName;
        maxLength = curName.length();
      }
    }
    return name;
  }

  public Set<IProject> getProjects() throws SimalRepositoryException {
    StmtIterator itr = getJenaResource().listProperties(Foaf.CURRENT_PROJECT);
    Set<IProject> projects = new HashSet<IProject>();
    while (itr.hasNext()) {
      projects.add(new Project(itr.nextStatement().getResource()));
    }
    return projects;
  }

  public String toJSONRecord() throws SimalRepositoryException {
    StringBuffer json = new StringBuffer();
    json.append("{");
    json.append(toJSONRecordContent());
    json.append("}");
    return json.toString();
  }

  protected String toJSONRecordContent() throws SimalRepositoryException {
    StringBuffer json = new StringBuffer();
    json.append("\"id\":\"" + getURI() + "\",");
    json.append("\"simalID\":\"" + getSimalID() + "\",");
    json.append("\"label\":\"" + getLabel() + "\",");
    json.append("\"name\":\"" + getNames() + "\"");
    Set<IProject> projects = getProjects();
    json.append(", \"project\":" + toJSONValues(projects));
    Iterator<IProject> itr = projects.iterator();
    Set<IDoapCategory> categories = new HashSet<IDoapCategory>();
    while (itr.hasNext()) {
      categories.addAll(itr.next().getCategories());
    }
    json.append(", \"category\":" + toJSONValues(categories));

    return json.toString();
  }

  /**
   * Given a set of DOAP resources return a JSON representation of those
   * resources.
   * 
   * @param resources
   * @return
   */
  private String toJSONValues(Set<?> resources) {
    if (resources == null) {
      return null;
    }
    StringBuffer values = new StringBuffer();
    Iterator<?> itr = resources.iterator();
    Object resource;
    values.append('[');
    while (itr.hasNext()) {
      resource = itr.next();
      if (resource instanceof IResource) {
        String label = ((IResource) resource).getLabel();
        values.append("\"" + StringEscapeUtils.escapeJavaScript(label.trim())
            + "\"");
      } else {
        values.append("\""
            + StringEscapeUtils.escapeJavaScript(resource.toString().trim())
            + "\"");
      }
      if (itr.hasNext()) {
        values.append(", ");
      }
    }
    values.append(']');
    return values.toString();
  }

  /**
   * @deprecated use addName(name) (scheduled for removal in 0.3)
   */
  public void setName(String name) {
    addName(name);
  }

  public void addName(String name) {
    getJenaResource().addProperty(FOAF.name, name);
  }

  public Set<String> getSHA1Sums() {
    StmtIterator itr = getJenaResource().listProperties(Foaf.MBOX_SHA1SUM);
    Set<String> hashes = new HashSet<String>();
    while (itr.hasNext()) {
      hashes.add(itr.nextStatement().getString());
    }
    return hashes;
  }

  public void addEmail(String email) {
    if (!email.startsWith("mailto:")) {
      email = "mailto:" + email;
    }
    Model model = getJenaResource().getModel();
    Statement statement = model.createStatement(getJenaResource(), FOAF.mbox,
        model.createResource(email));
    model.add(statement);
    try {
      addSHA1Sum(RDFUtils.getSHA1(email));
    } catch (NoSuchAlgorithmException e) {
      logger.warn("Unable to add the SHA1 has of an email address", e);
    }
  }

  public void addSHA1Sum(String sha1) {
    Model model = getJenaResource().getModel();
    Statement statement = model.createStatement(getJenaResource(),
        FOAF.mbox_sha1sum, sha1);
    model.add(statement);
  }

}

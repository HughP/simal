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
import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.Foaf;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IInternetAddress;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.model.simal.SimalOntology;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;
import uk.ac.osswatch.simal.rdf.jena.SimalRepository;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class Person extends Resource implements IPerson {
  private static final long serialVersionUID = -6510798839142810644L;
  private static final Logger logger = LoggerFactory.getLogger(Person.class);

  public Person(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
  }

  public Set<IPerson> getColleagues() throws SimalRepositoryException {
    Set<IPerson> colleagues = new HashSet<IPerson>();
    Set<String> colleagueIDs = new HashSet<String>();
    
    Iterator<IProject> projects = getProjects().iterator();
    while (projects.hasNext()) {
      IProject project = projects.next();
      Iterator<IPerson> people = project.getAllPeople().iterator();
      while (people.hasNext()) {
        IPerson colleague = people.next();
        String id = colleague.getSimalID();
        if (!id.equals(this.getSimalID()) && !colleagueIDs.contains(id)) {
          colleagues.add(colleague);
          colleagueIDs.add(id);
        }
      }
    }

    return colleagues;
  }

  public Set<IInternetAddress> getEmail() {
    Iterator<Statement> itr = listProperties(Foaf.MBOX).iterator();
    Set<IInternetAddress> emails = new HashSet<IInternetAddress>();
    while (itr.hasNext()) {
      emails.add(new InternetAddress(itr.next().getResource()));
    }
    return emails;
  }

  public Set<String> getGivennames() {
    Iterator<Statement> itr = listProperties(Foaf.GIVENNAME).iterator();
    Set<String> names = new HashSet<String>();
    while (itr.hasNext()) {
      names.add(itr.next().getString());
    }
    return names;
  }

  public Set<String> getNames() {
    Iterator<Statement> itr = listProperties(Foaf.NAME).iterator();
    Set<String> names = new HashSet<String>();
    while (itr.hasNext()) {
      names.add(itr.next().getString());
    }

    if (names.size() == 0) {
      names = getGivennames();
    }

    if (names.size() == 0) {
      names.add("No Name Supplied");
      try {
        logger.warn("There is a person with no name attached to projects " + getProjects().toString());
      } catch (SimalRepositoryException e) {
        e.printStackTrace();
      }
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
    Iterator<Statement> itr = listProperties(Foaf.HOMEPAGE).iterator();
    Set<IDoapHomepage> homepages = new HashSet<IDoapHomepage>();
    while (itr.hasNext()) {
      homepages.add(new Homepage(itr.next().getResource()));
    }
    return homepages;
  }

  public Set<IPerson> getKnows() {
    Iterator<Statement> itr = listProperties(Foaf.KNOWS).iterator();
    Set<IPerson> people = new HashSet<IPerson>();
    while (itr.hasNext()) {
      people.add(new Person(itr.next().getResource()));
    }
    return people;
  }

  public String getSimalID() throws SimalRepositoryException {
    String uniqueID = getUniqueSimalID();
    String id = uniqueID.substring(uniqueID.lastIndexOf("-") + 1);
    return id;
  }

  public String getUniqueSimalID() throws SimalRepositoryException {
    String id;
    Statement idStatement = getJenaResource().getProperty(
        SimalOntology.PERSON_ID);
    if (idStatement == null) {
      id = SimalRepositoryFactory.getInstance().getNewPersonID();
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
	    String name = getLiteralValue(Foaf.NAME);
	    
	    if (name == null) {
	      name = getLiteralValue(Foaf.GIVENNAME);
	    }
	    
	    if (name == null) {
	      name = getLiteralValue(RDFS.label);
	    }
	    
	    if (name == null) {
	      return getURI();
	    }
	    return name;
  }

  public Set<IProject> getProjects() throws SimalRepositoryException {
    Iterator<Statement> itr = listProperties(Foaf.CURRENT_PROJECT).iterator();
    Set<IProject> projects = new HashSet<IProject>();
    while (itr.hasNext()) {
      projects.add(new Project(itr.next().getResource()));
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

	public void removeName(String name) throws SimalRepositoryException {
	    Model model = getJenaResource().getModel();
	    StmtIterator statements = model.listStatements(getJenaResource(), Foaf.NAME, name);
	    if (statements == null) {
	    	throw new SimalRepositoryException("Name does not exist in resource, cannot remove: " + name);
	    }
	    model.remove(statements);
	}

}

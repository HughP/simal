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


import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.Doap;
import uk.ac.osswatch.simal.model.IDoapBugDatabase;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IDoapDownloadMirror;
import uk.ac.osswatch.simal.model.IDoapDownloadPage;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IDoapLicence;
import uk.ac.osswatch.simal.model.IDoapMailingList;
import uk.ac.osswatch.simal.model.IDoapRelease;
import uk.ac.osswatch.simal.model.IDoapRepository;
import uk.ac.osswatch.simal.model.IDoapResource;
import uk.ac.osswatch.simal.model.IDoapScreenshot;
import uk.ac.osswatch.simal.model.IDoapWiki;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.SimalOntology;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class Project extends DoapResource implements IProject {

  /**
   * 
   */
  private static final long serialVersionUID = 1960364043645152134L;
  private static final Logger logger = LoggerFactory.getLogger(Project.class);

  public Project(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
  }

  public HashSet<IPerson> getAllPeople() throws SimalRepositoryException {
    HashSet<IPerson> people = new HashSet<IPerson>();
    people.addAll((Collection<IPerson>) getMaintainers());
    people.addAll((Collection<IPerson>) getDevelopers());
    people.addAll((Collection<IPerson>) getDocumenters());
    people.addAll((Collection<IPerson>) getHelpers());
    people.addAll((Collection<IPerson>) getTesters());
    people.addAll((Collection<IPerson>) getTranslators());
    return people;

  }

  public Set<IDoapCategory> getCategories() {
    StmtIterator itr = jenaResource.listProperties(Doap.CATEGORY);
    Set<IDoapCategory> cats = new HashSet<IDoapCategory>();
    while (itr.hasNext()) {
      cats.add(new Category(itr.nextStatement().getResource()));
    }
    return cats;
  }

  public Set<IPerson> getDevelopers() {
    StmtIterator itr = jenaResource.listProperties(Doap.DEVELOPER);
    Set<IPerson> people = new HashSet<IPerson>();
    while (itr.hasNext()) {
      people.add(new Person(itr.nextStatement().getResource()));
    }
    return people;
  }

  public Set<IPerson> getDocumenters() {
    StmtIterator itr = jenaResource.listProperties(Doap.DOCUMENTER);
    Set<IPerson> people = new HashSet<IPerson>();
    while (itr.hasNext()) {
      people.add(new Person(itr.nextStatement().getResource()));
    }
    return people;
  }

  public Set<IDoapDownloadMirror> getDownloadMirrors() {
    StmtIterator itr = jenaResource.listProperties(Doap.DOWNLOAD_MIRROR);
    Set<IDoapDownloadMirror> mirrors = new HashSet<IDoapDownloadMirror>();
    while (itr.hasNext()) {
      mirrors.add(new DownloadMirror(itr.nextStatement().getResource()));
    }
    return mirrors;
  }

  public Set<IDoapDownloadPage> getDownloadPages() {
    StmtIterator itr = jenaResource.listProperties(Doap.DOWNLOAD_PAGE);
    Set<IDoapDownloadPage> pages = new HashSet<IDoapDownloadPage>();
    while (itr.hasNext()) {
      pages.add(new DownloadPage(itr.nextStatement().getResource()));
    }
    return pages;
  }

  public Set<IPerson> getHelpers() {
    StmtIterator itr = jenaResource.listProperties(Doap.HELPER);
    Set<IPerson> people = new HashSet<IPerson>();
    while (itr.hasNext()) {
      people.add(new Person(itr.nextStatement().getResource()));
    }
    return people;
  }

  public Set<IDoapHomepage> getHomepages() {
    StmtIterator itr = jenaResource.listProperties(Doap.HOMEPAGE);
    Set<IDoapHomepage> pages = new HashSet<IDoapHomepage>();
    while (itr.hasNext()) {
      pages.add(new Homepage(itr.nextStatement().getResource()));
    }
    return pages;
  }

  public Set<IDoapBugDatabase> getIssueTrackers() {
    StmtIterator itr = jenaResource.listProperties(Doap.BUG_DATABASE);
    Set<IDoapBugDatabase> trackers = new HashSet<IDoapBugDatabase>();
    while (itr.hasNext()) {
      trackers.add(new BugDatabase(itr.nextStatement().getResource()));
    }
    return trackers;
  }

  public Set<IDoapLicence> getLicences() {
    HashSet<IDoapLicence> licences = new HashSet<IDoapLicence>();
    StmtIterator statements = jenaResource.listProperties(Doap.LICENSE);
    while (statements.hasNext()) {
      licences.add(new Licence(statements.nextStatement().getResource()));
    }
    return licences;
  }

  public Set<IDoapMailingList> getMailingLists() {
    HashSet<IDoapMailingList> lists = new HashSet<IDoapMailingList>();
    StmtIterator statements = jenaResource.listProperties(Doap.MAILING_LIST);
    while (statements.hasNext()) {
      lists.add(new MailingList(statements.nextStatement().getResource()));
    }
    return lists;
  }

  public Set<IPerson> getMaintainers() {
    HashSet<IPerson> people = new HashSet<IPerson>();
    StmtIterator maintainers = jenaResource.listProperties(Doap.MAINTAINER);
    while (maintainers.hasNext()) {
      people.add(new Person(maintainers.nextStatement().getResource()));
    }
    return people;
  }

  public Set<String> getOSes() {
    StmtIterator itr = jenaResource.listProperties(Doap.OS);
    Set<String> langs = new HashSet<String>();
    while (itr.hasNext()) {
      langs.add(itr.nextStatement().getString());
    }
    return langs;
  }

  public Set<IDoapHomepage> getOldHomepages() {
    StmtIterator itr = jenaResource.listProperties(Doap.OLD_HOMEPAGE);
    Set<IDoapHomepage> pages = new HashSet<IDoapHomepage>();
    while (itr.hasNext()) {
      pages.add(new Homepage(itr.nextStatement().getResource()));
    }
    return pages;
  }

  public Set<String> getProgrammingLanguages() {
    StmtIterator itr = jenaResource.listProperties(Doap.PROGRAMMING_LANGUAGE);
    Set<String> langs = new HashSet<String>();
    while (itr.hasNext()) {
      langs.add(itr.nextStatement().getString());
    }
    return langs;
  }

  public Set<IDoapRelease> getReleases() {
    HashSet<IDoapRelease> releases = new HashSet<IDoapRelease>();
    StmtIterator statements = jenaResource.listProperties(Doap.RELEASE);
    while (statements.hasNext()) {
      releases.add(new Release(statements.nextStatement().getResource()));
    }
    return releases;
  }

  public Set<IDoapRepository> getRepositories() {
    HashSet<IDoapRepository> repos= new HashSet<IDoapRepository>();
    StmtIterator statements = jenaResource.listProperties(Doap.REPOSITORY);
    while (statements.hasNext()) {
      repos.add(new Repository(statements.nextStatement().getResource()));
    }
    return repos;
  }

  public Set<IDoapScreenshot> getScreenshots() {
    StmtIterator itr = jenaResource.listProperties(Doap.SCREENSHOTS);
    Set<IDoapScreenshot> langs = new HashSet<IDoapScreenshot>();
    while (itr.hasNext()) {
      langs.add(new Screenshot(itr.nextStatement().getResource()));
    }
    return langs;
  }

  public String getSimalID() {
    Statement idStatement = jenaResource.getProperty(SimalOntology.PROJECT_ID);
    return idStatement.getString();
  }

  public Set<IPerson> getTesters() {
    StmtIterator itr = jenaResource.listProperties(Doap.TESTER);
    Set<IPerson> people = new HashSet<IPerson>();
    while (itr.hasNext()) {
      people.add(new Person(itr.nextStatement().getResource()));
    }
    return people;
  }

  public Set<IPerson> getTranslators() {
    StmtIterator itr = jenaResource.listProperties(Doap.TRANSLATOR);
    Set<IPerson> people = new HashSet<IPerson>();
    while (itr.hasNext()) {
      people.add(new Person(itr.nextStatement().getResource()));
    }
    return people;
  }

  public Set<IDoapWiki> getWikis() {
    StmtIterator itr = jenaResource.listProperties(Doap.WIKI);
    Set<IDoapWiki> pages = new HashSet<IDoapWiki>();
    while (itr.hasNext()) {
      pages.add(new Wiki(itr.nextStatement().getResource()));
    }
    return pages;
  }

  public void setSimalID(String newID) {
    jenaResource.addLiteral(SimalOntology.PROJECT_ID, newID);
  }
  
  protected String toJSONRecordContent() throws SimalRepositoryException {
    StringBuffer json = new StringBuffer();
    json.append(super.toJSONRecordContent());

    json.append(", \"category\":" + toJSONValues(getCategories()));

    HashSet<IPerson> people;
    try {
      people = getAllPeople();
      json.append(", \"person\":" + toJSONValues(people));
    } catch (SimalRepositoryException e) {
      json.append(", \"person\":\"\"");
    }

    json.append(", \"programmingLanguage\":"
        + toJSONValues(getProgrammingLanguages()));
    return json.toString();
  }
  



  /**
   * Given a set of DOAP resources return a JSON representation
   * of those resources. 
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
    values.append("[");
    while (itr.hasNext()) {
      resource = itr.next();
      if (resource instanceof IDoapResource) {
        values.append("\"" + ((IDoapResource) resource).getLabel() + "\"");
      } else {
        values.append("\"" + resource.toString() + "\"");
      }
      if (itr.hasNext()) {
        values.append(", ");
      }
    }
    values.append("]");
    return values.toString();
  }

  public void removeDeveloper(IPerson person) throws SimalRepositoryException {
    Model model = jenaResource.getModel(); 
    Statement statement = model.createStatement(jenaResource, Doap.DEVELOPER, (com.hp.hpl.jena.rdf.model.Resource)person.getRepositoryResource());
    model.remove(statement);
  }

  public void addDeveloper(IPerson person) {
    Model model = jenaResource.getModel(); 
    Statement statement = model.createStatement(jenaResource, Doap.DEVELOPER, (com.hp.hpl.jena.rdf.model.Resource)person.getRepositoryResource());
    model.add(statement);
  }

  public void addDocumenter(IPerson person) {
    Model model = jenaResource.getModel(); 
    Statement statement = model.createStatement(jenaResource, Doap.DOCUMENTER, (com.hp.hpl.jena.rdf.model.Resource)person.getRepositoryResource());
    model.add(statement);
  }

  public void addHelper(IPerson person) {
    Model model = jenaResource.getModel(); 
    Statement statement = model.createStatement(jenaResource, Doap.HELPER, (com.hp.hpl.jena.rdf.model.Resource)person.getRepositoryResource());
    model.add(statement);
  }

  public void addMaintainer(IPerson person) {
    Model model = jenaResource.getModel(); 
    Statement statement = model.createStatement(jenaResource, Doap.MAINTAINER, (com.hp.hpl.jena.rdf.model.Resource)person.getRepositoryResource());
    model.add(statement);
  }

  public void addTester(IPerson person) {
    Model model = jenaResource.getModel(); 
    Statement statement = model.createStatement(jenaResource, Doap.TESTER, (com.hp.hpl.jena.rdf.model.Resource)person.getRepositoryResource());
    model.add(statement);
  }

  public void addTranslator(IPerson person) {
    Model model = jenaResource.getModel(); 
    Statement statement = model.createStatement(jenaResource, Doap.TRANSLATOR, (com.hp.hpl.jena.rdf.model.Resource)person.getRepositoryResource());
    model.add(statement);
  }

  public void removeDocumenter(IPerson person) throws SimalRepositoryException {
    Model model = jenaResource.getModel(); 
    Statement statement = model.createStatement(jenaResource, Doap.DOCUMENTER, (com.hp.hpl.jena.rdf.model.Resource)person.getRepositoryResource());
    model.remove(statement);
  }

  public void removeHelper(IPerson person) throws SimalRepositoryException {
    Model model = jenaResource.getModel(); 
    Statement statement = model.createStatement(jenaResource, Doap.HELPER, (com.hp.hpl.jena.rdf.model.Resource)person.getRepositoryResource());
    model.remove(statement);
  }

  public void removeMaintainer(IPerson person) throws SimalRepositoryException {
    Model model = jenaResource.getModel(); 
    Statement statement = model.createStatement(jenaResource, Doap.MAINTAINER, (com.hp.hpl.jena.rdf.model.Resource)person.getRepositoryResource());
    model.remove(statement);
  }

  public void removeTester(IPerson person) throws SimalRepositoryException {
    Model model = jenaResource.getModel(); 
    Statement statement = model.createStatement(jenaResource, Doap.TESTER, (com.hp.hpl.jena.rdf.model.Resource)person.getRepositoryResource());
    model.remove(statement);
  }

  public void removeTranslator(IPerson person) throws SimalRepositoryException {
    Model model = jenaResource.getModel(); 
    Statement statement = model.createStatement(jenaResource, Doap.TRANSLATOR, (com.hp.hpl.jena.rdf.model.Resource)person.getRepositoryResource());
    model.remove(statement);
  }

  public String toString() {
    return getNames().toString();
  }

}

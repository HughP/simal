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
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IDoapLicence;
import uk.ac.osswatch.simal.model.IDoapMailingList;
import uk.ac.osswatch.simal.model.IDoapRelease;
import uk.ac.osswatch.simal.model.IDoapRepository;
import uk.ac.osswatch.simal.model.IDoapResource;
import uk.ac.osswatch.simal.model.IDocument;
import uk.ac.osswatch.simal.model.IFeed;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.simal.IReview;
import uk.ac.osswatch.simal.model.simal.SimalOntology;
import uk.ac.osswatch.simal.rdf.Doap;
import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.IReviewService;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;

public class Project extends DoapResource implements IProject {
  private static final long serialVersionUID = 1960364043645152134L;
  private static final Logger logger = LoggerFactory.getLogger(Project.class);

  public Project(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
  }

  public HashSet<IPerson> getAllPeople() {
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
    Iterator<Statement> itr = listProperties(Doap.CATEGORY).iterator();
    Set<IDoapCategory> cats = new HashSet<IDoapCategory>();
    while (itr.hasNext()) {
      Statement stmt = itr.next();
      cats.add(new Category(stmt.getResource()));
    }
    return cats;
  }
  
  public void addCategory(IDoapCategory category) {
	  addResourceStatement(Doap.CATEGORY, category);
  }


  public Set<IPerson> getDevelopers() {
    return getUniquePeople(Doap.DEVELOPER);
  }

  public Set<IPerson> getDocumenters() {
    return getUniquePeople(Doap.DOCUMENTER);
  }

  public Set<IDocument> getDownloadMirrors() {
    return getDocuments(Doap.DOWNLOAD_MIRROR);
  }

  public Set<IDocument> getDownloadPages() {
    return getDocuments(Doap.DOWNLOAD_PAGE);
  }

  public Set<IPerson> getHelpers() {
    return getUniquePeople(Doap.HELPER);
  }

  public Set<IDocument> getHomepages() {
    return getDocuments(Doap.HOMEPAGE);
  }

  public Set<IDocument> getIssueTrackers() {
    return getDocuments(Doap.BUG_DATABASE);
  }

  public Set<IDoapMailingList> getMailingLists() {
    HashSet<IDoapMailingList> lists = new HashSet<IDoapMailingList>();
    Iterator<Statement> statements = listProperties(Doap.MAILING_LIST)
        .iterator();
    while (statements.hasNext()) {
      lists.add(new MailingList(statements.next().getResource()));
    }
    return lists;
  }

  public Set<IPerson> getMaintainers() {
    return getUniquePeople(Doap.MAINTAINER);
  }

  /**
   * Given a list of statements representing people extract all the unique people within that set.
   * 
   * @param property 
   * @return A set containing only unique people as identified by their Simal ID
   */
  private HashSet<IPerson> getUniquePeople(Property property) {
    List<Statement> personList = listProperties(property);
    HashSet<IPerson> people = new HashSet<IPerson>();
    HashSet<String> peopleIDs = new HashSet<String>();
    Iterator<Statement> itr = personList.iterator();
    while (itr.hasNext()) {
      String uri = itr.next().getResource().getURI();
      try {
        IPerson person = SimalRepositoryFactory.getPersonService().findBySeeAlso(uri);
        if (person == null) {
          person = SimalRepositoryFactory.getPersonService().get(uri);
          if (person == null) {
            throw new SimalRepositoryException("No person with the URI " + uri);
          }
        }
        String id = person.getSimalID();
        if (!peopleIDs.contains(id)) {
          people.add(person);
          peopleIDs.add(id);
        }
      } catch (SimalRepositoryException e) {
        logger.warn("Unable to read person with URI " + uri);
      }
    }
    return people;
  }

  public Set<String> getOSes() {
    Iterator<Statement> itr = listProperties(Doap.OS).iterator();
    Set<String> langs = new HashSet<String>();
    while (itr.hasNext()) {
      langs.add(itr.next().getString());
    }
    return langs;
  }

  public void setOSes(Set<String> oses) {
    replaceLiteralStatements(Doap.OS, oses);
  }
  
  public Set<IDocument> getOldHomepages() {
    return getDocuments(Doap.OLD_HOMEPAGE);
  }

  public Set<String> getProgrammingLanguages() {
    Iterator<Statement> itr = listProperties(Doap.PROGRAMMING_LANGUAGE)
        .iterator();
    Set<String> langs = new HashSet<String>();
    while (itr.hasNext()) {
      langs.add(itr.next().getString());
    }
    return langs;
  }

  public void setProgrammingLanguages(Set<String> langs) {
    replaceLiteralStatements(Doap.PROGRAMMING_LANGUAGE, langs);
  }

  public Set<IDoapRelease> getReleases() {
    HashSet<IDoapRelease> releases = new HashSet<IDoapRelease>();
    Iterator<Statement> statements = listProperties(Doap.RELEASE).iterator();
    while (statements.hasNext()) {
      releases.add(new Release(statements.next().getResource()));
    }
    return releases;
  }

  public Set<IDoapRepository> getRepositories() {
    HashSet<IDoapRepository> repos = new HashSet<IDoapRepository>();
    Iterator<Statement> statements = listProperties(Doap.REPOSITORY).iterator();
    while (statements.hasNext()) {
      try {
        repos.add(new Repository(statements.next().getResource()));
      } catch (SimalException e) {
        logger.warn("Unexpected unknown type of repository : " + e.getMessage());
      }
    }
    return repos;
  }

  public Set<IDocument> getScreenshots() {
    return getDocuments(Doap.SCREENSHOTS);
  }

  public String getSimalID() throws SimalRepositoryException {
    String uniqueID = getUniqueSimalID();
    String id = uniqueID.substring(uniqueID.lastIndexOf("-") + 1);
    return id;
  }

  public String getUniqueSimalID() throws SimalRepositoryException {
    String id;
    Statement idStatement = getJenaResource().getProperty(
        SimalOntology.PROJECT_ID);
    if (idStatement == null) {
      logger.warn("Project instance with no Simal ID - " + getURI());
      id = null;
    } else {
      id = idStatement.getString();
    }
    return id;
  }

  public void setSimalID(String id) throws SimalRepositoryException {
    if (id.contains(":")
        && !id.startsWith(SimalProperties
            .getProperty((SimalProperties.PROPERTY_SIMAL_INSTANCE_ID)))) {
      throw new SimalRepositoryException("Simal ID cannot contain a ':', id is " + id);
    }
    logger.info("Setting simalId for " + this + " to " + id);
    getJenaResource().addLiteral(SimalOntology.PROJECT_ID, id);
  }

  public Set<IPerson> getTesters() {
    return getUniquePeople(Doap.TESTER);
  }

  public Set<IPerson> getTranslators() {
    return getUniquePeople(Doap.TRANSLATOR);
  }

  private Set<IDocument> getDocuments(Property property) {
    Iterator<Statement> itr = listProperties(property).iterator();
    Set<IDocument> docs = new HashSet<IDocument>();
    while (itr.hasNext()) {
      docs.add(new Document(itr.next().getResource()));
    }
    return docs;
  }
  
  public Set<IDocument> getWikis() {
    return getDocuments(Doap.WIKI);
  }

  public Set<IDocument> getBlogs() {
    return getDocuments(Doap.BLOG);
  }

  protected String toJSONRecordContent() throws SimalRepositoryException {
    StringBuffer json = new StringBuffer();
    json.append(super.toJSONRecordContent());

    json.append(", \"simalID\":\"" + getSimalID() + "\"");

    // community resources
    json.append(", \"homepage\":" + toJSONValues(getHomepages()));
    json.append(", \"issueTracker\":" + toJSONValues(getIssueTrackers()));
    json.append(", \"repository\":" + toJSONValues(getRepositories()));
    json.append(", \"wiki\":" + toJSONValues(getWikis()));
    json.append(", \"downloadPage\":" + toJSONValues(getDownloadPages()));
    json.append(", \"downloadMirror\":" + toJSONValues(getDownloadMirrors()));
    json.append(", \"screenshot\":" + toJSONValues(getScreenshots()));
    
    json.append(", \"category\":" + toJSONValues(getCategories()));

    HashSet<IPerson> people;
    people = getAllPeople();
    json.append(", \"person\":" + toJSONValues(people));

    json.append(", \"programmingLanguage\":"
        + toJSONValues(getProgrammingLanguages()));
    return json.toString();
  }

  /**
   * Given a set of DOAP resources return a JSON representation of those
   * resources.
   * 
   * @param resources
   * @return
   * @throws SimalRepositoryException 
   */
  private String toJSONValues(Set<?> resources) throws SimalRepositoryException {
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
        values.append(((IDoapResource) resource).toJSON());
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
    removeCurrentProject(person);
    removeResourceStatement(Doap.DEVELOPER, person);
  }

  public void addDeveloper(IPerson person) {
    addResourceStatement(Doap.DEVELOPER, person);
    addCurrentProject(person);
  }

  public void removeHomepage(IDocument page) {
    removeResourceStatement(Doap.HOMEPAGE, page);
  }

  public void addHomepage(IDocument page) {
    addResourceStatement(Doap.HOMEPAGE, page);
  }

  /**
   * Add this project as a current project for a person.
   * 
   * @param person
   */
  private void addCurrentProject(IPerson person) {
    Model model = getJenaResource().getModel();
    Statement statement;
    statement = model.createStatement(
        (com.hp.hpl.jena.rdf.model.Resource) person.getRepositoryResource(),
        FOAF.currentProject, getJenaResource());
    model.add(statement);
  }

  private void removeCurrentProject(IPerson person) {
    Model model = getJenaResource().getModel();
    Statement statement = model.createStatement(
        (com.hp.hpl.jena.rdf.model.Resource) person.getRepositoryResource(),
        FOAF.currentProject, getJenaResource());
    model.remove(statement);
  }

  public void addDocumenter(IPerson person) {
    addResourceStatement(Doap.DOCUMENTER, person);
    addCurrentProject(person);
  }

  public void addHelper(IPerson person) {
    addResourceStatement(Doap.HELPER, person);
    addCurrentProject(person);
  }

  public void addMaintainer(IPerson person) {
    addResourceStatement(Doap.MAINTAINER, person);
    addCurrentProject(person);
  }
  
  public void addTester(IPerson person) {
    addResourceStatement(Doap.TESTER, person);
    addCurrentProject(person);
  }

  public void addTranslator(IPerson person) {
    addResourceStatement(Doap.TRANSLATOR, person);
    addCurrentProject(person);
  }

  private void replacePersonPropertyStatements(Property property,
      Set<IPerson> persons) {
    getJenaResource().removeAll(property);
    for (IPerson person : persons) {
      addResourceStatement(property, person);
      addCurrentProject(person);
    }
  }

  public void removeDocumenter(IPerson person) throws SimalRepositoryException {
    removeCurrentProject(person);
    removeResourceStatement(Doap.DOCUMENTER, person);
  }

  public void removeHelper(IPerson person) throws SimalRepositoryException {
    removeCurrentProject(person);
    removeResourceStatement(Doap.HELPER, person);
  }

  public void removeMaintainer(IPerson person) throws SimalRepositoryException {
    removeCurrentProject(person);
    removeResourceStatement(Doap.MAINTAINER, person);
  }

  public void removeTester(IPerson person) throws SimalRepositoryException {
    removeCurrentProject(person);
    removeResourceStatement(Doap.TESTER, person);
  }

  public void removeTranslator(IPerson person) throws SimalRepositoryException {
    removeCurrentProject(person);
    removeResourceStatement(Doap.TRANSLATOR, person);
  }
  
  public String toString() {
    return getNames().toString();
  }

  public Set<IFeed> getFeeds() {
    Iterator<Statement> itr = listProperties(FOAF.weblog).iterator();
    Set<IFeed> feeds = new HashSet<IFeed>();
    while (itr.hasNext()) {
      feeds.add(new Feed(itr.next().getResource()));
    }
    return feeds;
  }

  /**
   * Temporary fix to return an accessible url to the project; imitates
   * construction mechanism of the BookmarkablePageLink in Wicket. 
   * 
   * TODO Redesign in line with best practice linked data approach, eg.
   * make URI of the project an accessible URL.
   * 
   * @return URL of a web page where project can be viewed.
   */
  public String generateURL() {
    // http://<host>/project/simalID/<simalID>
    StringBuffer url = new StringBuffer();

    try {
      url.append(SimalProperties
          .getProperty(SimalProperties.PROPERTY_USER_WEBAPP_BASEURL));
      if (!url.toString().endsWith("/")) {
        url.append('/');
      }
      url.append("project/simalID/");
      url.append(getSimalID());
    } catch (SimalRepositoryException e) {
      logger.info("Could not generate url using property "
          + SimalProperties.PROPERTY_USER_WEBAPP_BASEURL + "; error:"
          + e.getMessage(), e);
    }
    
    return url.toString();
  }

  public int getOpennessRating() throws SimalRepositoryException {
	IReviewService service = SimalRepositoryFactory.getReviewService();
	Set<IReview> reviews = service.getReviewsForProject(this);
	if (reviews.size() == 0) {
		logger.debug(this.toString() + " project does not have a review and thus has no openness rating.");
		throw new SimalRepositoryException("Unable to get an openness rating since there has been no review of this entry yet");
	}
	
	double score = 0;
	if (getDownloadPages().size() > 0) {
		score = score + 1;
	}
	if (getFeeds().size() > 0) {
		score = score + 1;
	}
	if (getHomepages().size() > 0) {
	  score = score + 1;
	}
	if (getIssueTrackers().size() > 0) {
		score = score + 1;
	}
	if (getMailingLists().size() > 0) {
		score = score + 1;
	}
	if (getRepositories().size() > 0) {
		score = score + 1;
	}
		
	int rating = (int)(Math.round((score/6.0) * 100));
	return rating;
  }

  public void addRepository(IDoapRepository rcs) {
    addResourceStatement(Doap.REPOSITORY, rcs);
  }

  public void setRepositories(Set<IDoapRepository> repos) {
    replacePropertyStatements(Doap.REPOSITORY, repos);
  }

  public void setHomepages(Set<IDocument> homepages) {
    replacePropertyStatements(Doap.HOMEPAGE, homepages);
  }

  public void setIssueTrackers(Set<IDocument> trackers) {
    replacePropertyStatements(Doap.BUG_DATABASE, trackers);
  }

  public void addIssueTracker(IDocument tracker) {
    addResourceStatement(Doap.BUG_DATABASE, tracker);
  }

  public void removeIssueTracker(IDocument document) {
    removeResourceStatement(Doap.BUG_DATABASE, document);
  }
  
  public void setMailingLists(Set<IDoapMailingList> lists) {
    replacePropertyStatements(Doap.MAILING_LIST, lists);
  }

  public void addMailingList(IDoapMailingList list) {
    addResourceStatement(Doap.MAILING_LIST, list);
  }

  public void setMaintainers(Set<IPerson> maintainers) {
    replacePersonPropertyStatements(Doap.MAINTAINER, maintainers);
  }

  public void setReleases(Set<IDoapRelease> releases) {
    replacePropertyStatements(Doap.RELEASE, releases);
  }

  public void addRelease(IDoapRelease release) {
    addResourceStatement(Doap.RELEASE, release);
  }

  public void delete() throws SimalRepositoryException {
    Set<IDocument> allDocuments = getHomepages();
    allDocuments.addAll(getOldHomepages());
    allDocuments.addAll(getIssueTrackers());
    allDocuments.addAll(getScreenshots());
    allDocuments.addAll(getDownloadPages());
    allDocuments.addAll(getDownloadMirrors());
    allDocuments.addAll(getWikis());
    allDocuments.addAll(getBlogs());

    delete(true);

    for (IDocument doc : allDocuments) { 
      doc.delete();
    }
  }

  public void addWiki(IDocument document) {
    addResourceStatement(Doap.WIKI, document);
  }

  public void removeWiki(IDocument document) {
    removeResourceStatement(Doap.WIKI, document);
  }

  public void removeDownloadPage(IDocument document) {
    removeResourceStatement(Doap.DOWNLOAD_PAGE, document);
  }

  public void addDownloadPage(IDocument document) {
    addResourceStatement(Doap.DOWNLOAD_PAGE, document);
  }

  public void addDownloadMirror(IDocument document) {
    addResourceStatement(Doap.DOWNLOAD_MIRROR, document);
  }

  public void removeDownloadMirror(IDocument document) {
    removeResourceStatement(Doap.DOWNLOAD_MIRROR, document);
  }

  public void addScreenshot(IDocument document) {
    addResourceStatement(Doap.SCREENSHOTS, document);
  }

  public void removeScreenshot(IDocument document) {
    removeResourceStatement(Doap.SCREENSHOTS, document);
  }

  public Set<IDoapLicence> getLicences() {
    Iterator<Statement> props = listProperties(Doap.LICENSE).iterator();
    Set<IDoapLicence> results = new HashSet<IDoapLicence>();
    while (props.hasNext()) {
      results.add(new Licence(props.next().getResource()));
    }
    return results;
  }

  public void setLicences(Set<IDoapLicence> licenses) {
    replacePropertyStatements(Doap.LICENSE, licenses);
  }

}

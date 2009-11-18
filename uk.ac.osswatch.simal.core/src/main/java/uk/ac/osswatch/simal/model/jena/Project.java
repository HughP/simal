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
import uk.ac.osswatch.simal.model.IDoapBugDatabase;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IDoapDownloadMirror;
import uk.ac.osswatch.simal.model.IDoapDownloadPage;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IDoapMailingList;
import uk.ac.osswatch.simal.model.IDoapRelease;
import uk.ac.osswatch.simal.model.IDoapRepository;
import uk.ac.osswatch.simal.model.IDoapResource;
import uk.ac.osswatch.simal.model.IDoapScreenshot;
import uk.ac.osswatch.simal.model.IDoapWiki;
import uk.ac.osswatch.simal.model.IFeed;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.simal.IReview;
import uk.ac.osswatch.simal.model.simal.SimalOntology;
import uk.ac.osswatch.simal.rdf.Doap;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.IReviewService;

import com.hp.hpl.jena.rdf.model.Model;
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
	  com.hp.hpl.jena.rdf.model.Resource jenaCat = (com.hp.hpl.jena.rdf.model.Resource)category.getRepositoryResource();
	  getJenaResource().addProperty(Doap.CATEGORY, jenaCat);
  }


  public Set<IPerson> getDevelopers() {
    return getUniquePeople(listProperties(Doap.DEVELOPER));
  }

  public Set<IPerson> getDocumenters() {
    return getUniquePeople(listProperties(Doap.DOCUMENTER));
  }

  public Set<IDoapDownloadMirror> getDownloadMirrors() {
    Iterator<Statement> itr = listProperties(Doap.DOWNLOAD_MIRROR).iterator();
    Set<IDoapDownloadMirror> mirrors = new HashSet<IDoapDownloadMirror>();
    while (itr.hasNext()) {
      mirrors.add(new DownloadMirror(itr.next().getResource()));
    }
    return mirrors;
  }

  public Set<IDoapDownloadPage> getDownloadPages() {
    Iterator<Statement> itr = listProperties(Doap.DOWNLOAD_PAGE).iterator();
    Set<IDoapDownloadPage> pages = new HashSet<IDoapDownloadPage>();
    while (itr.hasNext()) {
      pages.add(new DownloadPage(itr.next().getResource()));
    }
    return pages;
  }

  public Set<IPerson> getHelpers() {
    return getUniquePeople(listProperties(Doap.HELPER));
  }

  public Set<IDoapHomepage> getHomepages() {
    List<Statement> props = listProperties(Doap.HOMEPAGE);
	Iterator<Statement> itr = props.iterator();
    Set<IDoapHomepage> pages = new HashSet<IDoapHomepage>();
    while (itr.hasNext()) {
      Statement stmnt = itr.next();
      pages.add(new Homepage(stmnt.getResource()));
    }
    return pages;
  }

  public Set<IDoapBugDatabase> getIssueTrackers() {
    Iterator<Statement> itr = listProperties(Doap.BUG_DATABASE).iterator();
    Set<IDoapBugDatabase> trackers = new HashSet<IDoapBugDatabase>();
    while (itr.hasNext()) {
      trackers.add(new BugDatabase(itr.next().getResource()));
    }
    return trackers;
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
    return getUniquePeople(listProperties(Doap.MAINTAINER));
  }

  /**
   * Given a list of statements representing people extract all the unique people within that set.
   * 
   * @param personList
   * @return A set containing only unique people as identified by their Simal ID
   */
  private HashSet<IPerson> getUniquePeople(List<Statement> personList) {
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

  public Set<IDoapHomepage> getOldHomepages() {
    Iterator<Statement> itr = listProperties(Doap.OLD_HOMEPAGE).iterator();
    Set<IDoapHomepage> pages = new HashSet<IDoapHomepage>();
    while (itr.hasNext()) {
      pages.add(new Homepage(itr.next().getResource()));
    }
    return pages;
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
      repos.add(new Repository(statements.next().getResource()));
    }
    return repos;
  }

  public Set<IDoapScreenshot> getScreenshots() {
    Iterator<Statement> itr = listProperties(Doap.SCREENSHOTS).iterator();
    Set<IDoapScreenshot> langs = new HashSet<IDoapScreenshot>();
    while (itr.hasNext()) {
      langs.add(new Screenshot(itr.next().getResource()));
    }
    return langs;
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
    return getUniquePeople(listProperties(Doap.TESTER));
  }

  public Set<IPerson> getTranslators() {
    return getUniquePeople(listProperties(Doap.TRANSLATOR));
  }

  public Set<IDoapWiki> getWikis() {
    Iterator<Statement> itr = listProperties(Doap.WIKI).iterator();
    Set<IDoapWiki> pages = new HashSet<IDoapWiki>();
    while (itr.hasNext()) {
      pages.add(new Wiki(itr.next().getResource()));
    }
    return pages;
  }

  protected String toJSONRecordContent() throws SimalRepositoryException {
    StringBuffer json = new StringBuffer();
    json.append(super.toJSONRecordContent());

    json.append(", \"simalID\":\"" + getSimalID() + "\"");

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
    Model model = getJenaResource().getModel();
    Statement statement = model.createStatement(getJenaResource(),
        Doap.DEVELOPER, (com.hp.hpl.jena.rdf.model.Resource) person
            .getRepositoryResource());
    model.remove(statement);
  }

  public void addDeveloper(IPerson person) {
    Model model = getJenaResource().getModel();
    Statement statement = model.createStatement(getJenaResource(),
        Doap.DEVELOPER, (com.hp.hpl.jena.rdf.model.Resource) person
            .getRepositoryResource());
    model.add(statement);
    addCurrentProject(person);
  }

  public void removeHomepage(IDoapHomepage page) {
    Model model = getJenaResource().getModel();
    Statement statement = model.createStatement(getJenaResource(),
        Doap.HOMEPAGE, (com.hp.hpl.jena.rdf.model.Resource) page
            .getRepositoryResource());
    model.remove(statement);
  }

  public void addHomepage(IDoapHomepage page) {
	Model model = getJenaResource().getModel();
    Statement statement = model.createStatement(getJenaResource(),
        Doap.HOMEPAGE, (com.hp.hpl.jena.rdf.model.Resource) page
            .getRepositoryResource());
    model.add(statement);
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

  public void addDocumenter(IPerson person) {
    Model model = getJenaResource().getModel();
    Statement statement = model.createStatement(getJenaResource(),
        Doap.DOCUMENTER, (com.hp.hpl.jena.rdf.model.Resource) person
            .getRepositoryResource());
    model.add(statement);
    addCurrentProject(person);
  }

  public void addHelper(IPerson person) {
    Model model = getJenaResource().getModel();
    Statement statement = model.createStatement(getJenaResource(), Doap.HELPER,
        (com.hp.hpl.jena.rdf.model.Resource) person.getRepositoryResource());
    model.add(statement);
    addCurrentProject(person);
  }

  public void addMaintainer(IPerson person) {
    Model model = getJenaResource().getModel();
    Statement statement = model.createStatement(getJenaResource(),
        Doap.MAINTAINER, (com.hp.hpl.jena.rdf.model.Resource) person
            .getRepositoryResource());
    model.add(statement);
    addCurrentProject(person);
  }
  
  public void addTester(IPerson person) {
    Model model = getJenaResource().getModel();
    Statement statement = model.createStatement(getJenaResource(), Doap.TESTER,
        (com.hp.hpl.jena.rdf.model.Resource) person.getRepositoryResource());
    model.add(statement);
    addCurrentProject(person);
  }

  public void addTranslator(IPerson person) {
    Model model = getJenaResource().getModel();
    Statement statement = model.createStatement(getJenaResource(),
        Doap.TRANSLATOR, (com.hp.hpl.jena.rdf.model.Resource) person
            .getRepositoryResource());
    model.add(statement);
    addCurrentProject(person);
  }

  public void removeDocumenter(IPerson person) throws SimalRepositoryException {
    Model model = getJenaResource().getModel();
    Statement statement = model.createStatement(getJenaResource(),
        Doap.DOCUMENTER, (com.hp.hpl.jena.rdf.model.Resource) person
            .getRepositoryResource());
    model.remove(statement);
  }

  public void removeHelper(IPerson person) throws SimalRepositoryException {
    Model model = getJenaResource().getModel();
    Statement statement = model.createStatement(getJenaResource(), Doap.HELPER,
        (com.hp.hpl.jena.rdf.model.Resource) person.getRepositoryResource());
    model.remove(statement);
  }

  public void removeMaintainer(IPerson person) throws SimalRepositoryException {
    Model model = getJenaResource().getModel();
    Statement statement = model.createStatement(getJenaResource(),
        Doap.MAINTAINER, (com.hp.hpl.jena.rdf.model.Resource) person
            .getRepositoryResource());
    model.remove(statement);
  }

  public void removeTester(IPerson person) throws SimalRepositoryException {
    Model model = getJenaResource().getModel();
    Statement statement = model.createStatement(getJenaResource(), Doap.TESTER,
        (com.hp.hpl.jena.rdf.model.Resource) person.getRepositoryResource());
    model.remove(statement);
  }

  public void removeTranslator(IPerson person) throws SimalRepositoryException {
    Model model = getJenaResource().getModel();
    Statement statement = model.createStatement(getJenaResource(),
        Doap.TRANSLATOR, (com.hp.hpl.jena.rdf.model.Resource) person
            .getRepositoryResource());
    model.remove(statement);
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
    Model model = getJenaResource().getModel();
    Statement statement = model.createStatement(getJenaResource(),
        Doap.REPOSITORY, (com.hp.hpl.jena.rdf.model.Resource) rcs
            .getRepositoryResource());
    model.add(statement);
}

public void setRepositories(Set<IDoapRepository> repos) throws SimalRepositoryException {
}

public void setHomepages(Set<IDoapHomepage> homepages) {
}

public void setIssueTrackers(Set<IDoapBugDatabase> trackers) {
}

public void addIssueTracker(IDoapBugDatabase tracker) {
	
}

public void setMailingLists(Set<IDoapMailingList> lists) {
	
}

public void addMailingList(IDoapMailingList list) {
	
}

public void setMaintainers(Set<IPerson> maintainers) {
	
}

public void setReleases(Set<IDoapRelease> releases) {
}

public void addRelease(IDoapRelease release) {
}

}

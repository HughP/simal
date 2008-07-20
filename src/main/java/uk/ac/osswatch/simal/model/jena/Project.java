package uk.ac.osswatch.simal.model.jena;

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

import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class Project extends DoapResource implements IProject {

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
    // TODO Auto-generated method stub
    return null;
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
    // TODO Auto-generated method stub
    return null;
  }

  public Set<IDoapDownloadPage> getDownloadPages() {
    // TODO Auto-generated method stub
    return null;
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
    Set<IDoapHomepage> trackers = new HashSet<IDoapHomepage>();
    while (itr.hasNext()) {
      trackers.add(new Homepage(itr.nextStatement().getResource()));
    }
    return trackers;
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
    // TODO Auto-generated method stub
    return null;
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
    // TODO Auto-generated method stub
    return null;
  }

  public Set<IDoapHomepage> getOldHomepages() {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<String> getProgrammingLanguages() {
    // TODO Auto-generated method stub
    return null;
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
    // TODO Auto-generated method stub
    return null;
  }

  public Set<IDoapScreenshot> getScreenshots() {
    // TODO Auto-generated method stub
    return null;
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
    // TODO Auto-generated method stub
    return null;
  }

  public void setSimalID(String newID) {
    jenaResource.addLiteral(SimalOntology.PROJECT_ID, newID);
  }

  public void delete() throws SimalRepositoryException {
    // TODO Auto-generated method stub

  }

  public String toJSON(boolean asRecord) {
    // TODO Auto-generated method stub
    return null;
  }

  public String toXML() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
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


}

package uk.ac.osswatch.simal.model.jena;

import java.net.URI;
import java.net.URL;
import java.util.HashSet;
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
import uk.ac.osswatch.simal.model.IDoapScreenshot;
import uk.ac.osswatch.simal.model.IDoapWiki;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

import com.hp.hpl.jena.rdf.model.StmtIterator;

public class Project extends Resource implements IProject {

  private static final Logger logger = LoggerFactory.getLogger(Project.class);
  
  public Project(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
  }
  
  public HashSet<IPerson> getAllPeople() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<IDoapCategory> getCategories() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getDescription() {
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
    // TODO Auto-generated method stub
    return null;
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
    // TODO Auto-generated method stub
    return null;
  }

  public Set<IDoapHomepage> getHomepages() {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<IDoapBugDatabase> getIssueTrackers() {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<IDoapLicence> getLicences() {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<IDoapMailingList> getMailingLists() {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<IPerson> getMaintainers() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getName() {
    // TODO Auto-generated method stub
    return null;
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
    // TODO Auto-generated method stub
    return null;
  }

  public Set<IDoapRepository> getRepositories() {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<IDoapScreenshot> getScreenshots() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getShortDesc() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getSimalID() {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<IPerson> getTesters() {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<IPerson> getTranslators() {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<IDoapWiki> getWikis() {
    // TODO Auto-generated method stub
    return null;
  }

  public void setDescription(String desc) {
    // TODO Auto-generated method stub

  }

  public void setShortDesc(String desc) {
    // TODO Auto-generated method stub

  }

  public void setSimalID(String newID) {
    // TODO Auto-generated method stub

  }

  public void addName(String name) {
    // TODO Auto-generated method stub

  }

  public String getCreated() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getLabel() {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<String> getNames() {
    // TODO Auto-generated method stub
    return null;
  }

  public void setCreated(String newCreated) throws SimalRepositoryException {
    // TODO Auto-generated method stub

  }

  public String getComment() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getLabel(String defaultLabel) {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<URI> getSeeAlso() {
    // TODO Auto-generated method stub
    return null;
  }

  public URI getURI() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public URL getURL() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
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

  public String toJSON() {
    // TODO Auto-generated method stub
    return null;
  }

  public String toJSONRecord() {
    // TODO Auto-generated method stub
    return null;
  }

}

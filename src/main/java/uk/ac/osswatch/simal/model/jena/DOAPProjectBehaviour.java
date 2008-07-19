package uk.ac.osswatch.simal.model.jena;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import uk.ac.osswatch.simal.model.IDoapBugDatabase;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IDoapDownloadMirror;
import uk.ac.osswatch.simal.model.IDoapDownloadPage;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IDoapMailingList;
import uk.ac.osswatch.simal.model.IDoapProjectBehaviour;
import uk.ac.osswatch.simal.model.IDoapRelease;
import uk.ac.osswatch.simal.model.IDoapRepository;
import uk.ac.osswatch.simal.model.IDoapScreenshot;
import uk.ac.osswatch.simal.model.IDoapWiki;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class DOAPProjectBehaviour extends DOAPResourceBehavior implements IDoapProjectBehaviour{

  public HashSet<IPerson> getAllPeople() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<IDoapCategory> getCategories() {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<IPerson> getDevelopers() {
    // TODO Auto-generated method stub
    return null;
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

  public void addName(String name) {
    // TODO Auto-generated method stub
    
  }

  public String getCreated() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getDescription() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getLabel() {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<String> getLicences() {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<String> getNames() {
    // TODO Auto-generated method stub
    return null;
  }

  public String getShortDesc() {
    // TODO Auto-generated method stub
    return null;
  }

  public URL getURL() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public void setCreated(String newCreated) throws SimalRepositoryException {
    // TODO Auto-generated method stub
    
  }

  public void setDescription(String newDescription) {
    // TODO Auto-generated method stub
    
  }

  public void setShortDesc(String shortDesc) {
    // TODO Auto-generated method stub
    
  }

  public String toJSON() {
    // TODO Auto-generated method stub
    return null;
  }

  public String toJSONRecord() {
    // TODO Auto-generated method stub
    return null;
  }

  public void delete() throws SimalRepositoryException {
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

  public String toJSON(boolean asRecord) {
    // TODO Auto-generated method stub
    return null;
  }

  public String toXML() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

}

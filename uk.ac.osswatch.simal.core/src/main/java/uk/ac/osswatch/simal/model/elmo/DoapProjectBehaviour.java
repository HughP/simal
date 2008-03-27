package uk.ac.osswatch.simal.model.elmo;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.openrdf.concepts.doap.Repository;
import org.openrdf.concepts.doap.Version;
import org.openrdf.concepts.foaf.Person;
import org.openrdf.elmo.annotations.rdf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * Behaviour for a simal.Project object. This class provieds easy access to an
 * elmo produced doap.Project instance.
 * 
 * @see org.openrdf.concepts.doap.Project
 */
@rdf("http://usefulinc.com/ns/doap#Project")
public class DoapProjectBehaviour extends DoapResourceBehaviour implements IDoapProjectBehaviour {
  private static final Logger logger = LoggerFactory
      .getLogger(DoapProjectBehaviour.class);

  /**
   * Create a new behaviour for an elmo Project object.
   * 
   * @param simalTestProject
   */
   public DoapProjectBehaviour(IProject elmoProject) {
    super(elmoProject);
    logger.debug("created a behaviour object for an Elmo Doap Project object");
  }
  
  private IProject getProject() {
    return (IProject) elmoEntity;
  }

  /**
   * Get a complete JSON file representing this project.
   * 
   * @return
   */
  public String toJSON() {
    StringBuffer json = new StringBuffer();
    json.append("{ \"items\": [");
    json.append(toJSONRecord());
    json.append("]}");
    return json.toString();
  }

  /**
   * Get a JSON representation of this project as a record, i.e. one that is
   * suitable for inserting into a JSON file.
   * 
   * @param json
   */
  public String toJSONRecord() {
    StringBuffer json = new StringBuffer();
    json.append("{");
    json.append(toJSONRecordContent());
    json.append("}");
    return json.toString();
  }

  protected String toJSONRecordContent() {
    StringBuffer json = new StringBuffer();
    json.append(super.toJSONRecordContent());

    json.append(", \"category\":" + toJSONValues(getProject().getDoapCategories()));

    HashSet<IPerson> people;
    try {
      people = getAllPeople();
      json.append(", \"person\":" + toJSONValues(people));
    } catch (SimalRepositoryException e) {
      json.append(", \"person\":\"\"");
    }

    json.append(", \"programmingLanguage\":"
        + toJSONValues(getProject().getDoapProgrammingLanguages()));
    return json.toString();
  }

  /**
   * Get all the people known to be engaged with this project.
   * 
   * @return
   * @throws SimalRepositoryException
   */
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


  /**
   * Given a set of DOAP resources return a JSON representation
   * of those resources. 
   * @param resources
   * @return
   */
  private String toJSONValues(Set<?> resources) {
    StringBuffer values = new StringBuffer();
    Iterator<?> itr = resources.iterator();
    Object resource;
    values.append("[");
    while (itr.hasNext()) {
      resource = itr.next();
      if (resource instanceof ResourceBehavior) {
        values.append("\"" + ((ResourceBehavior) resource).getLabel() + "\"");
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

  /**
   * Get all categories that this project belongs to.
   */
  public Set<IDoapCategory> getCategories() {
    Iterator<Object> categories = getProject().getDoapCategories().iterator();
    Set<IDoapCategory> result = new HashSet<IDoapCategory>();
    Object category;
    while (categories.hasNext()) {
      category = categories.next();
      result.add(elmoEntity.getElmoManager().designateEntity(IDoapCategory.class, category));
    }
    return result;
  }

  /**
   * Get all homepages belonging to this project.
   */
  public Set<IDoapHomepage> getHomepages() {
    Iterator<Object> homepages = getProject().getDoapHomepages().iterator();
    Set<IDoapHomepage> result = new HashSet<IDoapHomepage>();
    Object homepage;
    while (homepages.hasNext()) {
      homepage = homepages.next();
      result.add(elmoEntity.getElmoManager().designateEntity(IDoapHomepage.class, homepage));
    }
    return result;
  }

  /**
   * Get all developers who work on this project.
   */
  public Set<IPerson> getDevelopers() {
    return convertToSimalPeople(getProject().getDoapDevelopers());
  }

  /**
   * Get all documenters who work on this project.
   */
  public Set<IPerson> getDocumenters() {
    return convertToSimalPeople(getProject().getDoapDocumenters());
  }

  /**
   * Get all helpers who work on this project.
   */
  public Set<IPerson> getHelpers() {
    return convertToSimalPeople(getProject().getDoapHelpers());
  }

  /**
   * Get all maintainers who work on this project.
   */
  public Set<IPerson> getMaintainers() {
    return convertToSimalPeople(getProject().getDoapMaintainers());
  }

  /**
   * Get all testers who work on this project.
   */
  public Set<IPerson> getTesters() {
    return convertToSimalPeople(getProject().getDoapTesters());
  }

  /**
   * Get all translators who work on this project.
   */
  public Set<IPerson> getTranslators() {
    return convertToSimalPeople(getProject().getDoapTranslators());
  }

  private Set<IPerson> convertToSimalPeople(Set<Person> inPeople) {
    Set<IPerson> result = new HashSet<IPerson>(inPeople.size());
    Iterator<Person> people = inPeople.iterator();
    Person person;
    while (people.hasNext()) {
      person = people.next();
      result.add(elmoEntity.getElmoManager().designateEntity(IPerson.class, person));
    }
    return result;
  }

  /**
   * Get all issue trackers associated with this project.
   */
  public Set<IDoapBugDatabase> getIssueTrackers() {
    Set<Object> setTrackers = getProject().getDoapBugDatabases();
    Set<IDoapBugDatabase> result = new HashSet<IDoapBugDatabase>(setTrackers.size());
    Iterator<Object> trackers= setTrackers.iterator();
    Object tracker;
    while (trackers.hasNext()) {
      tracker = trackers.next();
      result.add(elmoEntity.getElmoManager().designateEntity(IDoapBugDatabase.class, tracker));
    }
    return result;
  }

  /**
   * Get all releases associated with this project.
   */
  public Set<IDoapRelease> getReleases() {
    Set<Version> setReleases = getProject().getDoapReleases();
    Set<IDoapRelease> result = new HashSet<IDoapRelease>(setReleases.size());
    Iterator<Version> releases = setReleases.iterator();
    Version release;
    while (releases.hasNext()) {
      release = releases.next();
      result.add(elmoEntity.getElmoManager().designateEntity(IDoapRelease.class, release));
    }
    return result;
  }

  /**
   * Get all mailing lists associated with this project.
   */
  public Set<IDoapMailingList> getMailingLists() {
    Set<Object> setLists = getProject().getDoapMailingLists();
    Set<IDoapMailingList> result = new HashSet<IDoapMailingList>(setLists.size());
    Iterator<Object> lists = setLists.iterator();
    Object list;
    while (lists.hasNext()) {
      list = lists.next();
      result.add(elmoEntity.getElmoManager().designateEntity(IDoapMailingList.class, list));
    }
    return result;
  }
  
  /**
   * Get all repositories associated with this project.
   */
  public Set<IDoapRepository> getRepositories() {
    Set<Repository> set = getProject().getDoapRepositories();
    Set<IDoapRepository> result = new HashSet<IDoapRepository>(set.size());
    Iterator<Repository> repositories = set.iterator();
    Repository repository;
    while (repositories.hasNext()) {
      repository = repositories.next();
      result.add(elmoEntity.getElmoManager().designateEntity(IDoapRepository.class, repository));
    }
    return result;
  }
  
  /**
   * Get all screen shots associated with this project.
   */
  public Set<IDoapScreenshot> getScreenshots() {
    Set<Object> set = getProject().getDoapScreenshots();
    Set<IDoapScreenshot> result = new HashSet<IDoapScreenshot>(set.size());
    Iterator<Object> resources = set.iterator();
    Object resource;
    while (resources.hasNext()) {
      resource = resources.next();
      result.add(elmoEntity.getElmoManager().designateEntity(IDoapScreenshot.class, resource));
    }
    return result;
  }
  
  /**
   * Get all operating systems this project can be associated with.
   * 
   */
  public Set<String> getOSes() {
    return Utilities.convertToSetOfStrings(getProject().getDoapOses());
  }
  
  /**
   * Get all programming languages this project can be associated with.
   * 
   */
  public Set<String> getProgrammingLanguages() {
    return Utilities.convertToSetOfStrings(getProject().getDoapProgrammingLanguages());
  }
  
  /**
   * Get all wikis associated with this project.
   */
  public Set<IDoapWiki> getWikis() {
    Set<Object> set = getProject().getDoapWikis();
    Set<IDoapWiki> result = new HashSet<IDoapWiki>(set.size());
    Iterator<Object> resources = set.iterator();
    Object resource;
    while (resources.hasNext()) {
      resource = resources.next();
      result.add(elmoEntity.getElmoManager().designateEntity(IDoapWiki.class, resource));
    }
    return result;
  }
  
  /**
   * Get all download pages associated with this project.
   */
  public Set<IDoapDownloadPage> getDownloadPages() {
    Set<Object> set = getProject().getDoapDownloadPages();
    Set<IDoapDownloadPage> result = new HashSet<IDoapDownloadPage>(set.size());
    Iterator<Object> resources = set.iterator();
    Object resource;
    while (resources.hasNext()) {
      resource = resources.next();
      result.add(elmoEntity.getElmoManager().designateEntity(IDoapDownloadPage.class, resource));
    }
    return result;
  }
  
  /**
   * Get all download mirrors associated with this project.
   */
  public Set<IDoapDownloadMirror> getDownloadMirrors() {
    Set<Object> set = getProject().getDoapDownloadMirrors();
    Set<IDoapDownloadMirror> result = new HashSet<IDoapDownloadMirror>(set.size());
    Iterator<Object> resources = set.iterator();
    Object resource;
    while (resources.hasNext()) {
      resource = resources.next();
      result.add(elmoEntity.getElmoManager().designateEntity(IDoapDownloadMirror.class, resource));
    }
    return result;
  }
}

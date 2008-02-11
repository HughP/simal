package uk.ac.osswatch.simal.model.elmo;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.IRCS;
import uk.ac.osswatch.simal.model.IVersion;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * This is a wrapper around an Elmo Project class.
 * 
 * @see org.openrdf.concepts.doap.Project
 */
public class Project extends DoapResource implements IProject {
	private static final long serialVersionUID = -1771017230656089944L;
	
	protected Project() {		
	}

	/**
	 * Create a new wrapper around an elmo Project object.
	 * 
	 * @param simalTestProject
	 */
	public Project(org.openrdf.concepts.doap.Project elmoProject) {
		super(elmoProject);
	}

	@SuppressWarnings("unchecked")
	public Set<Resource> getCategories() {
		return Resource.createResourceSet(getProject().getDoapCategories());
	}

	@SuppressWarnings("unchecked")
	public Set<IPerson> getDevelopers() throws SimalRepositoryException {
		Set<org.openrdf.concepts.rdfs.Resource> resources = (Set) getProject()
				.getDoapDevelopers();
		HashSet<IPerson> people = findPeople(resources);
		return people;
	}

	@SuppressWarnings("unchecked")
	public Set<IPerson> getDocumenters() throws SimalRepositoryException {
		Set<org.openrdf.concepts.rdfs.Resource> resources = (Set) getProject()
				.getDoapDocumenters();
		HashSet<IPerson> people = findPeople(resources);
		return people;
	}

	@SuppressWarnings("unchecked")
	public Set<Resource> getDownloadMirrors() {
		return Resource
				.createResourceSet(getProject().getDoapDownloadMirrors());
	}

	@SuppressWarnings("unchecked")
	public Set<Resource> getDownloadPages() {
		return Resource.createResourceSet(getProject().getDoapDownloadPages());
	}

	@SuppressWarnings("unchecked")
	public Set<IPerson> getHelpers() throws SimalRepositoryException {
		Set<org.openrdf.concepts.rdfs.Resource> resources = (Set) getProject()
				.getDoapHelpers();
		HashSet<IPerson> people = findPeople(resources);
		return people;
	}

	@SuppressWarnings("unchecked")
	public Set<Resource> getHomepages() {
		return Resource.createResourceSet(getProject().getDoapHomepages());
	}

	@SuppressWarnings("unchecked")
	public Set<Resource> getIssueTrackers() {
		return Resource.createResourceSet(getProject().getDoapBugDatabases());
	}

	@SuppressWarnings("unchecked")
	public Set<Resource> getMailingLists() {
		return Resource.createResourceSet(getProject().getDoapMailingLists());
	}

	@SuppressWarnings("unchecked")
	public Set<IPerson> getMaintainers() throws SimalRepositoryException {
		Set<org.openrdf.concepts.rdfs.Resource> resources = (Set) getProject()
				.getDoapMaintainers();
		HashSet<IPerson> people = findPeople(resources);
		return people;
	}

	@SuppressWarnings("unchecked")
	public Set<String> getOSes() {
		return (Set) getProject().getDoapOses();
	}

	@SuppressWarnings("unchecked")
	public Set<Resource> getOldHomepages() {
		return Resource.createResourceSet(getProject().getDoapOldHomepages());
	}

	@SuppressWarnings("unchecked")
	public Set<String> getProgrammingLangauges() {
		return (Set) getProject().getDoapProgrammingLanguages();
	}

	@SuppressWarnings("unchecked")
	public Set<IVersion> getReleases() throws SimalRepositoryException {
		return findReleases((Set) getProject().getDoapReleases());
	}

	@SuppressWarnings("unchecked")
	public Set<IRCS> getRepositories() throws SimalRepositoryException {
		return findRepositories((Set) getProject().getDoapRepositories());
	}

	@SuppressWarnings("unchecked")
	public Set<Resource> getScreenshots() {
		return Resource.createResourceSet(getProject().getDoapScreenshots());
	}

	@SuppressWarnings("unchecked")
	public Set<IPerson> getTesters() throws SimalRepositoryException {
		Set<org.openrdf.concepts.rdfs.Resource> resources = (Set) getProject()
				.getDoapTesters();
		HashSet<IPerson> people = findPeople(resources);
		return people;
	}

	@SuppressWarnings("unchecked")
	public Set<IPerson> getTranslators() throws SimalRepositoryException {
		Set<org.openrdf.concepts.rdfs.Resource> resources = (Set) getProject()
				.getDoapTranslators();
		HashSet<IPerson> people = findPeople(resources);
		return people;
	}

	@SuppressWarnings("unchecked")
	public Set<Resource> getWikis() {
		return Resource.createResourceSet(getProject().getDoapWikis());

	}

	/**
	 * Attempts to find people in the repository that are identified by a given
	 * set of resources.
	 * 
	 * @param resources
	 * @return
	 * @throws SimalRepositoryException
	 */
	@SuppressWarnings("unchecked")
	private HashSet<IPerson> findPeople(
			Set<org.openrdf.concepts.rdfs.Resource> resources)
			throws SimalRepositoryException {
		Iterator<String> uris = getResourceURIs(resources).iterator();
		HashSet<IPerson> people = new HashSet(resources.size());
		String uri;
		while (uris.hasNext()) {
			uri = uris.next();
			people.add(SimalRepository.getPerson(new QName(uri)));
		}
		return people;
	}

	/**
	 * Attempts to find release versions in the repository that are identified
	 * by a given set of resources.
	 * 
	 * @param resources
	 * @return
	 * @throws SimalRepositoryException
	 */
	@SuppressWarnings("unchecked")
	private HashSet<IVersion> findReleases(
			Set<org.openrdf.concepts.rdfs.Resource> resources)
			throws SimalRepositoryException {
		Iterator<String> uris = getResourceURIs(resources).iterator();
		HashSet<IVersion> versions = new HashSet(resources.size());
		String uri;
		while (uris.hasNext()) {
			uri = uris.next();
			versions.add(SimalRepository.getVersion(new QName(uri)));
		}
		return versions;
	}

	/**
	 * Attempts to find version control repository info in the repository that
	 * are identified by a given set of resources.
	 * 
	 * @param resources
	 * @return
	 * @throws SimalRepositoryException
	 * @throws SimalRepositoryException
	 */
	@SuppressWarnings("unchecked")
	private Set<IRCS> findRepositories(
			Set<org.openrdf.concepts.rdfs.Resource> resources)
			throws SimalRepositoryException {
		Iterator<String> uris = getResourceURIs(resources).iterator();
		HashSet<IRCS> repos = new HashSet(resources.size());
		String uri;
		while (uris.hasNext()) {
			uri = uris.next();
			repos.add(SimalRepository.getRCS(new QName(uri)));
		}
		return repos;
	}

	private org.openrdf.concepts.doap.Project getProject() {
		return (org.openrdf.concepts.doap.Project) elmoResource;
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
		
		json.append(", \"category\":" + toJSONValues(getCategories()));
		
		HashSet<Resource> people;
		try {
			people = getAllPeople();
			json.append(", \"person\":" + toJSONValues(people));
		} catch (SimalRepositoryException e) {
			json.append(", \"person\":\"\"" );
		}
		
		json.append(", \"programmingLanguage\":" + toJSONValues(getProgrammingLangauges()));
		return json.toString();
	}

	@SuppressWarnings("unchecked")
	public HashSet<Resource> getAllPeople() throws SimalRepositoryException {
		HashSet<Resource> people = new HashSet<Resource>();
		people.addAll((Collection<? extends Resource>) getMaintainers());
		people.addAll((Collection<? extends Resource>) getDevelopers());
		people.addAll((Collection<? extends Resource>) getDocumenters());
		people.addAll((Collection<? extends Resource>) getHelpers());
		people.addAll((Collection<? extends Resource>) getTesters());
		people.addAll((Collection<? extends Resource>) getTranslators());
		return people;
	}

	private String toJSONValues(Set<?> resources) {
		StringBuffer values = new StringBuffer();
		Iterator<?> itr = resources.iterator();
		Object resource;
		values.append("[");
		while (itr.hasNext()) {
			resource = itr.next();
			if (resource instanceof Resource) {
			  values.append("\"" + ((Resource)resource).getLabel() + "\"");
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

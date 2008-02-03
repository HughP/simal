package uk.ac.osswatch.simal.model.elmo;



import java.util.Set;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.IRepository;
import uk.ac.osswatch.simal.model.IVersion;

/**
 * This is a wrapper around an Elmo Project class.
 * 
 * @see org.openrdf.concepts.doap.Project
 */
public class Project extends DoapResource implements IProject {
	private static final long serialVersionUID = -1771017230656089944L;

	/**
	 * Create a new wrapper around an elmo Project object.
	 * 
	 * @param simalTestProject
	 */
	public Project(org.openrdf.concepts.doap.Project elmoProject) {
		this.elmoProject = elmoProject;
	}

	public Set<String> getCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IPerson> getDevelopers() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IPerson> getDocumentors() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> getDownloadMirrors() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> getDownloadPages() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IPerson> getHelpers() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> getHomepages() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> getIssueTracker() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> getMailingLists() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IPerson> getMaintainers() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> getOSes() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> getOldHomepages() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> getProgrammingLangauges() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IVersion> getReleases() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IRepository> getRepositories() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> getScreenshots() {
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

	public Set<String> getWikis() {
		// TODO Auto-generated method stub
		return null;
	}

}

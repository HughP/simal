package uk.ac.osswatch.simal.rdf.jena;

import java.util.HashSet;
import java.util.Set;

import uk.ac.osswatch.simal.model.Doap;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.jena.Project;
import uk.ac.osswatch.simal.rdf.IProjectService;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;

/**
 * A class for working with projects in the repository.
 * 
 */
public class ProjectService extends AbstractService implements IProjectService {
	static ProjectService service;

	ProjectService(SimalRepository simalRepository) {
		setRepository(simalRepository);
	};
	
	public Set<IProject> getProjectsWithRCS() {
		return getProjectsWith(Doap.REPOSITORY);
	}

	public Set<IProject> getProjectsWithHomepage() {
		return getProjectsWith(Doap.HOMEPAGE);
	}

	public Set<IProject> getProjectsWithMaintainer() {
		return getProjectsWith(Doap.MAINTAINER);
	}

	public Set<IProject> getProjectsWithMailingList() {
		return getProjectsWith(Doap.MAILING_LIST);
	}

	public Set<IProject> getProjectsWithBugDatabase() {
		return getProjectsWith(Doap.BUG_DATABASE);
	}

	public Set<IProject> getProjectsWithRelease() {
		return getProjectsWith(Doap.RELEASE);
	}
	
	private Set<IProject> getProjectsWith(Property property) {
		SimalRepository simalRepository = (SimalRepository)getRepository();
		Model model = simalRepository.getModel();
        ResIterator resources = model.listResourcesWithProperty(property);
        
        Set<IProject> projects = new HashSet<IProject>();
	    while (resources.hasNext()) {
	      projects.add(new Project((com.hp.hpl.jena.rdf.model.Resource) resources.next()));
	    }
	    return projects;
	}
	
}

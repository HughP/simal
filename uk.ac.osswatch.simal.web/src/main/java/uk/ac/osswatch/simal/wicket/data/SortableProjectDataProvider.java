package uk.ac.osswatch.simal.wicket.data;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

import uk.ac.osswatch.simal.model.elmo.Project;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A project data provider that allows the projects to be sorted.
 * 
 */
public class SortableProjectDataProvider extends SortableDataProvider {
	private static final long serialVersionUID = -7078982000589847543L;

	public static final String SORT_PROPERTY_NAME = "name";
	public static final String SORT_PROPERTY_SHORTDESC = "shortDesc";
	/**
	 * the set of projects we are providing access to.
	 */
	private Set<Project> projects;

	/**
	 * Create a SortableDataProvider containing all projects in the repository
	 * 
	 * @param size
	 * @throws SimalRepositoryException 
	 */
	public SortableProjectDataProvider() throws SimalRepositoryException {
		projects = SimalRepository.getAllProjects();
	}
	
	

	@Override
	public void setSort(SortParam param) {
		if (validateSortProperty(param.getProperty())) {
			super.setSort(param);
		} else {
			throw new RuntimeException("Set an unknown sort property: " + param.getProperty());
		}
	}
	
	@Override
	public void setSort(String property, boolean isAscending) {
		if (validateSortProperty(property)) {
		  super.setSort(property, isAscending);
		} else {
		  throw new RuntimeException("Set an unknown sort property: " + property);
		}
	}


    /**
     * Ensures a sort property is valid, that is Projects can be sorted by it.
     * @param property
     * @return
     */
	private boolean validateSortProperty(String property) {
		return property.equals(SORT_PROPERTY_NAME) || property.equals(SORT_PROPERTY_SHORTDESC);
	}



	public Iterator<Project> iterator(int first, int count) {
		ProjectComparator comparator = new ProjectComparator();
		TreeSet<Project> treeSet = new TreeSet<Project>(comparator);
		treeSet.addAll(projects);
		TreeSet<Project> result = new TreeSet<Project>(comparator);
		int idx = 0;
		Iterator<Project> all = treeSet.iterator();
		Project current;
		while (all.hasNext() && idx - count < 0) {
			current = all.next();
			if (idx >= first) {
				result.add(current);
			}
			idx++;
		}
		return result.iterator();
	}

	public IModel model(Object object) {
		if (!(object instanceof Project)) {
			throw new IllegalArgumentException("sortableProjectDataProvider only works for Project models");
		}
		return new DetachableProjectModel((Project)object);
	}

	public int size() {
		return projects.size();
	}

	private class ProjectComparator implements Comparator<Project> {

		public int compare(Project project1, Project project2) {
			if (project1.equals(project2)) { return 0; }
			
			int result = 0;
			
			String sortField;
			if (getSort() == null) {
				sortField = "name";
			} else {
				sortField = getSort().getProperty();
			}
			if (sortField.equals(SORT_PROPERTY_NAME)) {
				String name1 = (String) project1.getName();
				String name2 = (String) project2.getName();
				result = name1.compareTo(name2);
			} else if (sortField.equals(SORT_PROPERTY_SHORTDESC)) {
				String desc1 = project1.getShortDesc();
				String desc2 = project2.getShortDesc();
				if (desc1 == null) { 
					result = 1;
				} else if (desc2 == null) {
					result = -1;
				} else {
					result = desc1.compareTo(desc2);
				}
			}
			if (result == 0) {
				result = 1;
			}
			return result;
		}
	}
}

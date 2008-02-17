package uk.ac.osswatch.simal.wicket.data;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

import uk.ac.osswatch.simal.model.IDoapResource;
import uk.ac.osswatch.simal.model.elmo.Project;

/**
 * An IDoapResource data provider that allows the IDoapResources to be sorted.
 * 
 */
public class SortableDoapResourceDataProvider extends SortableDataProvider {
	private static final long serialVersionUID = -6674850425804180338L;
	
	public static final String SORT_PROPERTY_NAME = "name";
	public static final String SORT_PROPERTY_SHORTDESC = "shortDesc";
	
	/**
	 * the set of DoapResources we are providing access to.
	 */
	private Set<IDoapResource> resources;

	/** 
	 * Create a data provider for the supplied resources.
	 */
	@SuppressWarnings("unchecked")
	public SortableDoapResourceDataProvider(Set<? extends IDoapResource> resources) {
		this.resources = (Set<IDoapResource>) resources;
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
     * Ensures a sort property is valid, that is IDoapResources can be sorted by it.
     * @param property
     * @return
     */
	private boolean validateSortProperty(String property) {
		return property.equals(SORT_PROPERTY_NAME) || property.equals(SORT_PROPERTY_SHORTDESC);
	}



	public Iterator<IDoapResource> iterator(int first, int count) {
		IDoapResourceComparator comparator = new IDoapResourceComparator();
		TreeSet<IDoapResource> treeSet = new TreeSet<IDoapResource>(comparator);
		treeSet.addAll(resources);
		TreeSet<IDoapResource> result = new TreeSet<IDoapResource>(comparator);
		int idx = 0;
		Iterator<IDoapResource> all = treeSet.iterator();
		IDoapResource current;
		while (all.hasNext() && idx - count < 0) {
			current = all.next();
			if (idx >= first) {
				result.add(current);
			}
			idx++;
		}
		return result.iterator();
	}

	/**
	 * Get an iterator over all resources. The resources will be sorted
	 * accordingly.
	 * 
	 * @return
	 */
	public Iterator<IDoapResource> iterator() {
		return iterator(0, resources.size());
	}

	public IModel model(Object object) {
		if (!(object instanceof Project)) {
			throw new IllegalArgumentException("sortableDoapResourceDataProvider only works for Project models - should it work for more? Your helpo appreciated.");
		}
		return new DetachableProjectModel((Project)object);
	}

	public int size() {
		return resources.size();
	}

	private class IDoapResourceComparator implements Comparator<IDoapResource> {

		public int compare(IDoapResource resource1, IDoapResource resource2) {
			if (resource1.equals(resource2)) { return 0; }
			
			int result = 0;
			
			String sortField;
			if (getSort() == null) {
				sortField = "name";
			} else {
				sortField = getSort().getProperty();
			}
			if (sortField.equals(SORT_PROPERTY_NAME)) {
				String name1 = (String) resource1.getName();
				String name2 = (String) resource2.getName();
				result = name1.compareTo(name2);
			} else if (sortField.equals(SORT_PROPERTY_SHORTDESC)) {
				String desc1 = resource1.getShortDesc();
				String desc2 = resource2.getShortDesc();
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

package uk.ac.osswatch.simal.wicket.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.junit.Test;

import uk.ac.osswatch.simal.model.elmo.Project;

public class TestSortableProjectDataProvider {

	private static final int NUMBER_OF_TEST_PROJECTS = 3;

	@Test
	public void testSize() {
		SortableProjectDataProvider provider = new SortableProjectDataProvider();
		assertEquals(NUMBER_OF_TEST_PROJECTS, provider.size());
	}

	@Test
	public void testModel() {
		SortableProjectDataProvider provider = new SortableProjectDataProvider();
		try {
			provider.model("should throw an IllegalArgumentxception");
			fail("Failed to throw IllegalArgumentExcpetion when supplying a string");
		} catch (IllegalArgumentException e) {
			// test passed
		}
		
		// assertNotNull(provider.model());
	}

	@Test
	public void testIterator() {
		SortableProjectDataProvider provider = new SortableProjectDataProvider();
		int pageSize = NUMBER_OF_TEST_PROJECTS - 1;

		// test the default sort is by name
		Iterator<Project> iterator = provider.iterator(0, pageSize);
		Project project;
		String prev = null;
		String current;
		int count = 0;
		while (iterator.hasNext()) {
			project = iterator.next();
			current = (String) project.getName();
			if (prev != null) {
				assertTrue("Incorrect sort order: " + prev + " preceeds "
						+ current, current.compareTo(prev) >= 0);
			}
			prev = current;
			count = count + 1;
		}

		assertEquals("not returning the right number of elements for the given start point and pageSize", pageSize,
				count);

		// test the sort is by shortDesc
		provider.setSort(SortableProjectDataProvider.SORT_PROPERTY_SHORTDESC,
				true);
		iterator = provider.iterator(0, 10);
		prev = null;
		count = 0;
		while (iterator.hasNext()) {
			project = iterator.next();
			current = project.getShortDesc();
			System.out.println(current);
			if (prev != null && current != null) {
				assertTrue("Incorrect sort order: " + prev + " preceeds "
						+ current, current.compareTo(prev) >= 0);
			}
			prev = current;
			count = count + 1;
		}

		assertEquals("not returning the right number of elements",
				NUMBER_OF_TEST_PROJECTS, count);

		boolean threwRuntime = false;
		try {
			provider.setSort("Unknown property", true);
		} catch (RuntimeException e) {
			threwRuntime = true;
		}
		assertTrue(
				"Didn't throw a RuntimeException with an illegal sort property",
				threwRuntime);

	}
}

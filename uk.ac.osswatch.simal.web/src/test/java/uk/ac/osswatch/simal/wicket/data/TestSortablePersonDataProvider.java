package uk.ac.osswatch.simal.wicket.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.junit.Test;

import uk.ac.osswatch.simal.model.IFoafResourceBehaviour;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.TransactionException;
import uk.ac.osswatch.simal.wicket.TestBase;

public class TestSortablePersonDataProvider extends TestBase{

	@Test
	public void testSize() throws SimalRepositoryException, TransactionException {
		SortablePersonDataProvider provider = new SortablePersonDataProvider();
		assertEquals(NUMBER_OF_TEST_PEOPLE, provider.size());
	}

	@Test
	public void testModel() throws SimalRepositoryException {
		SortablePersonDataProvider provider = new SortablePersonDataProvider();
		try {
			provider.model("should throw an IllegalArgumentxception");
			fail("Failed to throw IllegalArgumentExcpetion when supplying a string");
		} catch (IllegalArgumentException e) {
			// test passed
		}
	}

	@Test
	public void testIterator() throws SimalRepositoryException {
		SortablePersonDataProvider provider = new SortablePersonDataProvider();
		int pageSize = NUMBER_OF_TEST_PEOPLE - 1;

		// test the default sort is by label
		Iterator<IFoafResourceBehaviour> iterator = provider.iterator(0, pageSize);
		IFoafResourceBehaviour person;
		String prev = null;
		String current;
		int count = 0;
		while (iterator.hasNext()) {
			person = iterator.next();
			current = (String) person.getLabel();
			if (prev != null) {
				assertTrue("Incorrect sort order: " + prev + " preceeds "
						+ current, current.compareTo(prev) >= 0);
			}
			prev = current;
			count = count + 1;
		}

		assertEquals("not returning the right number of elements for the given start point and pageSize", pageSize,
				count);

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

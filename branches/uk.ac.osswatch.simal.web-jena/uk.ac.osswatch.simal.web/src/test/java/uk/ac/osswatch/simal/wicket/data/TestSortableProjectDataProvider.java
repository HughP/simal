package uk.ac.osswatch.simal.wicket.data;
/*
 * Copyright 2008 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");   *
 * you may not use this file except in compliance with the License.  *
 * You may obtain a copy of the License at                           *
 *                                                                   *
 *   http://www.apache.org/licenses/LICENSE-2.0                      *
 *                                                                   *
 * Unless required by applicable law or agreed to in writing,        *
 * software distributed under the License is distributed on an       *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY            *
 * KIND, either express or implied.  See the License for the         *
 * specific language governing permissions and limitations           *
 * under the License.                                                *
 */


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.junit.Test;

import uk.ac.osswatch.simal.model.IDoapResource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.TransactionException;
import uk.ac.osswatch.simal.wicket.TestBase;

public class TestSortableProjectDataProvider extends TestBase{

	@Test
	public void testSize() throws SimalRepositoryException, TransactionException {
		SortableProjectDataProvider provider = new SortableProjectDataProvider();
		assertEquals(NUMBER_OF_TEST_PROJECTS, provider.size());
	}

	@Test
	public void testModel() throws SimalRepositoryException {
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
	public void testIterator() throws SimalRepositoryException {
		SortableProjectDataProvider provider = new SortableProjectDataProvider();
		int pageSize = NUMBER_OF_TEST_PROJECTS - 1;

		// test the default sort is by name
		Iterator<IDoapResource> iterator = provider.iterator(0, pageSize);
		IDoapResource project;
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

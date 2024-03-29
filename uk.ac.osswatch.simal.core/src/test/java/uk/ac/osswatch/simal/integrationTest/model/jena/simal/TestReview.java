package uk.ac.osswatch.simal.integrationTest.model.jena.simal;
/*
 * Copyright 2007 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static junit.framework.Assert.assertEquals;

import java.util.Calendar;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.integrationTest.model.repository.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.simal.IReview;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.IReviewService;

public class TestReview extends BaseRepositoryTest {
	
	static IReview review;

    @BeforeClass
	public static void getTestReview() throws SimalRepositoryException {
		IReviewService service = SimalRepositoryFactory.getReviewService();
		Set<IReview> reviews = service.getReviews();
		review = (IReview) reviews.toArray()[0];
	}

	@Test
	public void testPerson() throws SimalException {
		assertEquals("Got the wrong person for this review", "http://people.apache.org/~rgardler/#me", review.getReviewer().getURI());
	}

	
	@Test
	public void testProject() throws SimalException {
		IProject project = review.getProject();
		assertEquals("We have the wrong project for the test review", "http://simal.oss-watch.ac.uk/simalTest#", project.getURI());
	}
	
	@Test
	public void setSetProject() throws DuplicateURIException, SimalException {
		IReviewService service = SimalRepositoryFactory.getReviewService();
		IReview review = service.create("http://test.org/Review");
		review.setProject(project1);
		
		IProject project = review.getProject();
		assertEquals("Incorrectly setting the project", project1.getName(), project.getName());
		review.delete();
	}
	
	@Test
	public void getDate() throws SimalException {
		Calendar date = review.getDate();
		assertEquals("We have the wrong year in the date for the test review", 2009, date.get(Calendar.YEAR));
		assertEquals("We have the wrong month in the date for the test review", 4, date.get(Calendar.MONTH));
		assertEquals("We have the wrong day in the date for the test review", 22, date.get(Calendar.DATE));
	}
	
	@Test
	public void getShortDate() {
		String date = review.getShortDate();
		assertEquals("Short date is not correct", "22/05/09", date);
	}
}

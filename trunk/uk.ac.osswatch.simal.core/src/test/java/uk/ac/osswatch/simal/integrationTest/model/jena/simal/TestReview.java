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
import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Set;

import org.junit.Test;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.simal.IReview;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.IReviewService;
import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestReview extends BaseRepositoryTest {

	@Test
	public void testPerson() throws SimalException {
		IReviewService service = getRepository().getReviewService();
		Set<IReview> reviews = service.getReviews();
		IReview review = (IReview) reviews.toArray()[0];
		assertEquals("Got the wrong person for this review", "http://people.apache.org/~rgardler/#me", review.getReviewer().getURI());
	}

	
	@Test
	public void testProject() throws SimalException {
		IReviewService service = getRepository().getReviewService();
		Set<IReview> reviews = service.getReviews();
		IReview review = (IReview)reviews.toArray()[0];
		IProject project = review.getProject();
		assertEquals("We have the wrong project for the test review", "http://simal.oss-watch.ac.uk/simalTest#", project.getURI());
	}
	
	@Test
	public void setSetProject() throws DuplicateURIException, SimalException {
		IReviewService service = getRepository().getReviewService();
		IReview review = service.create("http://test.org/Review");
		review.setProject(project1);
		
		IProject project = review.getProject();
		assertEquals("Incorrectly setting the project", project1.getName(), project.getName());
	}
	
	@Test
	public void getDate() throws SimalException {
		IReviewService service = getRepository().getReviewService();
		Set<IReview> reviews = service.getReviews();
		IReview review = (IReview)reviews.toArray()[0];
		Calendar date = review.getDate();
		assertEquals("We have the wrong year in the date for the test review", 2009, date.get(Calendar.YEAR));
		assertEquals("We have the wrong month in the date for the test review", 4, date.get(Calendar.MONTH));
		assertEquals("We have the wrong day in the date for the test review", 22, date.get(Calendar.DATE));
	}
	
	@Test
	public void getShortDate() {
		IReviewService service = getRepository().getReviewService();
		Set<IReview> reviews = service.getReviews();
		IReview review = (IReview)reviews.toArray()[0];
		String date = review.getShortDate();
		assertEquals("Short date is not correct", "5/22/2009", date);
	}
}

package uk.ac.osswatch.simal.integrationTest.rdf;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.simal.IReview;
import uk.ac.osswatch.simal.rdf.IProjectService;
import uk.ac.osswatch.simal.rdf.IReviewService;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestReviewService extends BaseRepositoryTest {
  private static final Logger logger = LoggerFactory
      .getLogger(TestReviewService.class);

	@Test
	public void getAllReviews() {
		IReviewService service = getRepository().getReviewService();
		Set<IReview> reviews = service.getReviews();
		Iterator<IReview> itr = reviews.iterator();
		while (itr.hasNext()) {
			logger.debug(itr.next().toString());
		}
		assertEquals("We have an incorrect number of reviews in the repository", 1, reviews.size());
		IReview review = (IReview)reviews.toArray()[0];
		assertEquals("URI of review is incorrect", "http://simal.oss-watch.ac.uk/Review#testReview", review.getURI());
	}
	
	@Test
	public void getReview() {
		IReviewService service = getRepository().getReviewService();
		IReview review = service.getReview("http://simal.oss-watch.ac.uk/Review#testReview");
		assertNotNull("Failed to get the review from the repository", review);
	}

	  
	  @Test
	  public void testGetReviewsForProject() throws SimalRepositoryException {
		IReviewService service = getRepository().getReviewService();
		IProjectService projectService = getRepository().getProjectService();
		Set<IReview> reviews = service.getReviewsForProject(projectService.getProject(testProjectURI));  
		assertTrue("We should have a project review", 1 == reviews.size());
		IReview review = (IReview) reviews.toArray()[0];
		String label = review.getLabel();
		assertTrue("Review label is incorrect: " + label, label.contains("performed by"));
		assertFalse("Review label is incorrect: " + label, label.contains("null"));
		assertFalse("Review label is incorrect: " + label, label.toLowerCase(Locale.getDefault()).contains("unknown"));
	  }
}

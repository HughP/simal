package uk.ac.osswatch.simal.service.jcr;
/*
 * Copyright 2007 University of Oxford 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */
import java.util.Set;

import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.simal.IReview;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.AbstractService;
import uk.ac.osswatch.simal.service.IReviewService;

public class JcrReviewService extends AbstractService implements IReviewService {

	public JcrReviewService(ISimalRepository simalRepository) {
		super(simalRepository);
	}

	public IReview findReviewById(String id) throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getNewReviewID() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IReview getReview(String uri) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IReview> getReviews() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IReview> getReviewsForProject(IProject project)
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IReview create(String uri) throws DuplicateURIException,
			SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

}

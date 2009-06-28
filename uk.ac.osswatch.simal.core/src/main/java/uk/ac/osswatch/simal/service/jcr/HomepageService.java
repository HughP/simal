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
import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.jcr.Homepage;
import uk.ac.osswatch.simal.model.jcr.JcrSimalRepository;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.AbstractService;
import uk.ac.osswatch.simal.service.IHomepageService;

public class HomepageService extends AbstractService implements IHomepageService {

	public HomepageService(ISimalRepository repo) {
		super(repo);
	}

	public IDoapHomepage createHomepage(String uri)
			throws SimalRepositoryException, DuplicateURIException {
		if (uri == null || uri.length() == 0) {
			throw new SimalRepositoryException("URI cannot be blank or null");
		}
	    if (getRepository().containsResource(uri)) {
	      throw new DuplicateURIException(
	          "Attempt to create a second homepage with the URI " + uri);
	    }

	    Homepage page= new Homepage(getRepository().getEntityID(getNewHomepageID()));
	    ((JcrSimalRepository)SimalRepositoryFactory.getInstance()).getObjectContentManager().insert(page);
	    
	    return page;
	}

	public String getNewHomepageID() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IHomepageService getOrCreateHomepage(String url)
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

}

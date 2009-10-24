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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IDoapRelease;
import uk.ac.osswatch.simal.model.jcr.JcrSimalRepository;
import uk.ac.osswatch.simal.model.jcr.Release;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.AbstractService;
import uk.ac.osswatch.simal.service.IReleaseService;

public class JcrReleaseService extends AbstractService implements IReleaseService {
  public static final Logger logger = LoggerFactory
      .getLogger(JcrReleaseService.class);

	public JcrReleaseService(ISimalRepository repo) {
		super(repo);
	}

	public IDoapRelease create(String uri)
			throws SimalRepositoryException, DuplicateURIException {
		if (uri == null || uri.length() == 0) {
			throw new SimalRepositoryException("URI cannot be blank or null");
		}
	    if (getRepository().containsResource(uri)) {
	      throw new DuplicateURIException(
	          "Attempt to create a second release with the URI " + uri);
	    }

	    Release release= new Release(getRepository().getEntityID(getNewID()));
	    ((JcrSimalRepository)SimalRepositoryFactory.getInstance()).getObjectContentManager().insert(release);
	    
	    return release;
	}

	public String getNewID() throws SimalRepositoryException {
	    String fullID = null;
	    String strEntityID = SimalProperties.getProperty(
	        SimalProperties.PROPERTY_SIMAL_NEXT_RELEASE_ID, "1");
	    long entityID = Long.parseLong(strEntityID);

	    fullID = getRepository().getUniqueSimalID("page" + Long.toString(entityID));

	    long newId = entityID + 1;
	    SimalProperties.setProperty(SimalProperties.PROPERTY_SIMAL_NEXT_RELEASE_ID,
	        Long.toString(newId));
	    try {
	      SimalProperties.save();
	    } catch (Exception e) {
	      logger.warn("Unable to save properties file", e);
	      throw new SimalRepositoryException(
	          "Unable to save properties file when creating the next project ID", e);
	    }
	    logger.debug("Generated new homepage ID {}", fullID);
	    return fullID;
	}

	public IReleaseService getOrCreate(String url)
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

}

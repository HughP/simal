package uk.ac.osswatch.simal.service.jena;
/*
 * Copyright 2007,2010 University of Oxford 
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
import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.model.jena.simal.JenaSimalRepository;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.AbstractService;

import com.hp.hpl.jena.rdf.model.Model;

public class JenaService extends AbstractService {

  public static final Logger LOGGER = LoggerFactory
  .getLogger(JenaService.class);

  /**
	 * Create new JenaService but only for ISimalRepository
	 * objects that are of type JenaSimalRepository
	 * @param simalRepository
	 */
	public JenaService(ISimalRepository simalRepository) {
		super(simalRepository);
		
		if (!(simalRepository instanceof JenaSimalRepository)) {
		  throw new UnsupportedOperationException();
		}
	}

	@Override
	public void save(IResource resource) throws SimalRepositoryException {
		// no specific action required
	}

	/**
	 * Get the Model for this JenaSimalRepository 
	 * @return
	 */
	protected Model getModel() {
	  return ((JenaSimalRepository)getRepository()).getModel();
	}
	
  protected String getNewID(String propertyName, String defaultIdPrefix) throws SimalRepositoryException {
    String fullID = null;
    String strEntityID = SimalProperties.getProperty(
        propertyName, "1");
    long entityID = Long.parseLong(strEntityID);

    /**
     * If the properties file is lost for any reason the next ID value will
     * be lost. We therefore need to perform a sanity check that this is
     * unique.
     */
    boolean validID = false;
    int attempts = 0;
    while (!validID && ++attempts < 10) {
      fullID = getRepository().getUniqueSimalID(
          defaultIdPrefix + Long.toString(entityID));
      if (idAvailable(fullID)) {
        validID = true;
      } else {
        entityID = entityID + 1;
      }
    }
    
    if(!validID) {
      throw new SimalRepositoryException("Could not find available ID for property " + propertyName);
    }

    long newId = entityID + 1;
    SimalProperties.setProperty(propertyName, Long
            .toString(newId));
    try {
      SimalProperties.save();
    } catch (Exception e) {
      LOGGER.warn("Unable to save properties file", e);
      throw new SimalRepositoryException(
          "Unable to save properties file when creating the next category ID",
          e);
    }
    return fullID;
  }

  private boolean idAvailable(String id) {
  // FIXME Check on ID 
  return true;
  }

	
}

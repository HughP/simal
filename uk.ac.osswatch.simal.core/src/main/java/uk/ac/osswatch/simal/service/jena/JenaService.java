package uk.ac.osswatch.simal.service.jena;
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

import com.hp.hpl.jena.rdf.model.Model;

import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.model.jena.simal.JenaSimalRepository;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.AbstractService;

public class JenaService extends AbstractService {

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
}

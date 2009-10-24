package uk.ac.osswatch.simal.service;
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
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.model.jcr.JcrSimalRepository;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public abstract class AbstractService implements IService {
	ISimalRepository repo;
	

	public AbstractService(ISimalRepository simalRepository) {
		setRepository(simalRepository);
	}

	/**
	 * Set the repository this service is to work on.
	 * @param simalRepository
	 */
	protected void setRepository(ISimalRepository simalRepository) {
		this.repo = simalRepository;
	}
	
	/**
	 * Return the repository this service is operating on.
	 */
	protected ISimalRepository getRepository() {
		return this.repo;
	}
	

    /**
     * Save a resource.
     * 
     * @param project
     * @throws SimalRepositoryException 
     */
	public void save(IResource resource) throws SimalRepositoryException {
	    ObjectContentManager ocm = ((JcrSimalRepository)SimalRepositoryFactory.getInstance()).getObjectContentManager();
	    ocm.update(resource);
	    ocm.save();
	}
	
}

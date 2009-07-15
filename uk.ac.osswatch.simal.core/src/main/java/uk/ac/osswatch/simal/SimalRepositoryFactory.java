package uk.ac.osswatch.simal;

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

import uk.ac.osswatch.simal.model.jcr.JcrSimalRepository;
import uk.ac.osswatch.simal.model.jena.simal.JenaSimalRepository;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.ICategoryService;
import uk.ac.osswatch.simal.service.IHomepageService;
import uk.ac.osswatch.simal.service.IOrganisationService;
import uk.ac.osswatch.simal.service.IPersonService;
import uk.ac.osswatch.simal.service.IProjectService;
import uk.ac.osswatch.simal.service.IRepositoryService;
import uk.ac.osswatch.simal.service.IReviewService;
import uk.ac.osswatch.simal.service.jcr.JcrBugDatabaseService;
import uk.ac.osswatch.simal.service.jcr.JcrCategoryService;
import uk.ac.osswatch.simal.service.jcr.JcrHomepageService;
import uk.ac.osswatch.simal.service.jcr.JcrOrganisationService;
import uk.ac.osswatch.simal.service.jcr.JcrPersonService;
import uk.ac.osswatch.simal.service.jcr.JcrProjectService;
import uk.ac.osswatch.simal.service.jcr.JcrRepositoryService;
import uk.ac.osswatch.simal.service.jcr.JcrReviewService;
import uk.ac.osswatch.simal.service.jena.JenaCategoryService;
import uk.ac.osswatch.simal.service.jena.JenaHomepageService;
import uk.ac.osswatch.simal.service.jena.JenaOrganisationService;
import uk.ac.osswatch.simal.service.jena.JenaPersonService;
import uk.ac.osswatch.simal.service.jena.JenaProjectService;
import uk.ac.osswatch.simal.service.jena.JenaReviewService;

public class SimalRepositoryFactory {
  private static final Logger logger = LoggerFactory
      .getLogger(SimalRepositoryFactory.class);
  public static final int JENA = 1;
  public static final int JCR = 2;
  private static int repoType = JENA;
  
  /**
   * Get the SimalRepository object. Note that only one of each type can exist
   * in a single virtual machine.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public static ISimalRepository getInstance()
      throws SimalRepositoryException {
	  return getInstance(repoType);
  }
  
  /**
   * Get the SimalRepository object. Note that only one of each type can exist
   * in a single virtual machine.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public static ISimalRepository getInstance(int type)
      throws SimalRepositoryException {
	  repoType = type;
	  switch (repoType) {
	    case JENA:
	      return JenaSimalRepository.getInstance();
	    case JCR:  
	      return JcrSimalRepository.getInstance();
	    default:
	      throw new SimalRepositoryException("Attempt to create an unknown repository type");
	  }
  }
	
	/**
	 * Get an instance of a homepage service operating on this repository.
	 * @return
	 * @throws SimalRepositoryException 
	 * 
	 */
	public static IHomepageService getHomepageService() throws SimalRepositoryException {
		 switch (repoType) {
		    case JENA:
		      return new JenaHomepageService(getInstance());
		    case JCR:  
		      return new JcrHomepageService(getInstance());
		    default:
		      throw new SimalRepositoryException("Attempt to create an unknown repository type");
		  }
	}
  

	/**
	 * Get an instance of a project service operating on this repository.
	 * @param repo
	 * @return
	 * @throws SimalRepositoryException 
	 * 
	 */
	public static IProjectService getProjectService() throws SimalRepositoryException {
		 switch (repoType) {
		    case JENA:
		      return new JenaProjectService(getInstance());
		    case JCR:  
		      return new JcrProjectService(getInstance());
		    default:
		      throw new SimalRepositoryException("Attempt to create an unknown repository type");
		  }	
	}
	
	/**
	 * Get an instance of a review service operating on this repository.
	 * @return
	 * @throws SimalRepositoryException 
	 * 
	 */
	public static IReviewService getReviewService() throws SimalRepositoryException {
		 switch (repoType) {
		    case JENA:
		      return new JenaReviewService(getInstance());
		    case JCR:  
		      return new JcrReviewService(getInstance());
		    default:
		      throw new SimalRepositoryException("Attempt to create an unknown repository type");
		  }
	}
	
	/**
	 * Get an instance of a review service operating on this repository.
	 * @return
	 * @throws SimalRepositoryException 
	 * 
	 */
	public static IOrganisationService getOrganisationService() throws SimalRepositoryException {
		 switch (repoType) {
		    case JENA:
		      return new JenaOrganisationService(getInstance());
		    case JCR:  
		      return new JcrOrganisationService(getInstance());
		    default:
		      throw new SimalRepositoryException("Attempt to create an unknown repository type");
		  }
	}
	
	/**
	 * Get an instance of a category service.
	 * @return
	 * @throws SimalRepositoryException 
	 * 
	 */
	public static ICategoryService getCategoryService() throws SimalRepositoryException {
		 switch (repoType) {
		    case JENA:
		      return new JenaCategoryService(getInstance());
		    case JCR: 
			      return new JcrCategoryService(getInstance()); 
		    default:
		      throw new SimalRepositoryException("Attempt to create an unknown repository type");
		  }
	}
	
	/**
	 * Get an instance of a person service.
	 * @return
	 * @throws SimalRepositoryException 
	 * 
	 */
	public static IPersonService getPersonService() throws SimalRepositoryException {
		 switch (repoType) {
		    case JENA:
		      return new JenaPersonService(getInstance());
		    case JCR: 
		      return new JcrPersonService(getInstance());
		    default:
		      throw new SimalRepositoryException("Attempt to create an unknown repository type");
		  }
	}

	/**
	 * Get an instance of the revision control repository service.
	 * @return
	 * @throws SimalRepositoryException 
	 */
	public static IRepositoryService getRepositoryService() throws SimalRepositoryException {
		 switch (repoType) {
		    case JENA:
		      // return new JenaRepositoryService(getInstance());
		      logger.error("Not implemented JenaRepositoryService");
		      System.exit(1);
		    case JCR: 
		      return new JcrRepositoryService(getInstance());
		    default:
		      throw new SimalRepositoryException("Attempt to create an unknown repository type");
		  }
	}

	/**
	 * Get an instance of the bug database service.
	 * @return
	 * @throws SimalRepositoryException 
	 */
	public static JcrBugDatabaseService getBugDatabaseService() throws SimalRepositoryException {
		 switch (repoType) {
		    case JENA:
		      // return new JenaRepositoryService(getInstance());
		      logger.error("Not implemented JenaBugDatabaseService");
		      System.exit(1);
		    case JCR: 
		      return new JcrBugDatabaseService(getInstance());
		    default:
		      throw new SimalRepositoryException("Attempt to create an unknown type of bug datatbase service");
		  }
	}
}

package uk.ac.osswatch.simal;

/*
 * Copyright 2007, 2010 University of Oxford 
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

import uk.ac.osswatch.simal.model.jena.simal.JenaSimalRepository;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.ICategoryService;
import uk.ac.osswatch.simal.service.IDocumentService;
import uk.ac.osswatch.simal.service.IOrganisationService;
import uk.ac.osswatch.simal.service.IPersonService;
import uk.ac.osswatch.simal.service.IProjectService;
import uk.ac.osswatch.simal.service.IRepositoryService;
import uk.ac.osswatch.simal.service.IReviewService;
import uk.ac.osswatch.simal.service.jena.JenaCategoryService;
import uk.ac.osswatch.simal.service.jena.JenaDocumentService;
import uk.ac.osswatch.simal.service.jena.JenaOrganisationService;
import uk.ac.osswatch.simal.service.jena.JenaPersonService;
import uk.ac.osswatch.simal.service.jena.JenaProjectService;
import uk.ac.osswatch.simal.service.jena.JenaRepositoryService;
import uk.ac.osswatch.simal.service.jena.JenaReviewService;

public class SimalRepositoryFactory {
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
	      // commented out as this code moved to a branch - do we need to remove it yet?
	      // return JcrSimalRepository.getInstance();
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
	public static IDocumentService getHomepageService() throws SimalRepositoryException {
		 switch (repoType) {
		    case JENA:
		      return new JenaDocumentService(getInstance());
		    case JCR:  
		    	// commented out as this code moved to a branch - do we need to remove it yet?
			    // return new JcrHomepageService(getInstance());
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
		    	// commented out as this code moved to a branch - do we need to remove it yet?
			    // return new JcrProjectService(getInstance());
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
		    	// commented out as this code moved to a branch - do we need to remove it yet?
			    // return new JcrReviewService(getInstance());
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
		    	// commented out as this code moved to a branch - do we need to remove it yet?
			    // return new JcrOrganisationService(getInstance());
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
		    	// commented out as this code moved to a branch - do we need to remove it yet?
			      // return new JcrCategoryService(getInstance()); 
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
		    	// commented out as this code moved to a branch - do we need to remove it yet?
			      // return new JcrPersonService(getInstance());
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
		      return new JenaRepositoryService(getInstance());
		    case JCR: 
		    	// commented out as this code moved to a branch - do we need to remove it yet?
			      // return new JcrRepositoryService(getInstance());
		    default:
		      throw new SimalRepositoryException("Attempt to create an unknown repository type");
		  }
	}

	/**
	 * Get an instance of the bug database service.
	 * @return
	 * @throws SimalRepositoryException
	 * 
	 * commented out as this code moved to a branch - do we need to remove it yet?
	 * 
	public static JcrBugDatabaseService getBugDatabaseService() throws SimalRepositoryException {
		 switch (repoType) {
		    case JENA:
		      // return new JenaRepositoryService(getInstance());
		      throw new SimalRepositoryException("Not implemented JenaBugDatabaseService");		      
		    case JCR: 
		    	// commented out as this code moved to a branch - do we need to remove it yet?
			      // return new JcrBugDatabaseService(getInstance());
		    default:
		      throw new SimalRepositoryException("Attempt to create an unknown type of bug datatbase service");
		  }
	} 
	 */

	/**
	 * Get an instance of the mailing list service.
	 * @return
	 * @throws SimalRepositoryException 
	 * 
	 * commented out as this code moved to a branch - do we need to remove it yet?
	 * 
	public static JcrMailingListService getMailingListService() throws SimalRepositoryException {
		 switch (repoType) {
		    case JENA:
		      // return new JenaRepositoryService(getInstance());
		      throw new SimalRepositoryException("Not implemented JenaMailingListService");
		    case JCR: 
		    	return new JcrMailingListService(getInstance());
		    default:
		      throw new SimalRepositoryException("Attempt to create an unknown type of bug datatbase service");
		  }
	}
	*/

	
	/**
	 * Get an instance of the release service.
	 * @return
	 * @throws SimalRepositoryException 
	 * 
	 * commented out as this code moved to a branch - do we need to remove it yet?
	public static JcrReleaseService getReleaseService() throws SimalRepositoryException {
		 switch (repoType) {
		    case JENA:
		      // return new JenaRepositoryService(getInstance());
		      throw new SimalRepositoryException("Not implemented JenaRelaseService");
		    case JCR: 
		      return new JcrReleaseService(getInstance());
		    default:
		      throw new SimalRepositoryException("Attempt to create an unknown type of release service");
		  }
	}
	
	 */
}

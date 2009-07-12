package uk.ac.osswatch.simal.service.jcr;
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

import java.util.Set;

import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.query.Filter;
import org.apache.jackrabbit.ocm.query.Query;
import org.apache.jackrabbit.ocm.query.QueryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.jcr.JcrSimalRepository;
import uk.ac.osswatch.simal.model.jcr.Person;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;
import uk.ac.osswatch.simal.service.AbstractService;
import uk.ac.osswatch.simal.service.IPersonService;

public class JcrPersonService extends AbstractService implements IPersonService {
	public static final Logger logger = LoggerFactory
	.getLogger(JcrPersonService.class);

	public JcrPersonService(ISimalRepository simalRepository) {
		super(simalRepository);
	}

	public IPerson create(String uri) throws SimalRepositoryException,
			DuplicateURIException {
	    if (getRepository().containsPerson(uri)) {
	        throw new DuplicateURIException(
	            "Attempt to create a second person with the URI " + uri);
	      }

	      String personID = getNewID();
	      String simalPersonURI = RDFUtils.getDefaultPersonURI(personID);
	      logger.debug("Creating a new Simal Person instance with URI: "
	          + simalPersonURI);

	      IPerson person = new Person(simalPersonURI);
	      person.setSimalID(personID);
	      return person;
	}

	public Set<IPerson> filterByName(String filter) {
		// TODO Auto-generated method stub
		return null;
	}

	public IPerson findById(String id) throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IPerson findBySeeAlso(String seeAlso)
			throws SimalRepositoryException {
		ObjectContentManager ocm = ((JcrSimalRepository)SimalRepositoryFactory.getInstance()).getObjectContentManager();
		QueryManager queryManager = ocm.getQueryManager();

		Filter filter = queryManager.createFilter(Person.class);
		filter.addEqualTo("seeAlso", seeAlso); 

		Query query = queryManager.createQuery(filter);
		return (Person) ocm.getObject(query);
	}

	public IPerson findBySha1Sum(String sha1sum)
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IPerson get(String uri) throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IPerson> getAll() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAllAsJSON() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IPerson> getColleagues(IPerson person)
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IPerson getDuplicate(String email) throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getNewID() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IPerson getOrCreate(String uri) throws SimalRepositoryException {
		if (SimalRepositoryFactory.getInstance().containsResource(uri)) {
			return SimalRepositoryFactory.getPersonService().get(uri);
		} else {
			IPerson person = findBySeeAlso(uri);
			if (person == null) {
				try {
					return SimalRepositoryFactory.getPersonService().create(uri);
				} catch (DuplicateURIException e) {
					logger.error("Threw a DuplicateURIEception when we had already checked for resource existence", e);
					return null;
				}
			} else {
				return person;
			}
		}
	}

}

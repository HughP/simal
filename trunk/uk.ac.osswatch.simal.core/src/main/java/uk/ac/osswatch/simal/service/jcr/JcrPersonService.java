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

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.query.Filter;
import org.apache.jackrabbit.ocm.query.Query;
import org.apache.jackrabbit.ocm.query.QueryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.jcr.JcrSimalRepository;
import uk.ac.osswatch.simal.model.jcr.Person;
import uk.ac.osswatch.simal.model.jcr.Project;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalNamespaceContext;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;
import uk.ac.osswatch.simal.service.AbstractService;
import uk.ac.osswatch.simal.service.IPersonService;

import com.hp.hpl.jena.vocabulary.RDF;

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
	    String simalPersonURI;
	    if (!uri.startsWith(RDFUtils.PERSON_NAMESPACE_URI)) {
		    simalPersonURI = RDFUtils.getDefaultProjectURI(personID);
		    logger.debug("Creating a new Simal Person instance with URI: "
		        + simalPersonURI);
	    } else {
	        simalPersonURI = uri;
	    }

	    IPerson person = new Person("/person/" + personID);
	    person.setURI(uri);
	    ObjectContentManager ocm = ((JcrSimalRepository)SimalRepositoryFactory.getInstance()).getObjectContentManager();
	    ocm.insert(person);
	    ocm.save();
	    
	    return person;
	}

	public Set<IPerson> filterByName(String filter) {
		throw new UnsupportedOperationException();
	}

	public IPerson findById(String id) throws SimalRepositoryException {
		throw new UnsupportedOperationException();
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
		throw new UnsupportedOperationException();
	}

	public IPerson get(String uri) throws SimalRepositoryException {
		throw new UnsupportedOperationException();
	}

	public Set<IPerson> getAll() throws SimalRepositoryException {
		throw new UnsupportedOperationException();
	}

	public String getAllAsJSON() throws SimalRepositoryException {
		throw new UnsupportedOperationException();
	}

	public Set<IPerson> getColleagues(IPerson person)
			throws SimalRepositoryException {
		throw new UnsupportedOperationException();
	}

	public IPerson getDuplicate(String email) throws SimalRepositoryException {
		throw new UnsupportedOperationException();
	}

	public String getNewID() throws SimalRepositoryException {
		javax.jcr.Node node;
		node = ((JcrSimalRepository)SimalRepositoryFactory.getInstance()).getNode("id/person");
		String strID;
		try {
			strID = node.getProperty("nextID").getString();
		} catch (PathNotFoundException e) {
			strID = "1";
		} catch (RepositoryException e) {
			throw new SimalRepositoryException("Unable to get new person ID", e);
		}
		try {
			node.setProperty("nextID", Integer.toString(Integer.parseInt(strID) + 1));
		}  catch (RepositoryException e) {
			throw new SimalRepositoryException("Unable to set new person ID", e);
		}
		return strID;
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

	public IPerson createFromFoaf(Node personNode) throws SimalRepositoryException {
		IPersonService personService = SimalRepositoryFactory.getPersonService();
		
		Node about = personNode.getAttributes().getNamedItemNS(RDF.getURI(), "about");
		String id = personService.getNewID();
		String uri;
		if (about != null) {
		    uri = about.getTextContent();
		} else {
			uri = RDFUtils.getDefaultPersonURI(id);
		}
		IPerson person;
		try {
			person = personService.create(uri);
			person.setSimalID(id);
		} catch (DuplicateURIException e) {
			throw new SimalRepositoryException("The person already exists, currently we don't know how to merge data. URI = " + uri, e);
		}
		
		XPathFactory xpFactory = XPathFactory.newInstance();
		XPath xpath = xpFactory.newXPath();
		xpath.setNamespaceContext(new SimalNamespaceContext());
		try {
			Object value = xpath.evaluate("//foaf:name", personNode);
			person.addName((String)value);
		} catch (XPathExpressionException e) {
			throw new SimalRepositoryException("Problem with XPath experession", e);
		}
				
		personService.save(person);
		return person;
	}

}

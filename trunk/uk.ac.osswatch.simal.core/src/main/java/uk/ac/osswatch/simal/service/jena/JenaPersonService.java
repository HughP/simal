package uk.ac.osswatch.simal.service.jena;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.jena.Person;
import uk.ac.osswatch.simal.model.jena.simal.JenaSimalRepository;
import uk.ac.osswatch.simal.model.simal.SimalOntology;
import uk.ac.osswatch.simal.rdf.AbstractSimalRepository;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;
import uk.ac.osswatch.simal.service.IPersonService;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class JenaPersonService extends JenaService implements
		IPersonService {
	public static final Logger logger = LoggerFactory
	.getLogger(JenaPersonService.class);

	public JenaPersonService(ISimalRepository simalRepository) {
		super(simalRepository);
	}

	public IPerson create(String uri) throws SimalRepositoryException,
			DuplicateURIException {
	    if (getRepository().containsPerson(uri)) {
	        throw new DuplicateURIException(
	            "Attempt to create a second person with the URI " + uri);
	      }
		Model model = ((JenaSimalRepository)getRepository()).getModel();

	      String personID = getNewID();
	      String simalPersonURI = RDFUtils.getDefaultPersonURI(personID);
	      logger.debug("Creating a new Simal Person instance with URI: "
	          + simalPersonURI);

	      com.hp.hpl.jena.rdf.model.Resource r = model.createResource(simalPersonURI);
	      Statement s = model.createStatement(r, RDF.type, SimalOntology.PERSON);
	      model.add(s);
	      
	      if (!uri.startsWith(RDFUtils.PERSON_NAMESPACE_URI)) {
	        com.hp.hpl.jena.rdf.model.Resource res = model.createResource(uri);
	        s = model.createStatement(r, RDFS.seeAlso, res);
	        model.add(s);
	      }

	      IPerson person = new Person(r);
	      person.setSimalID(personID);
	      return person;
	}

	public Set<IPerson> filterByName(String filter) {
		String queryStr = "PREFIX xsd: <" + AbstractSimalRepository.XSD_NAMESPACE_URI
	    + "> " + "PREFIX foaf: <" + RDFUtils.FOAF_NS + "> "
	    + "PREFIX rdf: <" + AbstractSimalRepository.RDF_NAMESPACE_URI + ">"
	    + "PREFIX simal: <" + AbstractSimalRepository.SIMAL_NAMESPACE_URI + ">"
	    + "SELECT DISTINCT ?person WHERE { ?person a foaf:Person;"
	    + "  foaf:name ?name . "
	    + "  FILTER regex(?name, \"" + convertFilterToRE(filter) + "\", \"i\") }";

	    return filterBySPARQL(queryStr);
	}

	/**
	   * Find all people returned using a SPARQL query.
	   * 
	   * @param queryStr
	   * @return
	   */
	  private Set<IPerson> filterBySPARQL(String queryStr) {
			Model model = ((JenaSimalRepository)getRepository()).getModel();
	    Query query = QueryFactory.create(queryStr);
	    QueryExecution qe = QueryExecutionFactory.create(query, model);
	    ResultSet results = qe.execSelect();

	    Set<IPerson> people = new HashSet<IPerson>();
	    while (results.hasNext()) {
	      QuerySolution soln = results.nextSolution();
	      RDFNode node = soln.get("person");
	      if (node.isResource()) {
	        people.add(new Person((com.hp.hpl.jena.rdf.model.Resource) node));
	      }
	    }
	    qe.close();
	    return people;
	  }

	public IPerson findById(String id) throws SimalRepositoryException {
	    if (!getRepository().isValidSimalID(id)) {
	        throw new SimalRepositoryException(
	            "Attempt to find a person using an invalid Simal ID of "
	                + id
	                + " are you sure that's a world unique identifier? You may need to call getUniqueSimalID() or RDFUtils.getUniqueSimalID(id)");
	      }
	      String queryStr = "PREFIX xsd: <" + AbstractSimalRepository.XSD_NAMESPACE_URI
	          + "> " + "PREFIX rdf: <" + AbstractSimalRepository.RDF_NAMESPACE_URI + ">"
	          + "PREFIX simal: <" + AbstractSimalRepository.SIMAL_NAMESPACE_URI + ">"
	          + "SELECT DISTINCT ?person WHERE { " + "?person simal:personId \"" + id
	          + "\" }";
	      IPerson person = findBySPARQL(queryStr);

	      return person;
	}

	  /**
	   * Find a single person by executing a SPARQL query.
	   * 
	   * @param queryStr
	   * @return
	   */
	  private IPerson findBySPARQL(String queryStr) {
			Model model = ((JenaSimalRepository)getRepository()).getModel();
	    Query query = QueryFactory.create(queryStr);
	    QueryExecution qe = QueryExecutionFactory.create(query, model);
	    ResultSet results = qe.execSelect();

	    IPerson person = null;
	    while (results.hasNext()) {
	      QuerySolution soln = results.nextSolution();
	      RDFNode node = soln.get("person");
	      if (node.isResource()) {
	        person = new Person((com.hp.hpl.jena.rdf.model.Resource) node);
	      }
	    }
	    qe.close();
	    return person;
	  }

	public IPerson findBySeeAlso(String seeAlso)
			throws SimalRepositoryException {
    String queryStr = "PREFIX simal: <" + RDFUtils.SIMAL_NS + "> "
        + "PREFIX rdf: <" + AbstractSimalRepository.RDF_NAMESPACE_URI + "> "
        + "PREFIX rdfs: <" + AbstractSimalRepository.RDFS_NAMESPACE_URI + "> "
        + "SELECT DISTINCT ?person WHERE { "
        + "?person rdf:type simal:Person . " + "?person rdfs:seeAlso <"
        + seeAlso + ">}";

    IPerson person = findBySPARQL(queryStr);

    return person;
	}

	public IPerson findBySha1Sum(String sha1sum)
			throws SimalRepositoryException {
    String queryStr = "PREFIX foaf: <" + RDFUtils.FOAF_NS + "> "
        + "PREFIX rdf: <" + AbstractSimalRepository.RDF_NAMESPACE_URI + ">"
        + "SELECT DISTINCT ?person WHERE { " + "?person foaf:mbox_sha1sum \""
        + sha1sum + "\"}";

    IPerson person = findBySPARQL(queryStr);

    return person;
	}

	public IPerson get(String uri) throws SimalRepositoryException {
		Model model = ((JenaSimalRepository)getRepository()).getModel();
		if(uri.startsWith(RDFUtils.PERSON_NAMESPACE_URI)) {
		    if (getRepository().containsPerson(uri)) {
		      return new Person(model.getResource(uri));
		    } else {
		      return null;
		    }
		} else {
			return findBySeeAlso(uri);
		}
	}

	public Set<IPerson> getAll() throws SimalRepositoryException {
		Model model = ((JenaSimalRepository)getRepository()).getModel();
	    Property o = model.createProperty(JenaSimalRepository.SIMAL_PERSON_URI);
	    StmtIterator itr = model.listStatements(null, RDF.type, o);
	    Set<IPerson> people = new HashSet<IPerson>();
	    while (itr.hasNext()) {
	      String uri = itr.nextStatement().getSubject().getURI();
	      people.add(new Person(model.getResource(uri)));
	    }
	    return people;
	}

	public String getAllAsJSON() throws SimalRepositoryException {
	    StringBuffer json = new StringBuffer("{ \"items\": [");
	    Set<IPerson> setOfPeople = getAll();
	    Iterator<IPerson> people = setOfPeople.iterator();
	    IPerson person;
	    while (people.hasNext()) {
	      person = people.next();
	      json.append(person.toJSONRecord());
	      if (people.hasNext()) {
	        json.append(",");
	      }
	    }
	    json.append("]}");
	    return json.toString();
	}

	public IPerson getDuplicate(String email) throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getNewID() throws SimalRepositoryException {
	    String fullID = null;
	    String strEntityID = SimalProperties.getProperty(
	        SimalProperties.PROPERTY_SIMAL_NEXT_PERSON_ID, "1");
	    long entityID = Long.parseLong(strEntityID);

	    /**
	     * If the properties file is lost for any reason the next ID value will be
	     * lost. We therefore need to perform a sanity check that this is unique.
	     */
	    boolean validID = false;
	    while (!validID) {
	      fullID = getRepository().getUniqueSimalID("per" + Long.toString(entityID));
	      if (findById(fullID) == null) {
	        validID = true;
	      } else {
	        entityID = entityID + 1;
	      }
	    }

	    long newId = entityID + 1;
	    SimalProperties.setProperty(SimalProperties.PROPERTY_SIMAL_NEXT_PERSON_ID,
	        Long.toString(newId));
	    try {
	      SimalProperties.save();
	    } catch (Exception e) {
	      logger.warn("Unable to save properties file", e);
	      throw new SimalRepositoryException(
	          "Unable to save properties file when creating the next person ID", e);
	    }

	    return fullID;
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

	public Set<IPerson> getColleagues(IPerson person) throws SimalRepositoryException {
	    Set<IPerson> colleagues = new HashSet<IPerson>();
	    Set<String> colleagueIDs = new HashSet<String>();
	    
	    Iterator<IProject> projects = person.getProjects().iterator();
	    while (projects.hasNext()) {
	      IProject project = projects.next();
	      Iterator<IPerson> people = project.getAllPeople().iterator();
	      while (people.hasNext()) {
	        IPerson colleague = people.next();
	        String id = colleague.getSimalID();
	        if (!id.equals(person.getSimalID()) && !colleagueIDs.contains(id)) {
	          colleagues.add(colleague);
	          colleagueIDs.add(id);
	        }
	      }
	    }

	    return colleagues;
	}

	public IPerson createFromFoaf(Node node) {
		throw new UnsupportedOperationException();
	}
}

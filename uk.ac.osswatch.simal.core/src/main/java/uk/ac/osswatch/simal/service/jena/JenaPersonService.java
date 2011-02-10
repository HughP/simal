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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
import uk.ac.osswatch.simal.rdf.io.RDFXMLUtils;
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
	private static final Logger LOGGER = LoggerFactory
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
	      LOGGER.debug("Creating a new Simal Person instance with URI: "
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

  /**
   * Add a person to the repository. We attempt to handle duplicates by matching on items
   * such as email email hashes and seeAlso URIs.
   * 
   * @param simalProjectURI
   *          the URI of the simal record representing the project this person beongs to
   * @throws DOMException
   * @throws SimalRepositoryException
   */
  public void addPerson(Element sourcePersonRoot, String simalProjectURI)
      throws SimalRepositoryException, DOMException {
    if (!sourcePersonRoot.getLocalName().equals("Person")) {
      throw new IllegalArgumentException(
          "Supplied element is not a doap:Person element, it is "
              + sourcePersonRoot.getNodeName());
    }
    String simalPersonURI = null;
    
    cleanPerson(sourcePersonRoot);
    RDFXMLUtils.checkPersonSHA1(sourcePersonRoot.getOwnerDocument());

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    DocumentBuilder db;
    try {
      db = dbf.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      throw new SimalRepositoryException("Unable to create document builder", e);
    }
    Document simalPersonDoc = db.newDocument();
    Node simalPersonRDFRoot = simalPersonDoc.createElementNS(RDFUtils.RDF_NS,
        "RDF");
    Element simalPersonElement = simalPersonDoc.createElementNS(
        RDFUtils.SIMAL_NS, "Person");

    // handle duplicate people identified by their mbox_sha1sum
    NodeList sha1sums = sourcePersonRoot.getElementsByTagNameNS(
        RDFUtils.FOAF_NS, "mbox_sha1sum");
    for (int i = 0; i < sha1sums.getLength(); i = i + 1) {
      Element sha1sumNode = (Element) sha1sums.item(i);
      if (sha1sumNode.getParentNode().equals(sourcePersonRoot)) {
        String sha1Sum = sha1sumNode.getFirstChild().getNodeValue().trim();
        IPerson person = SimalRepositoryFactory.getPersonService().findBySha1Sum(sha1Sum);
        if (person != null) {
          LOGGER
              .debug("Simal already has a Person record with the foaf:mbox_sha1: "
                  + sha1Sum + " called " + person);
          person = SimalRepositoryFactory.getPersonService().findBySeeAlso(person.getURI());
          simalPersonURI = person.getURI();
        }
      }
    }

    // handle duplicate people identified by their rdf:about
    String uri = sourcePersonRoot.getAttributeNS(RDFUtils.RDF_NS, "about");
    IPerson person = SimalRepositoryFactory.getPersonService().findBySeeAlso(uri);
    if (person != null) {
      LOGGER
          .debug("Simal already has a Person record about "
                  + uri + " called " + person);
      simalPersonURI = person.getURI();
    }

    // handle duplicate people identified by their rdfs:seeAlso
    NodeList seeAlsos = sourcePersonRoot.getElementsByTagNameNS(
        RDFUtils.RDFS_NS, "seeAlso");
    Element seeAlso;
    for (int i = 0; i < seeAlsos.getLength(); i = i + 1) {
      seeAlso = (Element) seeAlsos.item(i);
      if (seeAlso.getParentNode().equals(sourcePersonRoot)) {
        uri = seeAlso.getAttributeNS(RDFUtils.RDF_NS, "resource").trim();
        person = SimalRepositoryFactory.getPersonService().findBySeeAlso(uri);
        if (person != null) {
          LOGGER
              .debug("Simal already has a Person record with the rdfs:seeAlso "
                  + uri + " called " + person);
          simalPersonURI = person.getURI();
        } else {
          seeAlso = simalPersonDoc.createElementNS(RDFUtils.RDFS_NS, "seeAlso");
          seeAlso.setAttributeNS(RDFUtils.RDF_NS, "resource", uri);
          simalPersonElement.appendChild(seeAlso);
        }
      }
    }

    // Add current project records
    Element projNode = simalPersonDoc.createElementNS(RDFUtils.FOAF_NS,
      "currentProject");
    projNode.setAttributeNS(RDFUtils.RDF_NS, "resource", simalProjectURI);
    simalPersonElement.appendChild(projNode);

    if (simalPersonURI == null) {
      String personID = getNewPersonID();
      simalPersonURI = RDFUtils.getDefaultPersonURI(personID);
      LOGGER.debug("Creating a new Simal Person instance with URI: "
          + simalPersonURI);

      Element simalID = simalPersonDoc.createElementNS(RDFUtils.SIMAL_NS,
          RDFUtils.SIMAL_PERSON_ID);
      simalID.setTextContent(personID);
      simalPersonElement.appendChild(simalID);
    } else {
      LOGGER.debug("Updating an existing Simal Person instance with URI: "
          + simalPersonURI);
    }
    simalPersonElement.setAttributeNS(RDFUtils.RDF_NS, "about", simalPersonURI);

    seeAlso = simalPersonDoc.createElementNS(RDFUtils.RDFS_NS, "seeAlso");
    String resource = sourcePersonRoot.getAttributeNS(RDFUtils.RDF_NS, "about");
    if (resource == null || resource.equals("")) {
      // we don't allow blank person nodes
      resource = RDFUtils.getDefaultPersonURI(getNewPersonID());
      sourcePersonRoot.setAttributeNS(RDFUtils.RDF_NS, "about", resource);
    }
    seeAlso.setAttributeNS(RDFUtils.RDF_NS, "resource", resource);
    simalPersonElement.appendChild(seeAlso);

    simalPersonRDFRoot.appendChild(simalPersonElement);
    simalPersonDoc.appendChild(simalPersonRDFRoot);

    RDFXMLUtils.addRDFXMLToModel(simalPersonDoc, getModel());

    // Handle all known people
    NodeList people = sourcePersonRoot.getElementsByTagNameNS(RDFUtils.FOAF_NS,
        "Person");
    Element personEl;
    for (int i = 0; i < people.getLength(); i = i + 1) {
      personEl = (Element) people.item(i);
      addPerson(personEl, simalProjectURI);
    }

    // add the source RDF
    Document sourceDoc = db.newDocument();
    Node root = sourceDoc.createElementNS(RDFUtils.RDF_NS, "RDF");
    root.appendChild(sourceDoc.importNode(sourcePersonRoot, true));
    sourceDoc.appendChild(root);
    RDFXMLUtils.addRDFXMLToModel(sourceDoc, getModel());
  }

  /**
   * Check the RDF for a person element for common mistakes and try to fix them.
   * 
   * @param personRoot
   */
  private void cleanPerson(Element personRoot) {
    RDFXMLUtils.validateResourceDefinition(personRoot.getElementsByTagNameNS(
        RDFUtils.FOAF_NS, "homepage"));
  }
  
	public Set<IPerson> filterByName(String filter) {
		String queryStr = "PREFIX xsd: <" + AbstractSimalRepository.XSD_NAMESPACE_URI
	    + "> " + "PREFIX foaf: <" + RDFUtils.FOAF_NS + "> "
	    + "PREFIX rdf: <" + AbstractSimalRepository.RDF_NAMESPACE_URI + "> "
      + "PREFIX rdfs: <" + AbstractSimalRepository.RDFS_NAMESPACE_URI + "> "
	    + "PREFIX simal: <" + AbstractSimalRepository.SIMAL_NAMESPACE_URI + "> "
	    + "SELECT DISTINCT ?person WHERE { ?person a simal:Person; rdfs:seeAlso ?fp . " 
	    + " ?fp   foaf:name ?name . "
	    + "FILTER regex(?name, \"" + convertFilterToRE(filter) + "\", \"i\") }";

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
        + "SELECT DISTINCT ?person WHERE { " + "?person foaf:mbox_sha1sum \""
        + sha1sum + "\"}";

    IPerson person = findBySPARQL(queryStr);

    return person;
	}

  public IPerson findByUsername(String username) {
    String queryStr = "PREFIX simal: <" + SimalOntology.NS + "> "
    + "SELECT DISTINCT ?person WHERE { " + "?person simal:username \""
    + username + "\"}";

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
	  return getNewID(SimalProperties.PROPERTY_SIMAL_NEXT_PERSON_ID, "per");
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
					LOGGER.error("Threw a DuplicateURIEception when we had already checked for resource existence", e);
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

  public String getNewPersonID() throws SimalRepositoryException {
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
      fullID = SimalRepositoryFactory.getInstance().getUniqueSimalID("per" + Long.toString(entityID));
      if (SimalRepositoryFactory.getPersonService().findById(fullID) == null) {
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
      LOGGER.warn("Unable to save properties file", e);
      throw new SimalRepositoryException(
          "Unable to save properties file when creating the next person ID", e);
    }

    return fullID;
  }

}

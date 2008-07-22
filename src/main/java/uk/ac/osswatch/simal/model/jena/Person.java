package uk.ac.osswatch.simal.model.jena;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import uk.ac.osswatch.simal.model.Doap;
import uk.ac.osswatch.simal.model.Foaf;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.SimalOntology;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class Person extends Resource implements IPerson {
  
  private static final Logger logger = LoggerFactory.getLogger(Person.class);

  public Person(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
  }

  public Set<IPerson> getColleagues() throws SimalRepositoryException {
    String uri = getURI();
    String queryStr = "PREFIX foaf: <" + Foaf.NS + "> " + "PREFIX doap: <"
        + Doap.NS + "> " 
        + "PREFIX rdf: <" + ISimalRepository.RDF_NAMESPACE_URI + "> " 
        + "SELECT DISTINCT ?colleague WHERE { "
        + "?project rdf:type doap:Project . "
        + "{?project doap:maintainer <" + uri + "> } UNION "
        + "{?project doap:developer <" + uri + "> } UNION "
        + "{?project doap:documentor <" + uri + "> } UNION "
        + "{?project doap:helper <" + uri + "> } UNION "
        + "{?project doap:tester <" + uri + "> } UNION "
        + "{?project doap:translator <" + uri + "> } . "
        + "{?project doap:developer ?colleague} UNION "
        + "{?project doap:documenter ?colleague} UNION "
        + "{?project doap:helper ?colleague} UNION"
        + "{?project doap:maintainer ?colleague} UNION"
        + "{?project doap:tester ?colleague} UNION"
        + "{?project doap:translator ?colleague} " + "}";
    logger.debug(("Executing SPARQL query:\n" + queryStr));
    Query query = QueryFactory.create(queryStr);
    QueryExecution qe = QueryExecutionFactory.create(query, jenaResource.getModel());
    ResultSet results = qe.execSelect();
    
    Set<IPerson> colleagues = new HashSet<IPerson>();
    IPerson colleague;
    while(results.hasNext()) {
      QuerySolution soln = results.nextSolution() ;
      RDFNode node = soln.get("colleague");
      if (node.isResource()) {
        colleague = new Person((com.hp.hpl.jena.rdf.model.Resource) node);
        String id = colleague.getSimalID();
        if (!id.equals(getSimalID())) {        
          colleagues.add(colleague);
        }
      }
    }
    qe.close();

    return colleagues;
  }

  public Set<InternetAddress> getEmail() {
    StmtIterator itr = jenaResource.listProperties(Foaf.MBOX);
    Set<InternetAddress> emails = new HashSet<InternetAddress>();
    while (itr.hasNext()) {
      emails.add(new InternetAddress(itr.nextStatement().getResource()));
    }
    return emails;
  }

  public Set<String> getGivennames() {
    StmtIterator itr = jenaResource.listProperties(Foaf.GIVENNAME);
    Set<String> names = new HashSet<String>();
    while (itr.hasNext()) {
      names.add(itr.nextStatement().getString());
    }
    return names;
  }

  public Set<String> getNames() {
    StmtIterator itr = jenaResource.listProperties(Foaf.NAME);
    Set<String> names = new HashSet<String>();
    while (itr.hasNext()) {
      names.add(itr.nextStatement().getString());
    }
    return names;
  }

  public Set<String> getFirstnames() {
    StmtIterator itr = jenaResource.listProperties(Foaf.FIRST_NAME);
    Set<String> names = new HashSet<String>();
    while (itr.hasNext()) {
      names.add(itr.nextStatement().getString());
    }
    return names;
  }

  public Set<Homepage> getHomepages() {
    StmtIterator itr = jenaResource.listProperties(Foaf.HOMEPAGE);
    Set<Homepage> homepages = new HashSet<Homepage>();
    while (itr.hasNext()) {
      homepages.add(new Homepage(itr.nextStatement().getResource()));
    }
    return homepages;
  }

  public Set<IPerson> getKnows() {
    StmtIterator itr = jenaResource.listProperties(Foaf.KNOWS);
    Set<IPerson> people = new HashSet<IPerson>();
    while (itr.hasNext()) {
      people.add(new Person(itr.nextStatement().getResource()));
    }
    return people;
  }

  public String getSimalID() {
    Statement idStatement = jenaResource.getProperty(SimalOntology.PERSON_ID);
    return idStatement.getString();
  }

  public void setSimalID(String newId) {
    jenaResource.addLiteral(SimalOntology.PERSON_ID, newId);
  }
  

  

  
  /**
   * Get the label for this person. The label for a person is derived
   * from their known names. If the person does not have any defined
   * names then the toString() method is used..
   * 
   * @return
   */
  public String getLabel() {
    Set<String> names = getNames();
    if (names.size() == 0) {
      names = getGivennames();
    }
    if (names == null ) {
      return toString();
    } else {
      if (names.size() == 0) {
        try {
          return getURI();
        } catch (SimalRepositoryException e) {
          logger.warn("Unable to generate URI for Person", e);
          return "Error: No valid label";
        }
      }
      return (String)names.toArray()[0];
    }
  }

}

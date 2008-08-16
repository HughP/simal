package uk.ac.osswatch.simal.spike.inferencing;

import uk.ac.osswatch.simal.rdf.jena.SimalRepository;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.PrintUtil;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.VCARD;

public abstract class AbstractReasoning {

  protected static String johnPrimeURI = "http://somewhere/JohnSmith";
  protected static String johnPrimeName = "John Smith";
  protected static String johnSecondURI = "http://somewhere.else/JohnSmith";
  protected static String johnSecondName = "John Wilbur Smith";

  public void printStatements(String title, Model m, Resource s, Property p,
      Resource o) {
    System.out.println(title);
    for (StmtIterator i = m.listStatements(s, p, o); i.hasNext();) {
      Statement stmt = i.nextStatement();
      System.out.println(" - " + PrintUtil.print(stmt));
    }
  }

  public void runTestQuery(InfModel infModel) {
    String queryStr = "PREFIX vcard: <" + VCARD.getURI()
        + "> " + "PREFIX rdf: <" + SimalRepository.RDF_NAMESPACE_URI + ">"
        + "SELECT ?uri WHERE { "
        + "?uri vcard:FN \"John Smith\" . }";
    Query query = QueryFactory.create(queryStr);
    QueryExecution qe = QueryExecutionFactory.create(query, infModel);
    ResultSet results = qe.execSelect();
    
    System.out.println("Query results:");
    while (results.hasNext()) {
      QuerySolution soln = results.nextSolution();
      RDFNode node = soln.getResource("uri");
      System.out.println("URI for John Smith: " + node);
    }
    qe.close();
  }

  public void addSecondResource(Model model) {
    Resource resource;
    resource = model.createResource(johnSecondURI);
    resource.addProperty(VCARD.FN,
        johnSecondName);
    resource.addProperty(OWL.sameAs,
        johnPrimeURI);
  }

  public void addPrimeResource(Model model) {
    Resource resource = model.createResource(johnPrimeURI);
    resource.addProperty(
        VCARD.FN, johnPrimeName);
    resource.addProperty(OWL.sameAs, johnSecondURI);
    resource.addProperty(RDFS.seeAlso, johnSecondURI);
  }

  public Model getModel() {
    Model model = ModelFactory.createDefaultModel();
    addPrimeResource(model);
    addSecondResource(model);
    return model;
  }

}

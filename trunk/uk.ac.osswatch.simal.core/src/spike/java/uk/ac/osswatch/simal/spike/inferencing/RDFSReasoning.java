package uk.ac.osswatch.simal.spike.inferencing;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * A spike class to experiment with RDFS reasoning in Jena.
 *  
 */
public class RDFSReasoning extends AbstractReasoning {
  /**
   * @param args
   */
  public static void main(String[] args) {
    RDFSReasoning spike = new RDFSReasoning();
    spike.sameAs();
  }

  /**
   * Play with seeAs reasoning. Answer questions like "does the reasoner merge
   * tuples from resources marked with seeAlso?"
   * 
   * NOTE: this was written whilst trying to learn about reasoning
   * At the time of writing this note there is no actual reasoning 
   * illustrated by this spike - it was a failed experiment. I had
   * hoped I could query and it would follow rdf:seeAlso links 
   */
  private void sameAs() {
    Model  data = getModel();
    
    InfModel infModel = ModelFactory.createRDFSModel(data);
    Resource resource = infModel.getResource(johnPrimeURI);
    printStatements("John Smith RDFS Reasoned:", infModel, resource, null, null);

    runTestQuery(infModel);
  }

}

package uk.ac.osswatch.simal.spike.inferencing;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;

/**
 * A spike class to experiment with OWL reasoning in Jena.
 *  
 */
public class OWLReasoning extends AbstractReasoning {
  /**
   * @param args
   */
  public static void main(String[] args) {
    OWLReasoning spike = new OWLReasoning();
    spike.sameAs();
  }

  /**
   * Play with sameAs reasoning. Answer questions like "does the reasoner merge
   * tuples from resources marked sameAs?"
   * 
   * NOTE: this was written whilst trying to learn about reasoning
   * At the time of writing this note there is no actual reasoning 
   * illustrated by this spike - it was a failed experiment. I had
   * hoped I could query and it would follow owl:sameAs links
   */
  private void sameAs() {
    Resource resource;
    Model model = getModel();

    Reasoner owlReasoner = ReasonerRegistry.getOWLReasoner();
    InfModel infModel = ModelFactory.createInfModel(owlReasoner, model);
    
    resource = infModel.getResource(johnPrimeURI);
    printStatements("OWLReasoned", infModel, resource, null, null);
    
    runTestQuery(infModel);
  }

}

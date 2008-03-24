package uk.ac.osswatch.simal.myExperiment;

import uk.ac.osswatch.simal.AbstractRESTServlet;
import uk.ac.osswatch.simal.IAPIHandler;
import uk.ac.osswatch.simal.RESTCommand;
import uk.ac.osswatch.simal.SimalAPIException;

/**
 * This servlet provides a wrapper around the MyExperiment API.
 * It translates calls using the Simal API into MyExperiment API
 * calls can translates the response into a format of use to 
 * Simal.
 *
 */
public class MyExperimentServlet extends AbstractRESTServlet {
  private static final long serialVersionUID = -6913510400947316207L;

  public MyExperimentServlet() {
  }

  @Override
  protected IAPIHandler getHandler(RESTCommand cmd) throws SimalAPIException {
    return MyExperimentHandlerFactory.createHandler(cmd,"http://www.myexperiment.org");
  }

}
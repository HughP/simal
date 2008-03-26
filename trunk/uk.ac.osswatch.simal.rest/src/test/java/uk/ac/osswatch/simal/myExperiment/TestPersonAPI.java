package uk.ac.osswatch.simal.myExperiment;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import uk.ac.osswatch.simal.rest.IAPIHandler;
import uk.ac.osswatch.simal.rest.RESTCommand;
import uk.ac.osswatch.simal.rest.SimalAPIException;

public class TestPersonAPI {
  private String personID = "15"; // ID for Ross Gardler

  @Test
  public void testAllColleaguesJSON() throws SimalAPIException {
    RESTCommand command = RESTCommand.createGetColleagues(personID, RESTCommand.SOURCE_TYPE_MYEXPERIMENT, RESTCommand.FORMAT_JSON);
    IAPIHandler handler = MyExperimentHandlerFactory.createHandler(command);
    String result = handler.execute();
    assertNotNull(result);
  }

  @Test
  public void testAllColleaguesXML() throws SimalAPIException {
    RESTCommand command = RESTCommand.createGetColleagues(personID, RESTCommand.SOURCE_TYPE_MYEXPERIMENT, RESTCommand.FORMAT_XML);
    IAPIHandler handler = MyExperimentHandlerFactory.createHandler(command);
    String result = handler.execute();
    assertNotNull(result);
  }
}

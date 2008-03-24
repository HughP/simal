package uk.ac.osswatch.simal.rest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import uk.ac.osswatch.simal.IAPIHandler;
import uk.ac.osswatch.simal.RESTCommand;
import uk.ac.osswatch.simal.SimalAPIException;

public class TestPersonAPI extends AbstractAPITest {

  @Test
  public void testAllColleaguesJSON() throws SimalAPIException {
    RESTCommand command = RESTCommand.createCommand(RESTCommand.COMMAND_ALL_COLLEAGUES + "person-1/json");
    IAPIHandler handler = SimalHandlerFactory.createHandler(command, repo);
    String result = handler.execute(command);
    assertNotNull(result);
  }

  @Test
  public void testAllColleaguesXML() throws SimalAPIException {
    RESTCommand command = RESTCommand.createCommand(RESTCommand.COMMAND_ALL_COLLEAGUES + "person-1/xml");
    IAPIHandler handler = SimalHandlerFactory.createHandler(command, repo);
    String result = handler.execute(command);
    assertNotNull(result);
    
    assertFalse("There should be no people with null IDs", result.contains("id=\"null\""));
    
    assertFalse("The viewer should not be in the viewer friends list",result.contains("<friend>1"));
  }
}

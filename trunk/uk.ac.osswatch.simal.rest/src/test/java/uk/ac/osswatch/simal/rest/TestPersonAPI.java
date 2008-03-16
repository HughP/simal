package uk.ac.osswatch.simal.rest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TestPersonAPI extends AbstractAPITest {

  @Test
  public void testAllColleaguesJSON() throws SimalAPIException {
    String command = RESTServlet.COMMAND_ALL_COLLEAGUES + "person-1/json";
    IAPIHandler handler = HandlerFactory.createHandler(command, repo);
    String result = handler.execute(command);
    assertNotNull(result);
  }

  @Test
  public void testAllColleaguesXML() throws SimalAPIException {
    String command = RESTServlet.COMMAND_ALL_COLLEAGUES + "person-1/xml";
    IAPIHandler handler = HandlerFactory.createHandler(command, repo);
    String result = handler.execute(command);
    assertNotNull(result);
    assertFalse("There should be no people with null IDs", result.contains("id=\"null\""));
  }
}

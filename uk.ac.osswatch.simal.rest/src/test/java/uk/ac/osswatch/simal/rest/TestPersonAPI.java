package uk.ac.osswatch.simal.rest;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TestPersonAPI extends AbstractAPITest {

  @Test
  public void testAllColleaguesJSON() throws SimalAPIException {
    String command = RESTServlet.COMMAND_ALL_COLLEAGUES + "json";
    IAPIHandler handler = HandlerFactory.createHandler(command, repo);
    String result = handler.execute(command);
    assertNotNull(result);
  }
}

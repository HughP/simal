package uk.ac.osswatch.simal.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TestRESTCommand extends AbstractAPITest {

  @Test
  public void testProjectCommand() {
    RESTCommand cmd =RESTCommand.createGetProject(PROJECT_ID, RESTCommand.SOURCE_TYPE_SIMAL, RESTCommand.FORMAT_XML);
    assertNotNull(cmd);
    
    String path = cmd.getPath();
    assertEquals("Command path aappears to be incorrect", path, "/project/source-simal/project-200/xml");
  }
}

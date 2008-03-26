package uk.ac.osswatch.simal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestRESTCommand {

  @Test
  public void testCreateDefaultSourceFromURI() {
    String cmdString = RESTCommand.COMMAND_ALL_COLLEAGUES + RESTCommand.PARAM_PERSON_ID + "1" + RESTCommand.FORMAT_JSON;
    RESTCommand cmd = RESTCommand.createCommand(cmdString);
    assertEquals("Command method is incorrect", RESTCommand.COMMAND_ALL_COLLEAGUES, cmd.getCommandMethod());
    assertEquals("Person ID is incorrect", "1", cmd.getPersonID());
    assertEquals("Format is incorrect", RESTCommand.FORMAT_JSON, cmd.getFormat());
    assertEquals("Source is incorrect", RESTCommand.SOURCE_TYPE_SIMAL, cmd.getSource());
  }
  
  @Test
  public void testCreateMyExperiementSOurceFromURI() {
    String cmdString = RESTCommand.COMMAND_ALL_COLLEAGUES + RESTCommand.PARAM_PERSON_ID + "1" + RESTCommand.PARAM_SOURCE + RESTCommand.SOURCE_TYPE_MYEXPERIMENT + RESTCommand.FORMAT_JSON;
    RESTCommand cmd = RESTCommand.createCommand(cmdString);
    assertEquals("Source is incorrect", RESTCommand.SOURCE_TYPE_MYEXPERIMENT, cmd.getSource());
    assertEquals("Command method is incorrect", RESTCommand.COMMAND_ALL_COLLEAGUES, cmd.getCommandMethod());
    assertEquals("Person ID is incorrect", "1", cmd.getPersonID());
    assertEquals("Format is incorrect", RESTCommand.FORMAT_JSON, cmd.getFormat());
  }
  

  @Test
  public void testToPathInfo() {
    String cmdString = RESTCommand.COMMAND_ALL_COLLEAGUES + RESTCommand.PARAM_SOURCE + RESTCommand.SOURCE_TYPE_MYEXPERIMENT + RESTCommand.PARAM_PERSON_ID + "1" + RESTCommand.FORMAT_JSON;
    RESTCommand cmd = RESTCommand.createCommand(cmdString);
    assertEquals("Path info is incorrect", cmdString, cmd.toPathInfo());
  }
}

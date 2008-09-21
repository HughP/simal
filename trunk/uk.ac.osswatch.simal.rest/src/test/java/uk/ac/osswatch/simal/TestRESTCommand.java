package uk.ac.osswatch.simal;

/*
 * Copyright 2008 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");   *
 * you may not use this file except in compliance with the License.  *
 * You may obtain a copy of the License at                           *
 *                                                                   *
 *   http://www.apache.org/licenses/LICENSE-2.0                      *
 *                                                                   *
 * Unless required by applicable law or agreed to in writing,        *
 * software distributed under the License is distributed on an       *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY            *
 * KIND, either express or implied.  See the License for the         *
 * specific language governing permissions and limitations           *
 * under the License.                                                *
 */

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.ac.osswatch.simal.rest.RESTCommand;
import uk.ac.osswatch.simal.rest.SimalAPIException;

public class TestRESTCommand {

  @Test
  public void testCreateDefaultSourceFromURI() throws SimalAPIException {
    String cmdString = RESTCommand.ALL_COLLEAGUES + RESTCommand.PARAM_PERSON_ID
        + "1" + RESTCommand.FORMAT_JSON;
    RESTCommand cmd = RESTCommand.createCommand(cmdString);
    assertEquals("Command method is incorrect", RESTCommand.ALL_COLLEAGUES, cmd
        .getCommandMethod());
    assertEquals("Person ID is incorrect", "1", cmd.getPersonID());
    assertEquals("Format is incorrect", RESTCommand.FORMAT_JSON, cmd
        .getFormat());
    assertEquals("Source is incorrect", RESTCommand.TYPE_SIMAL, cmd.getSource());
  }

  @Test
  public void testCreateMyExperiementSOurceFromURI() throws SimalAPIException {
    String cmdString = RESTCommand.ALL_COLLEAGUES + RESTCommand.PARAM_PERSON_ID
        + "1" + RESTCommand.PARAM_SOURCE + RESTCommand.TYPE_MYEXPERIMENT
        + RESTCommand.FORMAT_JSON;
    RESTCommand cmd = RESTCommand.createCommand(cmdString);
    assertEquals("Source is incorrect", RESTCommand.TYPE_MYEXPERIMENT, cmd
        .getSource());
    assertEquals("Command method is incorrect", RESTCommand.ALL_COLLEAGUES, cmd
        .getCommandMethod());
    assertEquals("Person ID is incorrect", "1", cmd.getPersonID());
    assertEquals("Format is incorrect", RESTCommand.FORMAT_JSON, cmd
        .getFormat());
  }

  @Test
  public void testToPathInfo() throws SimalAPIException {
    String cmdString = RESTCommand.ALL_COLLEAGUES + RESTCommand.PARAM_SOURCE
        + RESTCommand.TYPE_MYEXPERIMENT + RESTCommand.PARAM_PERSON_ID + "1"
        + RESTCommand.FORMAT_JSON;
    RESTCommand cmd = RESTCommand.createCommand(cmdString);
    assertEquals("Path info is incorrect", cmdString, cmd.getPath());
  }
}

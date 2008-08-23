package uk.ac.osswatch.simal.rest;

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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestPersonAPI extends AbstractAPITest {

  @Test
  public void testAllColleaguesJSON() throws SimalAPIException {
    RESTCommand command = RESTCommand.createCommand(RESTCommand.ALL_COLLEAGUES
        + RESTCommand.PARAM_PERSON_ID + "15" + RESTCommand.FORMAT_JSON);
    IAPIHandler handler = SimalHandlerFactory.createHandler(command, getRepo());
    String result = handler.execute();
    assertNotNull(result);
  }

  @Test
  public void testAllColleaguesXML() throws SimalAPIException {
    final RESTCommand command = RESTCommand.createCommand(RESTCommand.ALL_COLLEAGUES
        + RESTCommand.PARAM_PERSON_ID + "15" + RESTCommand.FORMAT_XML);
    final IAPIHandler handler = SimalHandlerFactory.createHandler(command, getRepo());
    String result = handler.execute();
    assertNotNull("No XML Returned by getAllColleagues", result);

    assertFalse("There should be no people with null IDs", result
        .contains("id=\"null\""));

    assertFalse("The viewer should not be in the viewer friends list", result
        .contains("<friend>15</friend>"));
  }

  @Test
  public void testGetPerson() throws SimalAPIException {
    RESTCommand command = RESTCommand.createCommand(RESTCommand.PERSON
        + RESTCommand.PARAM_PERSON_ID + "15" + RESTCommand.FORMAT_XML);
    IAPIHandler handler = SimalHandlerFactory.createHandler(command, getRepo());
    String result = handler.execute();
    assertNotNull("No XML Returned by getPerson", result);
    
    assertTrue("XML file does not appear to describe a person", result.contains("Person>"));
  }
}

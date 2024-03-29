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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TestRESTCommand extends AbstractAPITest {

  @Test
  public void testProjectCommand() throws SimalAPIException {
    RESTCommand cmd = RESTCommand.createGetProject(testProjectID,
        RESTCommand.TYPE_SIMAL, RESTCommand.FORMAT_XML);
    assertNotNull(cmd);

    String path = cmd.getPath();
    StringBuilder expectedPath = new StringBuilder(
        "/project/source-simal/project-");
    expectedPath.append(testProjectID);
    expectedPath.append("/xml");
    assertEquals("Command path aappears to be incorrect", expectedPath
        .toString(), path);
  }
}

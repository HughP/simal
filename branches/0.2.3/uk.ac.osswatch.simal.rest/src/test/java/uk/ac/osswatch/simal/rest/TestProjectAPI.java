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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.junit.Test;

/**
 * Tests API functions for working with complete DOAP files.
 * 
 */
public class TestProjectAPI extends AbstractAPITest {

  @Test
  public void addDOAP() throws SimalAPIException, URISyntaxException,
      IOException {
    RESTCommand command = RESTCommand.createCommand(RESTCommand.PROJECT_ADD);
    command.addParameter("rdf", "illegal RDF data");
    IAPIHandler handler = SimalHandlerFactory.createHandler(command, getRepo());
    try {
      handler.execute();
    } catch (SimalAPIException e) {
      // that's good, we don't expect to add invalid data
    }

    InputStream fis = this.getClass().getResourceAsStream("/doapTestFile.xml");
    int x = fis.available();
    byte b[] = new byte[x];
    fis.read(b);
    String data = new String(b);

    command.addParameter(RESTCommand.PARAM_RDF, data);
    handler = SimalHandlerFactory.createHandler(command, getRepo());
    try {
      handler.execute();
    } catch (SimalAPIException e) {
      fail("Exception thrown when adding project" + e.getMessage());
    }

    // we don't bother testing to see if the project has been added here
    // that's the job of the repository tests.
  }

  @Test
  public void testGetProjectByCommand() throws SimalAPIException {
    RESTCommand cmd = RESTCommand.createGetProject(testProjectID,
        RESTCommand.TYPE_SIMAL, RESTCommand.FORMAT_XML);
    
    IAPIHandler handler = SimalHandlerFactory.createHandler(cmd, getRepo());
    String result = handler.execute();
    
    assertNotNull("No XML Returned by getProject", result);
    assertTrue("XML file does not appear to describe a project", result
        .contains("Project>"));
  }
  
  @Test
  public void testgetFeaturedProjectAsXML() throws SimalAPIException {
    RESTCommand cmd = RESTCommand.createGetProject("featured",
            RESTCommand.TYPE_SIMAL, RESTCommand.FORMAT_XML);
    
    IAPIHandler handler = SimalHandlerFactory.createHandler(cmd, getRepo());
    String result = handler.execute();
    
    assertNotNull("No XML Returned by getProject", result);
    assertTrue("XML file does not appear to describe a project", result
        .contains("Project>"));
    
    cmd = RESTCommand.createCommand("/project/project-featured/xml");
    
    handler = SimalHandlerFactory.createHandler(cmd, getRepo());
    result = handler.execute();
    
    assertNotNull("No XML Returned by getProject", result);
    assertTrue("XML file does not appear to describe a project", result
        .contains("Project>"));
  }
  
  @Test
  public void testgetFeaturedProjectAsJSON() throws SimalAPIException {
    RESTCommand cmd = RESTCommand.createGetProject("featured",
            RESTCommand.TYPE_SIMAL, RESTCommand.FORMAT_JSON);
    
    IAPIHandler handler = SimalHandlerFactory.createHandler(cmd, getRepo());
    String result = handler.execute();
    
    assertNotNull("No JSON Returned by getProject", result);
    assertTrue("JSON file does not appear to describe a project", result
        .contains("simalID"));
  }
  
  @Test
  public void testGetProjectByURL() throws SimalAPIException {
	String url = "/project/source-simal/project-" + testProjectID + "/xml";
	RESTCommand cmd = RESTCommand.createCommand(url);
	IAPIHandler handler = SimalHandlerFactory.createHandler(cmd, getRepo());
    String result = handler.execute();
    
    assertNotNull("No XML Returned by getProject", result);
    assertTrue("XML file does not appear to describe a project", result
        .contains("Project>"));
  }
}

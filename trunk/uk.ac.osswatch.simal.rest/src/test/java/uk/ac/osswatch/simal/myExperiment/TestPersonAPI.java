package uk.ac.osswatch.simal.myExperiment;
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


import org.junit.Test;

import uk.ac.osswatch.simal.rest.SimalAPIException;

public class TestPersonAPI {
  private String personID = "15"; // ID for Ross Gardler

  @Test
  public void testAllColleaguesJSON() throws SimalAPIException {
    /**
     * Commenting out as the MyExperiment server is unstable at present.
     * Thus tests fail
    RESTCommand command = RESTCommand.createGetColleagues(personID, RESTCommand.SOURCE_TYPE_MYEXPERIMENT, RESTCommand.FORMAT_JSON);
    IAPIHandler handler = MyExperimentHandlerFactory.createHandler(command);
    String result = handler.execute();
    assertNotNull(result);
    */
  }

  @Test
  public void testAllColleaguesXML() throws SimalAPIException {
    /**
     * Commenting out as the MyExperiment server is unstable at present.
     * Thus tests fail
    RESTCommand command = RESTCommand.createGetColleagues(personID, RESTCommand.SOURCE_TYPE_MYEXPERIMENT, RESTCommand.FORMAT_XML);
    IAPIHandler handler = MyExperimentHandlerFactory.createHandler(command);
    String result = handler.execute();
    assertNotNull(result);    
     */
  }
}

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


import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rest.IAPIHandler;
import uk.ac.osswatch.simal.rest.RESTCommand;
import uk.ac.osswatch.simal.rest.SimalAPIException;

/**
 * A factory class for generating a specific handler for a
 * given MyExperiment API request.
 * 
 */
public class MyExperimentHandlerFactory {

  /**
   * Create the required API handler for a given command.
   * 
   * @param command the command to execute
   * @param url the URL of the MyExperiment server
   * @return
   * @throws SimalAPIException 
   */
  public static IAPIHandler createHandler(RESTCommand command) throws SimalAPIException {
    IAPIHandler handler = null;
    if (command.isPersonCommand()) {
      try {
        handler = new PersonAPI(command);
      } catch (SimalRepositoryException e) {
        throw new SimalAPIException("Unable to create API handler", e);
      }
    }
    
    if (handler == null) {
      throw new SimalAPIException("Unable to create API Handler for command (" + command + "}");
    }
    
    return handler;
    
  }
}

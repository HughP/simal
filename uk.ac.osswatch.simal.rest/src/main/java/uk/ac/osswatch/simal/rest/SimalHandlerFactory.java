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

import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A factory class for generating a specific handler for a given API request.
 * 
 */
public class SimalHandlerFactory {

  /**
   * Create the required API handler for a given command.
   * 
   * @param command
   *          the command to execute
   * @param repo
   *          the repo that the command is to operate on
   * @return
   * @throws SimalAPIException
   */
  public static IAPIHandler createHandler(RESTCommand command,
      ISimalRepository repo) throws SimalAPIException {
    IAPIHandler handler = null;

    try {
      if (command.isProjectCommand()) {
        handler = new ProjectAPI(command);
      } else if (command.isPersonCommand()) {
        handler = new PersonAPI(command);
      } else if (command.isCategoryCommand()) {
        handler = new CategoryAPIHandler(command);
      }
    } catch (SimalRepositoryException e) {
      throw new SimalAPIException("Unable to create handler", e);
    }

    if (handler == null) {
      throw new SimalAPIException("Unable to create API Handler for command ("
          + command + "}");
    }

    return handler;

  }
}

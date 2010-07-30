package uk.ac.osswatch.simal.rest;

/*
 * Copyright 2010 University of Oxford
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

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A class for handling all API calls relating to categories.
 * 
 */
public class CategoryAPIHandler extends AbstractHandler {

  /**
   * Create a new CategoryAPI object to process the given command.
   * 
   * @param cmd
   * @throws SimalRepositoryException
   */
  protected CategoryAPIHandler(RESTCommand cmd) throws SimalRepositoryException {
    super(cmd);
  }

  /**
   * Execute a command.
   * 
   * @param cmd
   * @throws SimalAPIException
   */
  public String execute() throws SimalAPIException {
    String execResult = null;
    
    if (command.isGetAllCategories()) {
      execResult = getAllCategories(command);
    } else {
      throw new SimalAPIException("Unknown command: " + command);
    }
    
    return execResult;
  }

/**
   * Get all the categories from the repository.
   * 
   * @param cmd
   * @return
   * @throws SimalAPIException
   */
  public String getAllCategories(RESTCommand cmd) throws SimalAPIException {
    if (cmd.isJSON()) {
      try {
        return SimalRepositoryFactory.getCategoryService().getAllCategoriesAsJSON();
      } catch (SimalRepositoryException e) {
        throw new SimalAPIException(
            "Unable to get JSON representation of all categories from the repository",
            e);
      }
    } else {
      throw new SimalAPIException("Unkown format requested - " + cmd);
    }
  }

}

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


import uk.ac.osswatch.simal.myExperiment.MyExperimentHandlerFactory;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryFactory;

/**
 * A factory class for REST command handlers.
 * 
 */
public class HandlerFactory {

  private static ISimalRepository simalRepo;
  private static HandlerFactory factory;

  /**
   * No need to instantiate this calls, use get(RESTCommand cmd) instead.
   * 
   * @throws SimalRepositoryException
   */
  private HandlerFactory() throws SimalAPIException {
    try {
      simalRepo = SimalRepositoryFactory.getInstance(SimalRepositoryFactory.TYPE_JENA);
    } catch (SimalRepositoryException e) {
      throw new SimalAPIException("Unable to create HandlerFactory", e);
    }
  }

  public HandlerFactory(ISimalRepository repo) throws SimalAPIException {
    if (!repo.isInitialised()) {
      throw new SimalAPIException("Supplied repository has not been initialised, please initialise before handing to the HandlerFactory");
    }
    simalRepo = repo;
  }

  /**
   * Get a handler for the supplied command and repository.
   * 
   * @param cmd
   * @return
   * @throws SimalAPIException
   */
  public static IAPIHandler get(RESTCommand cmd, ISimalRepository repo) throws SimalAPIException {
    initFactory(repo);
    
    if (cmd.getSource().equals(RESTCommand.TYPE_SIMAL)) {
      return SimalHandlerFactory.createHandler(cmd, simalRepo);
    } else if (cmd.getSource().equals(RESTCommand.TYPE_MYEXPERIMENT)) {
      return MyExperimentHandlerFactory.createHandler(cmd);
    } else {
      throw new SimalAPIException("Unable to get handler for source type "
          + cmd.getSource());
    }
  }

  /**
   * Initialise the handler factory if not already done so. 
   * This initialise routine uses the supplied repository.
   * @param repo 
   * 
   * @throws SimalAPIException
   */
  private static void initFactory(ISimalRepository repo) throws SimalAPIException {
    if (factory == null) {
      factory = new HandlerFactory(repo);
    }
  }

  /**
   * Initialise the handler factory if not already done so. This
   * init method uses the default settings for the factory.
   * 
   * @throws SimalAPIException
   */
  private static void initFactory() throws SimalAPIException {
    if (factory == null) {
      factory = new HandlerFactory();
    }
  }
  
  /**
   * Get the Simal repository this HandlerFactory is working with.
   * @throws SimalAPIException if unable to connect to the repo
   */
  public static ISimalRepository getSimalRepository() throws SimalAPIException {
    initFactory();
    return simalRepo;
  }
}

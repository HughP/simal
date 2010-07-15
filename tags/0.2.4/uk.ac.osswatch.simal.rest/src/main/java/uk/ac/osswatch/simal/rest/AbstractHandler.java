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

import java.net.URI;
import java.net.URISyntaxException;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * An abstract handler for the REST API. All handlers should extend this
 * abstract class.
 * 
 */
public abstract class AbstractHandler implements IAPIHandler {
  protected RESTCommand command;
  protected HandlerFactory handlerFactory;
  private String baseurl;

  /**
   * Create a handler to operate on a given repository. Handlers should not be
   * instantiated directly, use HandlerFactory.createHandler(...) instead.
   * 
   * @param cmd
   * @throws SimalRepositoryException
   */
  protected AbstractHandler(RESTCommand cmd) throws SimalRepositoryException {
    this.command = cmd;
    this.baseurl = SimalProperties
        .getProperty(SimalProperties.PROPERTY_REST_BASEURL);
  }

  public URI getStateURI() throws URISyntaxException {
    URI uri;
    try {
      uri = new URI(baseurl + command.getPath());
    } catch (SimalAPIException e) {
      throw new URISyntaxException("Can't create state URI", e.getMessage());
    }
    return uri;
  }

  /**
   * Get the Base URI for this command.
   * 
   * @return
   */
  public String getBaseURI() {
    if (command.getSource().equals(RESTCommand.TYPE_SIMAL)) {
      return baseurl;
    } else if (command.getSource().equals(RESTCommand.TYPE_MYEXPERIMENT)) {
      return "http://www.myexperiment.org";
    }
    return null;
  }

  /**
   * Get the repository this factory is working for.
   * 
   * @return
   * @throws SimalAPIException
   */
  protected ISimalRepository getRepository() throws SimalAPIException {
    if (handlerFactory == null) {
      handlerFactory = new HandlerFactory();
    }
    return handlerFactory.getSimalRepository();
  }
}

package uk.ac.osswatch.simal.openSocial;
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


import java.net.URISyntaxException;
import org.apache.shindig.social.samplecontainer.XmlStateFileFetcher;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rest.HandlerFactory;
import uk.ac.osswatch.simal.rest.IAPIHandler;
import uk.ac.osswatch.simal.rest.RESTCommand;
import uk.ac.osswatch.simal.rest.SimalAPIException;
import uk.ac.osswatch.simal.wicket.UserApplication;

/**
 * A class for retrieving social data from the Simal Rest API.
 * 
 */
public class RESTFetcher {

  /**
   * This class should not be instantiated directly.
   * 
   * @throws UserReportableException
   * @see RESTFetcher#get()
   */
  private RESTFetcher() {
  };

  /**
   * Get a RESTFetcher that will respond to the supplied
   * command.
   * @param fetcher 
   * 
   * @return
   * @throws SimalAPIException 
   */
  public static XmlStateFileFetcher get(RESTCommand cmd, XmlStateFileFetcher fetcher) throws SimalAPIException {
    IAPIHandler handler;
    try {
      handler = HandlerFactory.get(cmd, UserApplication.getRepository());
      fetcher.resetStateFile(handler.getStateURI());
    } catch (URISyntaxException e) {
      throw new SimalAPIException("Unable to create state URI", e);
    } catch (SimalRepositoryException e) {
      throw new SimalAPIException("Unable to connect to the repository", e);
    }  
    return fetcher;
  }

}


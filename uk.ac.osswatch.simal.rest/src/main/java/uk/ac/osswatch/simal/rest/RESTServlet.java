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


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryFactory;

/**
 * A servlet class that allows multiple resources to be used for retrieving
 * data.
 * 
 */
public class RESTServlet extends HttpServlet {
  private static final long serialVersionUID = -7003783530005464708L;
  HandlerFactory handlerFactory;

  public RESTServlet() {
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    PrintWriter out = res.getWriter();

    RESTCommand cmd = RESTCommand.createCommand(req.getPathInfo());

    String response = "Could not handle request for " + req.getPathInfo();

    try {
      ISimalRepository repo = SimalRepositoryFactory.getInstance(SimalRepositoryFactory.TYPE_JENA);
      if (!repo.isInitialised()) {
        repo.initialise();
      }
      handlerFactory = new HandlerFactory(repo);
      IAPIHandler handler = handlerFactory.get(cmd);
      response = handler.execute();
    } catch (SimalAPIException e) {
      response = errorResponse(e);
    } catch (SimalRepositoryException e) {
      response = errorResponse(new SimalAPIException(
          "Unable to connect to repository", e));
    } finally {
      if (cmd.isXML()) {
        res.setContentType("text/xml; charset=UTF-8");
      } else if (cmd.isJSON()) {
        res.setContentType("application/json; charset=UTF-8");
      }
      out.println(response);
      out.close();
    }
  }

  /**
   * Generate an Error response to be returned to the client.
   * 
   * @param e
   * @return
   */
  private String errorResponse(SimalAPIException e) {
    String response;
    response = "ERROR: " + e.getMessage();
    return response;
  }
}

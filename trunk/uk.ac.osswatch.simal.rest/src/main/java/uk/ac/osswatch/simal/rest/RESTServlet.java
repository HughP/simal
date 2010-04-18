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
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A servlet class that allows multiple resources to be used for retrieving
 * data.
 * 
 */
public class RESTServlet extends HttpServlet {
  private static final long serialVersionUID = -7003783530005464708L;
  private static final Logger logger = LoggerFactory
      .getLogger(RESTServlet.class);

  public RESTServlet() {
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    if (logger.isTraceEnabled()) {
      logRequest(req);
    }
    PrintWriter out = res.getWriter();

    RESTCommand cmd;
    try {
      cmd = RESTCommand.createCommand(req.getPathInfo());
    } catch (SimalAPIException e) {
      throw new ServletException("Unable to create Simal REST command", e);
    }
    
    if (!cmd.isGet()) {
      throw new ServletException(cmd.getCommandMethod() + " cannot be handled using the GET HTTP method");
    }

    String response = "Could not handle request for " + req.getPathInfo();

    try {
      ISimalRepository repo = SimalRepositoryFactory.getInstance();
      if (!repo.isInitialised()) {
        repo.initialise();
      }
      HandlerFactory handlerFactory = new HandlerFactory(repo);
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

  public void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    if (logger.isTraceEnabled()) {
      logRequest(req);
    }
    PrintWriter out = res.getWriter();

    RESTCommand cmd;
    try {
      cmd = RESTCommand.createCommand(req.getPathInfo(), req.getParameterMap());
    } catch (SimalAPIException e) {
      logger.warn("Error craeting Simal REST command: " + e.getMessage());
      throw new ServletException("Unable to create Simal REST command", e);
    }
    
    if (!cmd.isPost()) {
      throw new ServletException(cmd.getCommandMethod() + " cannot be handled using the POST HTTP method");
    }

    String response = "Could not handle request for " + req.getPathInfo();

    try {
      ISimalRepository repo = SimalRepositoryFactory.getInstance();
      if (!repo.isInitialised()) {
        repo.initialise();
      }
      HandlerFactory handlerFactory = new HandlerFactory(repo);
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

  private void logRequest(HttpServletRequest req) {
    StringBuilder msg = new StringBuilder("Recieved Request: ");
    msg.append(req.getRequestURL());
    msg.append(" from ");
    msg.append(req.getRemoteHost());
    msg.append(" (");
    msg.append(req.getRemoteUser());
    msg.append(")\n");
    msg.append("Contents (length ");
    msg.append(req.getContentLength());
    msg.append("):\n");
    msg.append(getRequestParameters(req));
    logger.trace(msg.toString());
  }

  /**
   * Get all parameter names and values from the request.
   * @return String representation of parameters
   */
  @SuppressWarnings("unchecked")
  private String getRequestParameters(HttpServletRequest req) { 
    StringBuilder msg = new StringBuilder();
    Enumeration<String> allParams = req.getParameterNames();

    while (allParams.hasMoreElements()) {
      String curParam = allParams.nextElement();
      msg.append(curParam);
      msg.append("=");
      msg.append(req.getParameter(curParam));
      msg.append('\n');
    }

    return msg.toString();
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

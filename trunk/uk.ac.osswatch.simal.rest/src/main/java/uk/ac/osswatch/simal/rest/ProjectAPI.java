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

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.jena.Project;
import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFXMLUtils;

/**
 * A class for handling all API calls relating to projects.
 * 
 */
public class ProjectAPI extends AbstractHandler {

  /**
   * Create a new project API object to operate on projects in a given Simal
   * repository. Handlers should not be instantiated directly, use
   * HandlerFactory.createHandler(...) instead.
   * 
   * @param repo
   * @throws SimalRepositoryException
   */
  protected ProjectAPI(RESTCommand cmd) throws SimalRepositoryException {
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
    
    if (command.isGetAllProjects()) {
      execResult = getAllProjects(command);
    } else if (command.isGetProject()) {
      execResult = getProject(command);
    } else if (command.isAddProject()) {
      IProject newProject = addProject(command);
      if (newProject instanceof Project) {
        execResult = ((Project) newProject).generateURL();
      }
    } else {
      throw new SimalAPIException("Unknown command: " + command);
    }
    
    return execResult;
  }

/**
   * Get all the projects from the repository.
   * 
   * @param req
   * @param cmd
   * @return
   * @throws SimalAPIException
   */
  public String getAllProjects(RESTCommand cmd) throws SimalAPIException {
    if (cmd.isJSON()) {
      try {
        return getRepository().getAllProjectsAsJSON();
      } catch (SimalRepositoryException e) {
        throw new SimalAPIException(
            "Unable to get JSON representation of all projects from the repository",
            e);
      }
    } else {
      throw new SimalAPIException("Unkown format requested - " + cmd);
    }
  }

  /**
   * Get a single project from the repository.
   * 
   * @param command
   * @return
   * @throws SimalAPIException
   */
  private String getProject(RESTCommand command) throws SimalAPIException {
    String id = command.getProjectID();
    IProject project;
    
    try {
	    if (id.equals("featured")) {
	      project = getRepository().getFeaturedProject();
	    } else {
	      project = SimalRepositoryFactory.getProjectService().getProjectById(
	        getRepository().getUniqueSimalID(id));
	    }
	    if (project == null) {
	      throw new SimalAPIException("Project with Simal ID " + id
	          + " does not exist");
	    }
    } catch (SimalRepositoryException e) {
      throw new SimalAPIException(
        "Unable to get XML representation of project from the repository",
        e);
    }
    
    try {
	    if (command.isXML()) {
	    	return project.toXML();
	    } else if (command.isJSON()) {
	    	return project.toJSON();
	    } else {
	    	throw new SimalAPIException("Unkown format requested - " + command);
	    }
    } catch (SimalRepositoryException e) {
    	throw new SimalAPIException("Unable to convert project to the chosen format", e);
    }
  }

  /**
   * Attempt to add a project using supplied data.
   * 
   * @param command
   * @throws SimalAPIException
   *           if the project was not added for any reason
   */
  private IProject addProject(RESTCommand command) throws SimalAPIException {
    IProject newProject = null;
    String rdfXml = command.getParameter(RESTCommand.PARAM_RDF);
    
    if(rdfXml == null) {
      throw new SimalAPIException("Did not find RDF/XML data to add project from.");
    }
    
    try {
      newProject = SimalRepositoryFactory.getProjectService().createProject(RDFXMLUtils.convertXmlStringToDom(rdfXml));
    } catch (SimalException e) {
      throw new SimalAPIException("Unable to add RDF data", e);
    }
    
    return newProject;
  }
}

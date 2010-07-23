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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * Command objects for the REST API. This object is used to represent a command
 * in the API and provides convenience methods for extracting parameters.
 */
public final class RESTCommand {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(RESTCommand.class);

  public static final String ALL_PROJECTS = "/allProjects";
  public static final String GET_PROJECT = "/project";
  public static final String PROJECT_ADD = "/addProject";
  public static final String ALL_PEOPLE = "/allPeople";
  public static final String PERSON = "/person";
  public static final String ALL_COLLEAGUES = "/allColleagues";

  public static final String PARAM_PERSON_ID = "/person-";
  public static final String PARAM_PROJECT_ID = "/project-";
  public static final String PARAM_PERSON_EMAIL = "/email-";
  public static final String PARAM_SOURCE = "/source-";
  public static final String PARAM_RDF = "rdf";

  public static final String FORMAT_XML = "/xml";
  public static final String FORMAT_JSON = "/json";

  public static final String TYPE_SIMAL = "simal";
  public static final String TYPE_MYEXPERIMENT = "myExperiment";

  public static final String CONTENT_TYPE_TEXT_XML = "text/xml";
  
  private static String PARAM_METHOD = "method";
  private static String PARAM_FORMAT = "format";

  private transient Map<String, String> params = new HashMap<String, String>();

  /**
   * This class should not be instantiated directly, use the create*(...)
   * methods instead.
   */
  private RESTCommand(String resourceID, String source, String command,
      String format) {
    params.put(PARAM_SOURCE, source);
    params.put(PARAM_METHOD, command);
    params.put(PARAM_FORMAT, format);
    if (isPersonCommand()) {
      params.put(PARAM_PERSON_ID, resourceID);
    } else {
      params.put(PARAM_PROJECT_ID, resourceID);
    }
  }

  /**
   * Create a new, but empty command object.
   */
  private RESTCommand() {
  }

  /**
   * Create a command to retrieve all colleagues of a given person, from a given
   * source.
   * 
   * @param personID
   *          the ID of the person we are searching on
   * @param source
   *          the source of the data required. See the TYPE_* constants
   * @param format
   *          the format that the data should be returned in. See the FORMAT_*
   *          constants
   * @return
   */
  public static RESTCommand createGetColleagues(String personID, String source,
      String format) {
    return new RESTCommand(personID, source, ALL_COLLEAGUES, format);
  }

  /**
   * Create a command to retrieve the RDF for a project, from a given source.
   * 
   * @param projectID
   *          the ID of the project
   * @param source
   *          the source of the data required. See the TYPE_* constants
   * @param format
   *          the format that the data should be returned in. See the FORMAT_*
   *          constants
   * @return
   */
  public static RESTCommand createGetProject(String projectID, String source,
      String format) {
    return new RESTCommand(projectID, source, GET_PROJECT, format);
  }

  /**
   * Create a command to retrieve the RDF for a person, from a given source.
   * 
   * @param personID
   *          the ID of the person
   * @param source
   *          the source of the data required. See the TYPE_* constants
   * @param format
   *          the format that the data should be returned in. See the FORMAT_*
   *          constants
   * @return
   */
  public static RESTCommand createGetPerson(String personID, String source,
      String format) {
    return new RESTCommand(personID, source, PERSON, format);
  }

  /**
   * A code that indicates the data source against this command should be run.
   * 
   * @return
   */
  public String getSource() {
    return params.get(PARAM_SOURCE);
  }

  /**
   * Get the ID of the person that this command relates to. The ID is the id of
   * the person within the source repository.
   * 
   * @return The id of the person or null if not applicable.
   */
  public String getPersonID() {
    return params.get(PARAM_PERSON_ID);
  }

  /**
   * Get the EMail of the person that this command relates to. 
   * 
   * @return The EMAil of the person or null if not applicable.
   */
  public String getPersonEMail() {
    return params.get(PARAM_PERSON_EMAIL);
  }

  /**
   * Set the EMail of the person that this command relates to. 
   * 
   * @param email The EMAil of the person or null if not applicable.
   */
  public void setPersonEMail(String email) {
    params.put(PARAM_PERSON_EMAIL, email);
  }
  
  /**
   * Test to see if this command is a person command. That is, a command that
   * operates on a person.
   * 
   * @return
   */
  public boolean isProjectCommand() {
    if (isGetAllProjects() || isGetProject() || isAddProject()) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Test to see if this command is a person command. That is, a command that
   * operates on a person.
   * 
   * @return
   */
  public boolean isPersonCommand() {
    if (isGetPerson() || isGetColleagues() || isGetAllPeople()) {
      return true;
    }
    return false;
  }

  /**
   * Test to see if this command is a getAllPEople command.
   * 
   * @return
   */
  public boolean isGetAllPeople() {
    if (params.get(PARAM_METHOD).equals(ALL_PEOPLE)) {
      return true;
    }
    return false;
  }

  /**
   * Test to see if this command is a getPerson command.
   * 
   * @return
   */
  public boolean isGetPerson() {
    if (params.get(PARAM_METHOD).equals(PERSON)) {
      return true;
    }
    return false;
  }

  /**
   * Test to see if this command is a getAllColleagues command.
   * 
   * @return
   */
  public boolean isGetColleagues() {
    if (params.get(PARAM_METHOD).equals(ALL_COLLEAGUES)) {
      return true;
    }
    return false;
  }

  /**
   * Test to see if this command is to return data in JSON format.
   * 
   * @return
   */
  public boolean isJSON() {
    if (params.get(PARAM_FORMAT).equals(FORMAT_JSON)) {
      return true;
    }
    return false;
  }

  /**
   * Test to see if this command is to return data in JSON format.
   * 
   * @return
   */
  public boolean isXML() {
    if (params.get(PARAM_FORMAT).equals(FORMAT_XML)) {
      return true;
    }
    return false;
  }


  /**
   * Create a command object that represents a complete REST command string
   * 
   * @param cmdString
   *          the pathInfo part of the request URI
   * @param paramMap
   *          the parameters supplied in the request
   * @return
   * @throws SimalAPIException 
   */
  @SuppressWarnings("unchecked")
  public static RESTCommand createCommand(HttpServletRequest req) throws SimalAPIException {
    String cmdString = req.getPathInfo();
    Map<String, String[]> paramMap = req.getParameterMap();
    RESTCommand cmd = createCommand(cmdString);
    
    Iterator<Entry<String, String[]>> entries = paramMap.entrySet().iterator();
    while (entries.hasNext()) {
      Entry<String, String[]> entry = entries.next();
      String key= entry.getKey();
      String[] values = entry.getValue();
      for (int i = 0; i < values.length; i++) {
        cmd.addParameter(key, values[i]);
      }
    }

    if(cmd.isAddProject() && req.getContentType().equals(CONTENT_TYPE_TEXT_XML)) {
      cmd.addParameter(PARAM_RDF, readRequestBody(req));
    }
    
    return cmd;
  }
  
  /**
   * Extract the body of the HttpServletRequest
   * @param req
   * @return
   */
  private static String readRequestBody(HttpServletRequest req) {
    StringBuffer requestBody = new StringBuffer();
    try {
      BufferedReader reader = req.getReader();
      String currentLine = null;
      while((currentLine = reader.readLine()) != null) {
        requestBody.append(currentLine);
      }
    } catch (IOException e) {
      LOGGER.warn("Could not read request body of http req: " + e.getMessage(),e);
    }
    return requestBody.toString();
  }
  
  /**
   * Create a command object that represents a complete REST command string
   * 
   * @param cmdString
   *          the pathInfo part of the request URI
   * @return
   * @throws SimalAPIException
   */
  public static RESTCommand createCommand(String cmdString)
      throws SimalAPIException {
    if (cmdString != null) {
  	  if (cmdString.contains("://")) {
		throw new SimalAPIException("the command string should not contain a protocol or host and path to the REST-API. It should only contain the command string.");
	  }
      RESTCommand cmd = new RESTCommand();
      cmd.setCommandMethod(extractCommandMethod(cmdString));
      cmd.setPersonID(extractPersonId(cmdString));
      cmd.setPersonEMail(extractPersonEMail(cmdString));
      cmd.setProjectID(extractProjectId(cmdString));
      cmd.setSource(extractSource(cmdString));
      cmd.setFormat(extractFormat(cmdString));
      return cmd;
    } else {
      throw new SimalAPIException("No command string was submitted.");
    }
  }

  /**
   * Extract the format required from the supplied URI command string.
   * 
   * @param cmdString
   *          the PathInfo portion of a URI representing a REST command
   * @return the format data should be returned in
   */
  private static String extractFormat(String cmdString) {
    String format = cmdString.substring(cmdString.lastIndexOf("/"));
    return format;
  }

  /**
   * Extract the source from the supplied URI command string. If no "source-"
   * parameter exists then assume the source is Simal.
   * 
   * @param cmdString
   *          the PathInfo portion of a URI representing a REST command
   * @return the data source for this command
   */
  private static String extractSource(String cmdString) {
    int paramStart = cmdString.indexOf(PARAM_SOURCE);
    if (paramStart < 0) {
      return TYPE_SIMAL;
    } else {
      paramStart = paramStart + PARAM_SOURCE.length();
    }
    int paramEnd = cmdString.indexOf("/", paramStart);
    return cmdString.substring(paramStart, paramEnd);
  }

  /**
   * Extract the person ID from the supplied URI command string.
   * 
   * @param cmdString
   *          the PathInfo portion of a URI representing a REST command
   * @return the person ID if it is present, or null if not present.
   */
  private static String extractPersonId(String cmdString) {
    int paramStart = cmdString.indexOf(PARAM_PERSON_ID)
        + PARAM_PERSON_ID.length();
    int paramEnd = cmdString.indexOf("/", paramStart);
    if (paramEnd < 0 || paramStart < PARAM_PERSON_ID.length()) {
      return null;
    } else {
      return cmdString.substring(paramStart, paramEnd);
    }
  }

  /**
   * Extract the project ID from the supplied URI command string.
   * 
   * @param cmdString
   *          the PathInfo portion of a URI representing a REST command
   * @return the project ID if it is present, or null if not present.
   */
  private static String extractProjectId(String cmdString) {
    int paramStart = cmdString.indexOf(PARAM_PROJECT_ID)
        + PARAM_PROJECT_ID.length();
    int paramEnd = cmdString.indexOf("/", paramStart);
    if (paramEnd < 0 || paramStart < PARAM_PROJECT_ID.length()) {
      return null;
    } else {
      return cmdString.substring(paramStart, paramEnd);
    }
  }
  
  /**
   * Extract the person EMail from the supplied URI command string.
   * 
   * @param cmdString
   *          the PathInfo portion of a URI representing a REST command
   * @return the person EMail if it is present, or null if not present.
   */
  private static String extractPersonEMail(String cmdString) {
    int paramStart = cmdString.indexOf(PARAM_PERSON_EMAIL)
        + PARAM_PERSON_EMAIL.length();
    int paramEnd = cmdString.indexOf("/", paramStart);
    if (paramEnd < 0 || paramStart < PARAM_PERSON_EMAIL.length()) {
      return null;
    } else {
      return cmdString.substring(paramStart, paramEnd);
    }
  }

  /**
   * Extract the command method from the supplied URI command string.
   * 
   * @param cmdString
   *          the PathInfo portion of a URI representing a REST command
   * @return
   */
  private static String extractCommandMethod(String cmdString) {
    int commandEnd = cmdString.indexOf("/", 1);
    if (commandEnd > 0) {
      return cmdString.substring(0, commandEnd);
    } else {
      return cmdString;
    }
  }

  /**
   * Get a string that represents the command method. See the * constants
   * 
   * @return
   */
  public String getCommandMethod() {
    return params.get(PARAM_METHOD);
  }

  /**
   * Set the command method. See the * constants
   * 
   * @return
   */
  public void setCommandMethod(String commandMethod) {
    params.put(PARAM_METHOD, commandMethod);
  }

  public String getFormat() {
    return params.get(PARAM_FORMAT);
  }

  public void setFormat(String format) {
    params.put(PARAM_FORMAT, format);
  }

  public void setSource(String source) {
    params.put(PARAM_SOURCE, source);
  }

  public void setPersonID(String personID) {
    params.put(PARAM_PERSON_ID, personID);
  }

  public String getProjectID() {
    return params.get(PARAM_PROJECT_ID);
  }

  private void setProjectID(String id) {
    params.put(PARAM_PROJECT_ID, id);
  }

  /**
   * Return true if this command is a get all projects command.
   * 
   * @return
   */
  public boolean isGetAllProjects() {
    if (params.get(PARAM_METHOD).equals(ALL_PROJECTS)) {
      return true;
    }
    return false;
  }

  /**
   * Return true if this command is to get a single project.
   * 
   * @return
   */
  public boolean isGetProject() {
    if (params.get(PARAM_METHOD).equals(GET_PROJECT)) {
      return true;
    }
    return false;
  }

  /**
   * Return true if this command is to add a project.
   * 
   * @return
   */
  public boolean isAddProject() {
    if (params.get(PARAM_METHOD).equals(PROJECT_ADD)) {
      return true;
    }
    return false;
  }
  
  /**
   * Return the path info part of the URI that represents this command.
   * 
   * @return
   * @throws SimalAPIException
   *           if the method of the command is unrecognised
   */
  public String getPath() throws SimalAPIException {
    StringBuffer sb = new StringBuffer();
    sb.append(getCommandMethod());
    sb.append(PARAM_SOURCE);
    sb.append(getSource());
    if (isPersonCommand()) {
      sb.append(PARAM_PERSON_ID);
      sb.append(getPersonID());
    } else if (isProjectCommand()) {
      sb.append(PARAM_PROJECT_ID);
      sb.append(getProjectID());
    } else {
      throw new SimalAPIException("Don't recognise the command type");
    }
    sb.append(getFormat());
    return sb.toString();
  }

  /**
   * Get the URL for this REST command.
   * 
   * @throws SimalAPIException
   *           If the method of the command is unrecognised
   */
  public String getURL() throws SimalAPIException {
    try {
      return SimalProperties.getProperty(SimalProperties.PROPERTY_REST_BASEURL)
          + getPath();
    } catch (SimalRepositoryException e) {
      throw new SimalAPIException(
          "unable to get base url from the properties file", e);
    }
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer("\n\nSimal REST Command:\n");
    sb.append("\n\tMethod = ");
    sb.append(getCommandMethod());
    sb.append("\n\tFormat = ");
    sb.append(getFormat());
    sb.append("\n\tPerson ID = ");
    sb.append(getPersonID());
    sb.append("\n\tProject ID = ");
    sb.append(getProjectID());
    sb.append("\n\tSource = ");
    sb.append(getSource());
    sb.append("\n\tParamaters = ");
    sb.append(params);
    sb.append("\n");
    return sb.toString();
  }

  /**
   * Add a parameter to the command.
   * 
   * @param name
   *          the parameter name
   * @param value
   *          the parameter value
   */
  public void addParameter(String name, String value) {
    params.put(name, value);
  }

  /**
   * Get the value of the named parameter.
   * 
   * @param name
   *          paramaeter name
   * @return
   */
  public String getParameter(String name) {
    return params.get(name);
  }

  /**
   * Return true is this command can be handled by an HTTP
   * post.
   * 
   * @return
   */
  public boolean isPost() {
    if (isAddProject()) {
      return true;
    } else {
      return false;
    }
  }
  
  /**
   * Return true is this command can be handled by an HTTP
   * get.
   */
  public boolean isGet() {
    return !isPost();
  }

}

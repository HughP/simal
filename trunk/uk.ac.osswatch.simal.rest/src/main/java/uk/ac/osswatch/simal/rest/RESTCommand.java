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


/**
 * Command objects for the REST API. This object is used to represent a command
 * in the API and provides convenience methods for extracting parameters.
 */
public class RESTCommand {

  public static final String COMMAND_ALL_PROJECTS = "/allProjects";
  public static final String COMMAND_PROJECT = "/project";
  public static final String COMMAND_ALL_COLLEAGUES = "/allColleagues";

  public static final String PARAM_PERSON_ID = "/person-";
  public static final String PARAM_PROJECT_ID = "/project-";
  public static final String PARAM_SOURCE = "/source-";

  public static final String FORMAT_XML = "/xml";
  public static final String FORMAT_JSON = "/json";

  public static String SOURCE_TYPE_SIMAL = "simal";
  public static String SOURCE_TYPE_MYEXPERIMENT = "myExperiment";

  private String source;
  private String personID;
  private String projectID;
  private String commandMethod;
  private String format;

  /**
   * This class should not be instantiated directly, use the create*(...)
   * methods instead.
   */
  private RESTCommand(String personID, String source, String command,
      String format) {
    this.source = source;
    this.personID = personID;
    this.commandMethod = command;
    this.format = format;
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
   *          the source of the data required. See the SOURCE_TYPE_* constants
   * @param format
   *          the format that the data should be returned in. See the FORMAT_*
   *          constants
   * @return
   */
  public static RESTCommand createGetColleagues(String personID, String source,
      String format) {
    return new RESTCommand(personID, source, COMMAND_ALL_COLLEAGUES, format);
  }

  /**
   * A code that indicates the data source against this command should be run.
   * 
   * @return
   */
  public String getSource() {
    return source;
  }

  /**
   * Get the ID of the person that this command relates to. The ID is the id of
   * the person within the source repository.
   * 
   * @return The id of the person or null if not applicable.
   */
  public String getPersonID() {
    return personID;
  }

  /**
   * Test to see if this command is a person command. That is, a command that
   * operates on a person.
   * 
   * @return
   */
  public boolean isPersonCommand() {
    if (isGetColleagues()) {
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
    if (commandMethod.equals(COMMAND_ALL_COLLEAGUES)) {
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
    if ((format.equals(FORMAT_JSON))) {
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
    if ((format.equals(FORMAT_XML))) {
      return true;
    }
    return false;
  }

  /**
   * Create a command object that represents a complete REST command string
   * 
   * @param cmdString
   *          the pathInfo part of the request URI
   * @return
   */
  public static RESTCommand createCommand(String cmdString) {
    RESTCommand cmd = new RESTCommand();
    cmd.setCommandMethod(extractCommandMethod(cmdString));
    cmd.setPersonID(extractPersonId(cmdString));
    cmd.setProjectID(extractProjectId(cmdString));
    cmd.setSource(extractSource(cmdString));
    cmd.setFormat(extractFormat(cmdString));
    return cmd;
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
      return SOURCE_TYPE_SIMAL;
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
   * Extract the command method from the supplied URI command string.
   * 
   * @param cmdString
   *          the PathInfo portion of a URI representing a REST command
   * @return
   */
  private static String extractCommandMethod(String cmdString) {
    int commandEnd = cmdString.indexOf("/", 1);
    return cmdString.substring(0, commandEnd);
  }

  /**
   * Get a string that represents the command method. See the COMMAND_*
   * constants
   * 
   * @return
   */
  public String getCommandMethod() {
    return commandMethod;
  }

  /**
   * Set the command method. See the COMMAND_* constants
   * 
   * @return
   */
  public void setCommandMethod(String commandMethod) {
    this.commandMethod = commandMethod;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public void setPersonID(String personID) {
    this.personID = personID;
  }

  public String getProjectID() {
    return this.projectID;
  }

  private void setProjectID(String id) {
    this.projectID = id;
  }

  /**
   * Return true if this command is a get all projects command.
   * 
   * @return
   */
  public boolean isGetAllProjects() {
    if (commandMethod.equals(COMMAND_ALL_PROJECTS)) {
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
    if (commandMethod.equals(COMMAND_PROJECT)) {
      return true;
    }
    return false;
  }

  /**
   * Return the path info part of the URI that represents this command.
   * 
   * @return
   */
  public String toPathInfo() {
    StringBuffer sb = new StringBuffer();
    sb.append(getCommandMethod());
    sb.append(PARAM_SOURCE);
    sb.append(getSource());
    sb.append(PARAM_PERSON_ID);
    sb.append(getPersonID());
    sb.append(getFormat());
    return sb.toString();
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
    sb.append("\n");
    return sb.toString();
  }

}

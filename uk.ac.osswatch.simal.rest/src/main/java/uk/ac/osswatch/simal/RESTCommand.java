package uk.ac.osswatch.simal;

/**
 * Command objects for the REST API. This object is used to represent a command
 * in the API and provides convenience methods for extracting parameters.
 */
public class RESTCommand {

  public static final String COMMAND_ALL_PROJECTS = "/allProjects/";
  public static final String COMMAND_ALL_COLLEAGUES = "/allColleagues/";

  public static final String PARAM_PERSON_ID = "person-";

  public static final String FORMAT_XML = "/xml";
  public static final String FORMAT_JSON = "/json";

  public static Integer SOURCE_TYPE_AGGREGATE = 0;
  public static Integer SOURCE_TYPE_SIMAL = 100;
  public static Integer SOURCE_TYPE_MYEXPERIMENT = 200;

  private Integer source;
  private String personID;
  private String commandMethod;
  private String format;

  /**
   * This class should not be instantiated directly, use the create*(...)
   * methods instead.
   */
  private RESTCommand(String personID, Integer source, String command, String format) {
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
   *          the format that the data should be returned in.
   *          See the FORMAT_* constants
   * @return
   */
  public static RESTCommand createGetColleagues(String personID, Integer source, String format) {
    return new RESTCommand(personID, source, COMMAND_ALL_COLLEAGUES, format);
  }

  /**
   * A code that indicates the data source against this command should be run.
   * 
   * @return
   */
  public Integer getSource() {
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
   * @return
   */
  public boolean isXML() {
    if ((format.equals(FORMAT_XML))) {
      return true;
    }
    return false;
  }

  /**
   * Create a command object that represents a complete REST
   * command string
   * 
   * @param cmdString the pathInfo part of the request URI
   * @return
   */
  public static RESTCommand createCommand(String cmdString) {
    RESTCommand cmd = new RESTCommand();
    cmd.setCommandMethod(extractCommandMethod(cmdString));
    cmd.setPersonID(extractPersonId(cmdString));
    cmd.setSource(extractSource(cmdString));
    cmd.setFormat(extractFormat(cmdString));
    return cmd;
  }

  /**
   * Extract the format required from the supplied URI command string.
   * @param cmdString the PathInfo portion of a URI representing a REST command
   * @return the format data should be returned in
   */
  private static String extractFormat(String cmdString) {
    String format = cmdString.substring(cmdString.lastIndexOf("/"));
    return format;
  }

  /**
   * Extract the source from the supplied URI command string.
   * @param cmdString the PathInfo portion of a URI representing a REST command
   * @return the data source for this command 
   */
  private static Integer extractSource(String cmdString) {
    // TODO: need to (optionally) encode the source in the cmdString
    return SOURCE_TYPE_SIMAL;
  }

  /**
   * Extract the person ID from the supplied URI command string.
   * @param cmdString the PathInfo portion of a URI representing a REST command
   * @return the person ID if it is present, or null if not present.
   */
  private static String extractPersonId(String cmdString) {
    int paramStart = cmdString.indexOf(PARAM_PERSON_ID) + PARAM_PERSON_ID.length();
    int paramEnd = cmdString.indexOf("/", paramStart); 
    return cmdString.substring(paramStart, paramEnd);
  }

  /**
   * Extract the command method from the supplied URI command string.
   * @param cmdString the PathInfo portion of a URI representing a REST command
   * @return
   */
  private static String extractCommandMethod(String cmdString) {
    return cmdString.substring(0, cmdString.indexOf("/", 1) + 1);
  }

  /**
   * Get a string that represents the command method.
   * See the COMMAND_* constants
   * @return
   */
  public String getCommandMethod() {
    return commandMethod;
  }

  /**
   * Set the command method.
   * See the COMMAND_* constants
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

  public void setSource(Integer source) {
    this.source = source;
  }

  public void setPersonID(String personID) {
    this.personID = personID;
  }
  
  /**
   * Return true if this command is a get all projects 
   * command.
   * 
   * @return
   */
  public boolean isGetAllProjects() {
    if (commandMethod.equals(COMMAND_ALL_PROJECTS)) {
      return true;
    } 
    return false;
  }

}

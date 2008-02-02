package uk.ac.osswatch.simal.model;

import java.io.Serializable;

import uk.ac.osswatch.simal.model.elmo.ProjectException;

/**
 * An interface for wrapping repository specific representations of
 * a Project. Other classes should not access the repository classes
 * directly, instead they should access the data through a class that 
 * implements this interface.
 *
 * @see uk.ac.osswatch.simal.model.elmo.Project
 */
public interface IProject extends Serializable {	
  public String getName() throws ProjectException;
  public void setName(String name) throws ProjectException;
  
  public String getShortDesc() throws ProjectException;
  public void setShortDesc(String shortDesc) throws ProjectException;
  
  /**
   * Get a JSON representation of this project.
   * @return
   */
  public String toJSON();
}

package uk.ac.osswatch.simal.model;

import java.util.Set;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public interface IDoapResource extends IResource {
  /**
   * Returns the default name for this resource/
   * 
   * If no names are supplied then the return value of getLabel() is returned.
   */
  public String getName();

  /**
   * Add a name.
   */
  public void addName(String name);
  
  /**
   * Get a human readable label for this resource.
   * Wherever posssible return a value degined by rds:label,
   * otherwise return a sensible value derived from other data.
   */
  public String getLabel();
  

  /**
   * Return all names associated with this resource. If no names are available
   * then a set containing a single value is returned. this value is generated
   * using a getLabel(true) method call.
   * 
   * @return
   */
  public abstract Set<String> getNames();

  public abstract String getShortDesc();

  public abstract void setShortDesc(String shortDesc);

  public abstract String getCreated();

  public abstract void setCreated(String newCreated)
      throws SimalRepositoryException;

  public abstract String getDescription();

  public abstract void setDescription(String newDescription);

  public abstract Set<IDoapLicence> getLicences();
}

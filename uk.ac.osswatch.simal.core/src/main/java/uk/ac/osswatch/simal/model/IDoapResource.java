package uk.ac.osswatch.simal.model;

import java.io.Serializable;
import java.util.Set;

import javax.xml.namespace.QName;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * An interface for wrapping repository specific representations of
 * a DOAP resource. Other classes should not access the repository classes
 * directly, instead they should access the data through a class that 
 * implements this interface.
 *
 * @see uk.ac.osswatch.simal.model.elmo.DoapResource
 */
public interface IDoapResource extends IResource, Serializable {
	/**
	 * Returns the default name for this resource/
	 * 
	 * If no names are supplied then the return value of 
	 * getLabel() is returned.
	 */
	public String getName();
	
	/**
	 * Return all names associated with this resource.
	 * If no names are available then a set containing a single
	 * value is returned. this value is generated using a
	 * getLabel(true) method call.
	 * 
	 * @return
	 */
	public abstract Set<String> getNames();

	public abstract String getShortDesc();

	public abstract void setShortDesc(String shortDesc);
	
	public abstract QName getQName();
	
	public abstract String getCreated();
	
	
	public abstract void setCreated(String newCreated) throws SimalRepositoryException;
	
	
	public abstract String getDescription();
	
	
	public abstract void setDescription(String newDescription);
	
	
	public abstract Set<String> getLicences();
	
	/**
	 * Get a JSON representation of this resource.
	 * 
	 * @return
	 */
	public abstract String toJSON();
	
	/**
	 * Get a JSON representation of this resource as a JSON record, that
	 * is a representation that is not a complete JSON file but is 
	 * intended to be inserted into another JSON file.
	 * 
	 * @return
	 */
	public abstract String toJSONRecord();

}
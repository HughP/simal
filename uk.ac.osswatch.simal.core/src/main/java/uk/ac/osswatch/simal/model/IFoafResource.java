package uk.ac.osswatch.simal.model;

import java.io.Serializable;

import javax.xml.namespace.QName;

/**
 * An interface for wrapping repository specific representations of
 * a FOAF resource. Other classes should not access the repository classes
 * directly, instead they should access the data through a class that 
 * implements this interface.
 *
 * @see uk.ac.osswatch.simal.model.elmo.FoafResource
 */
public interface IFoafResource extends Serializable {

	public abstract QName getQName();
	
	/**
	 * Get a JSON representation of this project.
	 * 
	 * @param asRecord if set to true then only a single JSON record is returned,otherwie a complete JSON file is returned
	 * @return
	 */
	public abstract String toJSON(boolean asRecord);

}
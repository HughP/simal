package uk.ac.osswatch.simal.model;

/**
 * A wrapper for an RDFS Resource in Elmo.
 * 
 * @see org.opeenrdf.sesame.elmo.concepts.rdfs.IResource
 *
 */
public interface IResource {

	/**
	 * A label for representing the resource to humans.
	 * @return
	 */
	public String getLabel();
	
	/**
	 * A human readable comment describing the resource.
	 * @return
	 */
	public String getComment();
}

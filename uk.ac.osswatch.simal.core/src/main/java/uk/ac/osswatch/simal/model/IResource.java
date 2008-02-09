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
	 * If no label is defined (using rdfs:label) then 
	 * the value of rdfs:resource is returned. 
	 * @return
	 */
	public String getLabel();


	/**
	 * A label for representing the resource to humans.
	 * If no label is defined (using rdfs:label) then 
	 * the value of defaultLabel is returned. If defaultLabel
	 * is null then the value of rdfs:resource is returned.
	 * @return
	 */
	public String getLabel(String defaultLabel);
	
	/**
	 * A human readable comment describing the resource.
	 * @return
	 */
	public String getComment();
}

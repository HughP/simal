package uk.ac.osswatch.simal.model;

public class Person {
	private String fullName;
	private String seeAlso;

	/**
	 * Get the resource URL for any rdf:seeAlso element in this
	 * person description.
	 * @return
	 */
	public String getSeeAlso() {
		return seeAlso;
	}

	/**
	 * Set the resource URL for any rdf:seeAlso element in this
	 * person description.
	 */
	public void setSeeAlso(String seeAlso) {
		this.seeAlso = seeAlso;
	}

	public Person() {
	};

	/**
	 * Create a new Person with a specified name.
	 * 
	 * @param fullName
	 *            the full name of the person.
	 */
	public Person(String fullName) {
		setFullName(fullName);
	}

	/**
	 * Get the full name of this person. If the name provided consists of
	 * multiple names (e.g. first name and last name) this method returns all
	 * names in the correct order.
	 * 
	 * @return
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * Set the full name of this person.
	 */
	public void setFullName(String name) {
		this.fullName = name;
	}
}

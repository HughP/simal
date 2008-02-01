package uk.ac.osswatch.simal.model.elmo;

import java.util.Set;

import javax.xml.namespace.QName;

import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.elmo.sesame.roles.SesameEntity;

import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepository;

/**
 * This is a wrapper around an Elmo Project class.
 * 
 * @see org.openrdf.concepts.doap.Project
 */
public class Project implements IProject {

	private org.openrdf.concepts.doap.Project elmoProject;

	/**
	 * Create a new wrapper around an elmo Project object.
	 * 
	 * @param simalTestProject
	 */
	public Project(org.openrdf.concepts.doap.Project elmoProject) {
		this.elmoProject = elmoProject;
	}

	/**
	 * Returns the first name in the set of names contained within the Elmo
	 * Project class.
	 */
	public String getName() {
		return (String) elmoProject.getDoapNames().toArray()[0];
	}

	public String getShortDesc() {
		return elmoProject.getDoapShortdesc();
	}

	/**
	 * Sets the name of the project. Note that this assumes that there is only
	 * one name. In the event of there being more than one name it throws an
	 * exception.
	 * 
	 * @throws ProjectException
	 *             if the project has more than one name
	 */
	public void setName(String name) throws ProjectException {
		if (elmoProject.getDoapNames() != null) {
			if (elmoProject.getDoapNames().size() != 1) {
				throw new ProjectException(
						"At present the Project wrapper can only handle a single project name.");
			}
			Set<Object> names = elmoProject.getDoapNames();
			names.remove(names.toArray()[0]);
			names.add(name);
		}
	}

	public void setShortDesc(String shortDesc) {
		elmoProject.setDoapShortdesc(shortDesc);
	}

	public QName getQName() {
		// Sesame or Elmo is adding curly brackets around the QName, why?
		// HACK: trip them manually and create a new QName
		if (elmoProject.getQName() != null) {
			String str = elmoProject.getQName().toString();
			return new QName(str.substring(1, str.length() - 1));
		} else {
			return null;
		}
	}
	
	public String toString() {
		return getName();
	}

}

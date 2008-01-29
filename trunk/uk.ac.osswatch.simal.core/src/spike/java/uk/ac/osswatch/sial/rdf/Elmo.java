package uk.ac.osswatch.sial.rdf;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;

import org.openrdf.concepts.doap.Project;
import org.openrdf.concepts.rdf.Seq;
import org.openrdf.elmo.ElmoModule;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.elmo.sesame.SesameManagerFactory;


public class Elmo {

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// See http://www.openrdf.org/doc/elmo/1.0-beta2/xref/index.html
		
		// Create the manager
		SesameManagerFactory factory = new SesameManagerFactory(new ElmoModule());
		SesameManager manager = factory.createElmoManager();
		
		// Create a project
		Project project = createProject(manager);
		System.out.println("Project names are: " + project.getDoapNames());
		
		// Change the names of an existing project
		// note the problem with this is that it adds the new name to the end
		if (project.getDoapNames() != null) {
		    Set<Object> names = project.getDoapNames();
		    names.remove("project 1");
		    names.add("new project name");
		}
		System.out.println("Project names are: " + project.getDoapNames());
		
	}

	@SuppressWarnings("unchecked")
	private static Project createProject(SesameManager manager) {
		Set names = new HashSet();
		names.add("project 1");
		names.add("project 2");
		names.add("project3");
		
		QName qname = new QName("http:example.org/doap");
		Project project = manager.designate(Project.class, qname);
		project.setDoapNames(names);
	
		return project;
	}	

}

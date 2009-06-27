package uk.ac.osswatch.simal.model.jcr;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IInternetAddress;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class Person extends Resource implements IPerson {
	
	public Person(String uri) throws URISyntaxException {
		setPath(new URI(uri));
	}

	public void addEmail(String email) {
		// TODO Auto-generated method stub

	}

	public void addName(String name) {
		// TODO Auto-generated method stub

	}

	public void addSHA1Sum(String sha1) {
		// TODO Auto-generated method stub

	}

	public Set<IPerson> getColleagues() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IInternetAddress> getEmail() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> getGivennames() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IDoapHomepage> getHomepages() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IPerson> getKnows() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> getNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IProject> getProjects() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> getSHA1Sums() {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeName(String name) throws SimalRepositoryException {
		// TODO Auto-generated method stub

	}

	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	public String toJSONRecord() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

}

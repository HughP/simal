package uk.ac.osswatch.simal.model.jcr;
/*
 * Copyright 2007 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IInternetAddress;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class Person extends Resource implements IPerson {
	private static final long serialVersionUID = 1L;

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

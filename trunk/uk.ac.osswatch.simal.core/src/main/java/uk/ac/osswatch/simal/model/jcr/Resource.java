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
import java.util.Set;

import uk.ac.osswatch.simal.model.AbstractResource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class Resource extends AbstractResource {
	private static final long serialVersionUID = 1L;
	private String comment;
	private String label;
	private Set<URI> seeAlsos;
	private String simalID;
	private Set<String> sources;

	public Resource(String simalID) throws SimalRepositoryException {
		super(simalID);
	}

	public String getComment() {
		return comment;
	}

	public String getLabel(String defaultLabel) {
		if (label != null) {
			return label;
		}
		if (defaultLabel != null) {
			return defaultLabel;
		}
		return getURI();
	}

	public Object getRepositoryResource() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<URI> getSeeAlso() {
		return seeAlsos;
	}

	public String getUniqueSimalID() throws SimalRepositoryException {
		return simalID;
	}

	public void setSimalID(String newID) throws SimalRepositoryException {
		this.simalID = newID;
	}

	public void delete() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		
	}

	public Set<String> getSources() {
		return sources;
	}

	public String toXML() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public void addSeeAlso(URI uri) {
		seeAlsos.add(uri);
	}
	

}

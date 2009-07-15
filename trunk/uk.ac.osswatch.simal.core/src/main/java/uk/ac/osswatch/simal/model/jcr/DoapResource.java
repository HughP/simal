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

import uk.ac.osswatch.simal.model.IDoapLicence;
import uk.ac.osswatch.simal.model.IDoapResource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class DoapResource extends Resource implements IDoapResource {
	private static final long serialVersionUID = 1L;

	public DoapResource(String simalID) throws SimalRepositoryException {
		super(simalID);
	}

	public DoapResource() {
		super();
	}

	public void addName(String name) {
		// TODO Auto-generated method stub
		
	}

	public String getCreated() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IDoapLicence> getLicences() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> getNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getShortDesc() {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeName(String name) throws SimalRepositoryException {
		// TODO Auto-generated method stub
		
	}

	public void setCreated(String newCreated) throws SimalRepositoryException {
		// TODO Auto-generated method stub
		
	}

	public void setDescription(String newDescription) {
		// TODO Auto-generated method stub
		
	}

	public void setShortDesc(String shortDesc) {
		// TODO Auto-generated method stub
		
	}

	public String toJSON() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public String toJSONRecord() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getComment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabel(String defaultLabel) {
	    String name = this.getName();
	    if (name == null) {
	    	return super.getLabel(defaultLabel);
	    } else {
	      return name;
	    }
	}

	public Object getRepositoryResource() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<URI> getSeeAlso() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSimalID() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUniqueSimalID() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSimalID(String newID) throws SimalRepositoryException {
		// TODO Auto-generated method stub
		
	}

	public void delete() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		
	}

	public Set<String> getSources() {
		// TODO Auto-generated method stub
		return null;
	}

	public String toXML() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

}

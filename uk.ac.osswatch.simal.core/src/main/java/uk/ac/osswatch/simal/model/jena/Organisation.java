package uk.ac.osswatch.simal.model.jena;
/*
 * 
 Copyright 2007 University of Oxford * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

import com.hp.hpl.jena.vocabulary.RDFS;

import uk.ac.osswatch.simal.model.Foaf;
import uk.ac.osswatch.simal.model.IOrganisation;

public class Organisation extends Resource implements IOrganisation {

	public Organisation(com.hp.hpl.jena.rdf.model.Resource resource) {
		super(resource);
	}

	private static final long serialVersionUID = 1L;

	public String getName() {
	    String name = getLiteralValue(Foaf.NAME);
	    
	    if (name == null) {
	      name = getLiteralValue(RDFS.label);
	    }
	    
	    if (name == null) {
	      return getURI();
	    }
	    return name;
	}

}

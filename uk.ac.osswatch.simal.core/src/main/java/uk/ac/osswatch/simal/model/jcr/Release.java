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

import java.util.Set;

import uk.ac.osswatch.simal.model.IDoapRelease;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class Release extends DoapResource implements IDoapRelease {
	private static final long serialVersionUID = 1L;
	
	public Release() {
		super();
	}

	public Release(String id) throws SimalRepositoryException {
		super(id);
	}

	public Set<String> getRevisions() {
		// TODO Auto-generated method stub
		return null;
	}

}

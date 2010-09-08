/*
 * Copyright 2010 University of Oxford 
 * 
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

package uk.ac.osswatch.simal.service.jena;

import uk.ac.osswatch.simal.model.IDoapResource;
import uk.ac.osswatch.simal.model.jena.DoapResource;
import uk.ac.osswatch.simal.model.jena.simal.JenaSimalRepository;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.service.IDoapResourceService;

public class JenaDoapResourceService extends JenaService implements
    IDoapResourceService {

  /**
   * @param simalRepository
   */
  public JenaDoapResourceService(ISimalRepository simalRepository) {
    super(simalRepository);
  }

  /** 
   * Get a IDoapResource from Jena based on its URI if it's in the repository.
   * @see uk.ac.osswatch.simal.service.IDoapResourceService#get(java.lang.String)
   */
  public IDoapResource get(String uri) {
    JenaSimalRepository repo = ((JenaSimalRepository) getRepository());
    IDoapResource resource = null;

    if (repo != null && repo.containsResource(uri)) {
      resource = new DoapResource(repo.getModel().getResource(uri));
    }

    return resource;
  }

}

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

package uk.ac.osswatch.simal.service;

import uk.ac.osswatch.simal.model.IDoapResource;

/**
 * Simal generic service to retrieve IDoapResources based on their
 * URI only.
 */
public interface IDoapResourceService extends IService {

  /**
   * Get a resource based on its URI encoded in a String.
   * @param uri
   * @return the resource if it exists, null otherwise.
   */
  public IDoapResource get(String uri);
}

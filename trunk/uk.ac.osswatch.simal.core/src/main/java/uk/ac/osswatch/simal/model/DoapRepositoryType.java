package uk.ac.osswatch.simal.model;

import com.hp.hpl.jena.rdf.model.Resource;

import uk.ac.osswatch.simal.rdf.Doap;

/*
 * Copyright 2010 University of Oxford * 
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

/**
 * Enum for all valid types of Repository in DOAP.
 * TODO Borderline design/implementation  
 */
public enum DoapRepositoryType {
  ARCH_REPOSITORY (Doap.ARCH_REPOSITORY, "GNU Arch"), 
  BK_REPOSITORY (Doap.BKREPOSITORY, "BitKeeper"), 
  BAZAAR_REPOSITORY (Doap.BAZAARREPOSITORY, "Bazaar Branch"), 
  CVS_REPOSITORY (Doap.CVSREPOSITORY, "CVS"), 
  DARCS_REPOSITORY (Doap.DARCSREPOSITORY, "Darcs Repository"), 
  GIT_REPOSITORY (Doap.GITREPOSITORY, "Git Repository"), 
  HG_REPOSITORY (Doap.HGREPOSITORY, "Mercurial"), 
  SVN_REPOSITORY (Doap.SVNREPOSITORY, "Subversion");
  
  private Resource resource;
  private String label;
  
  private DoapRepositoryType(Resource resource, String label)  {
    this.resource = resource;
    this.label= label;
  }
  
  public Resource getResource() {
    return this.resource;
  }
  
  public String getLabel() {
    return this.label;
  }

  /**
   * Find the right DoapRepositoryType for a Resource.
   * 
   * @param jenaResource
   * @return matching DoapRepositoryType; returns null if no match found.
   */
  public static DoapRepositoryType getMatchingDoapRepositoryType(
      Resource jenaResource) {
    DoapRepositoryType matchingType = null;

    for (DoapRepositoryType type : values()) {
      if (jenaResource.equals(type.getResource())) {
        matchingType = type;
        break;
      }
    }
    return matchingType;
  }
}
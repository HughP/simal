/*
 * Copyright 2007, 2010 University of Oxford
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
package uk.ac.osswatch.simal.model;

import java.util.Set;

/**
 * A repository that is known to be used by a project. It is a resource
 * used to define the doap:repository entries of doap:Project.
 * 
 */
public interface IDoapRepository extends IDoapResource {

  /**
   * Return the anonymous roots for his repository.
   * 
   * @return
   */
  Set<String> getAnonRoots();

  /**
   * Return the anonymous roots for his repository.
   * 
   * @return
   */
  Set<String> getModule();

  /**
   * Return the anonymous roots for his repository.
   * 
   * @return
   */
  Set<IDocument> getLocations();

  /**
   * Return the anonymous roots for his repository.
   * 
   * @return
   */
  Set<IDocument> getBrowse();

  /**
   * 
   * @param browseAccesses
   */
  void setBrowse(Set<IDocument> browseAccesses);

  /**
   * 
   * @param anonRoots
   */
  void setAnonRoots(Set<String> anonRoots);

  /**
   * 
   * @param modules
   */
  void setModule(Set<String> modules);

  /**
   * 
   * @param locations
   */
  void setLocations(Set<IDocument> locations);


  /**
   * If this repository is an GNU Arch repository return true, else return
   * false.
   * 
   * @return
   */
  boolean isARCH();

  /**
   * If this repository is a Bazaar Branch return true, else return false.
   * 
   * @return
   */
  boolean isBazaar();

  /**
   * If this repository is a BitKeeper repository return true, else return
   * false.
   * 
   * @return
   */
  boolean isBK();

  /**
   * If this repository is a CVS repository return true, else return false.
   * 
   * @return
   */
  boolean isCVS();

  /**
   * If this repository is a darcs source code repository return true, else
   * return false.
   * 
   * @return
   */
  boolean isDarcs();

  /**
   * If this repository is a Git source code repository return true, else return
   * false.
   * 
   * @return
   */
  boolean isGit();

  /**
   * If this repository is a Mercurial source code repository return true, else
   * return false.
   * 
   * @return
   */
  boolean isMercurial();

  /**
   * If this repository is a Subversion source code repository return true, else
   * return false.
   * 
   * @return
   */
  boolean isSVN();

}

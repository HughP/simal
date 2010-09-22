package uk.ac.osswatch.simal.wicket.panel.homepage;

/*
 * Copyright 2008-2010 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");   *
 * you may not use this file except in compliance with the License.  *
 * You may obtain a copy of the License at                           *
 *                                                                   *
 *   http://www.apache.org/licenses/LICENSE-2.0                      *
 *                                                                   *
 * Unless required by applicable law or agreed to in writing,        *
 * software distributed under the License is distributed on an       *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY            *
 * KIND, either express or implied.  See the License for the         *
 * specific language governing permissions and limitations           *
 * under the License.                                                *
 */

import org.apache.wicket.IClusterable;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * An input model for managing a DOAP Homepage object.
 * 
 */
public class DoapHomepageFormInputModel implements IClusterable {
  private static final long serialVersionUID = 1L;
  private String name;
  private String url;

  public DoapHomepageFormInputModel() {
  }

  /**
   * Get the name for this homepage.
   * 
   * @return
   */
  public String getName() {
    return name;
  }

  /**
   * Set the name for this homepage.
   * 
   * @return
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get the homepage defined by this form.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public IDoapHomepage getHomepage() throws SimalRepositoryException {
    IDoapHomepage homepage = SimalRepositoryFactory.getHomepageService().getOrCreate(getUrl());
    // FIXME homepage.addName(getName());
    return homepage;
  }

  /**
   * Get the URL for this homepage.
   * 
   * @return
   */
  public String getUrl() {
    return url;
  }

  /**
   * Set the URl for this homepage.
   * 
   * @return
   */
  public void setUrl(String url) {
    this.url = url;
  }
}

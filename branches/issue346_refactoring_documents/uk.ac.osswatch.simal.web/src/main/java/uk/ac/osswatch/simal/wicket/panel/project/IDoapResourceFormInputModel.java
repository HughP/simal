package uk.ac.osswatch.simal.wicket.panel.project;

/*
 * Copyright 2010 University of Oxford
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

/**
 * An input model for managing any DOAP resource that has a name and URL.
 * In these cases the URL will also be the URI but they are entered by
 * the users as web-accessible URLs. 
 */
public class IDoapResourceFormInputModel implements IClusterable {
  private static final long serialVersionUID = 3298250788209468208L;
  private String name;
  private String url;

  /**
   * Get the name for this resource.
   * 
   * @return
   */
  public String getName() {
    return name;
  }

  /**
   * Set the name for this resource.
   * 
   * @return
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get the URL for this resource.
   * 
   * @return
   */
  public String getUrl() {
    return url;
  }

  /**
   * Set the URl for this resource.
   * 
   * @return
   */
  public void setUrl(String url) {
    this.url = url;
  }
}

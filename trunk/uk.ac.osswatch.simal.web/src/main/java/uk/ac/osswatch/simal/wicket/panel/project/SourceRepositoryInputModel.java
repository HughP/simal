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

import java.util.List;

import org.apache.wicket.IClusterable;

import uk.ac.osswatch.simal.model.DoapRepositoryType;

public class SourceRepositoryInputModel implements IClusterable {

  private static final long serialVersionUID = -2773000218221113991L;

  private String anonymousAccess;
  private String devAccess;
  private String browseAccess;


  private List<DoapRepositoryType> listedCategories;
  
  private DoapRepositoryType comboChoice;

  public DoapRepositoryType getcomboChoice() {
      return comboChoice;
  }

  public void setComboChoice(DoapRepositoryType comboChoice) {
      this.comboChoice = comboChoice;
  }

  public void setListedCategories(List<DoapRepositoryType> listedCategories) {
    this.listedCategories = listedCategories;
  }

  public List<DoapRepositoryType> getListedCategories() {
    return listedCategories;
  }
  public void setAnonymousAccess(String anonymousAccess) {
    this.anonymousAccess = anonymousAccess;
  }

  public String getAnonymousAccess() {
    return anonymousAccess;
  }

  public void setDevAccess(String devAccess) {
    this.devAccess = devAccess;
  }

  public String getDevAccess() {
    return devAccess;
  }

  public void setBrowseAccess(String browseAccess) {
    this.browseAccess = browseAccess;
  }

  public String getBrowseAccess() {
    return browseAccess;
  }
}

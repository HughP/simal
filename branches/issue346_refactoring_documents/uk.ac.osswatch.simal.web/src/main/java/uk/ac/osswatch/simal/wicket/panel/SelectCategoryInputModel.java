package uk.ac.osswatch.simal.wicket.panel;

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

import uk.ac.osswatch.simal.model.IDoapCategory;

/**
 * A Java bean usable as input model for managing a selection of Categories.
 */
public class SelectCategoryInputModel implements IClusterable {

  private static final long serialVersionUID = -6143804114236894073L;

  private List<IDoapCategory> listedCategories;
  
  private IDoapCategory comboChoice;

  public IDoapCategory getcomboChoice() {
      return comboChoice;
  }

  public void setComboChoice(IDoapCategory comboChoice) {
      this.comboChoice = comboChoice;
  }

  public void setListedCategories(List<IDoapCategory> listedCategories) {
    this.listedCategories = listedCategories;
  }

  public List<IDoapCategory> getListedCategories() {
    return listedCategories;
  }
}

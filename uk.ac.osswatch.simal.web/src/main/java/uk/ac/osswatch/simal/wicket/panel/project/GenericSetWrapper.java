package uk.ac.osswatch.simal.wicket.panel.project;

import java.io.Serializable;
import java.util.List;

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

/**
 * Wrapper for items in a set so they can be edited on a form. When the Set is a
 * property of another object it needs to be updated separately, for example
 * when OSes are edited for an IProject.
 * 
 * @param <T>
 */
public class GenericSetWrapper<T> implements Serializable {

  private static final long serialVersionUID = 7273060956309086010L;

  private List<T> editableList;

  public GenericSetWrapper(List<T> editableList) {
    this.editableList = editableList;
  }

  public List<T> getEditableList() {
    return editableList;
  }

  public void setEditableList(List<T> editableList) {
    // FIXME this setter is not called because the 
    // ListItemModel.setObject works directly on the List.
    this.editableList = editableList;
  }

}

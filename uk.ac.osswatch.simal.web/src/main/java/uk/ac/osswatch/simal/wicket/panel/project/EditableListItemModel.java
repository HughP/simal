package uk.ac.osswatch.simal.wicket.panel.project;

/*
 * 
 Copyright 2011 University of Oxford * 
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

import java.util.List;

import org.apache.wicket.markup.html.list.ListItemModel;
import org.apache.wicket.markup.html.list.ListView;

/**
 * Custom version of ListItemModel<T> to allow access to the  
 * ListView's List outside the object. 
 * 
 */
public class EditableListItemModel<T> extends ListItemModel<T>
{
  private static final long serialVersionUID = -8873451739437744645L;

  private final ListView<T> listView;

  /**
   * Create EditableListItemModel but keep the ListView locally so
   * it can be retreived later.
   * 
   * @param listView
   *            The ListView
   * @param index
   *            The index of this model
   */
  public EditableListItemModel(final ListView<T> listView, final int index)
  {
    super(listView, index);
    this.listView = listView;
  }

  /**
   * Return the List<T> this EditableListItemModel is part of.
   * @return
   */
  public List<T> getParentList() {
    return this.listView.getModelObject();
  }
}

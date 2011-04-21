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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.wicket.panel.project.EditProjectPanel.ReadOnlyStyleBehavior;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.tdb.TDB;

public class EditableListView extends ListView<String> {

  private static final long serialVersionUID = 154815894763179933L;
  
  private IProject project;
  
  private PropertyModel<Set<String>> projectModel;

  private ReadOnlyStyleBehavior rosb;

  private Form<IProject> form;

  public EditableListView(String labelWicketID, List<String> editableList, IProject project, ReadOnlyStyleBehavior rosb, Form<IProject> form) {
    super(labelWicketID, editableList);

    this.project = project;
    this.rosb = rosb;
    this.form = form;

    this.projectModel = new PropertyModel<Set<String>>(project, labelWicketID);
    setReuseItems(true);
  }

  protected void populateItem(ListItem<String> item) {
    String wrapper = (String) item.getModelObject();
    TextField<String> setItemValue = new TextField<String>(
        "setItemValue", item.getModel());
    setItemValue.add(this.rosb);
    item.add(setItemValue);
    item.add(generateDeleteItemButton(!EditProjectPanel.NEW_ITEM
        .equals(wrapper), item));
  }

  @Override
  protected IModel<String> getListItemModel(
      IModel<? extends List<String>> listViewModel, int index) {
    
    
    return new EditableListItemModel<String>(this, index) {
      private static final long serialVersionUID = 2563628936377112626L;

      @Override
      public void setObject(String object) {
        super.setObject(object);
        
        Set<String> updatedSet = new HashSet<String>(getParentList());
        projectModel.setObject(updatedSet);

        // FIXME This needs to happen somewhere else
        TDB.sync(((Resource) project.getRepositoryResource()).getModel());
      }
      
    };
  }
  
  private AjaxFallbackButton generateDeleteItemButton(
      boolean visibilityAllowed, ListItem<String> item) {
    AjaxFallbackButton deleteItemButton = new AjaxFallbackDeleteItemButton(
        "deleteItem", new Model<String>("X"), this.form, item);

    deleteItemButton.setVisibilityAllowed(visibilityAllowed);
    deleteItemButton.add(this.rosb);
    return deleteItemButton;

  }
  
  private static class AjaxFallbackDeleteItemButton extends AjaxFallbackButton {

    private static final long serialVersionUID = -6395239712922873605L;

    private ListItem<String> item;

    /**
     * @param id
     * @param model
     * @param form
     */
    public AjaxFallbackDeleteItemButton(String id, IModel<String> model,
        Form<?> form, ListItem<String> item) {
      super(id, model, form);
      this.item = item;
    }

    @Override
    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
      item.getModel().setObject(null);
      item.setVisible(false);
      setVisible(false);
      target.addComponent(form);
    }
  }

}

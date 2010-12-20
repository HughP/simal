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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.UrlValidator;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.DoapRepositoryType;
import uk.ac.osswatch.simal.model.IDoapRepository;
import uk.ac.osswatch.simal.model.IDocument;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.InvalidURIException;
import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.panel.AbstractAddDoapResourcePanel;
import uk.ac.osswatch.simal.wicket.panel.SourceRepositoriesPanel;

public class AddSourceRepositoryPanel extends AbstractAddDoapResourcePanel<IDoapRepository> {

  private static final long serialVersionUID = -7535911641335687669L;

  private SourceRepositoryInputModel inputModel;

  public AddSourceRepositoryPanel(String wicketid,
      SourceRepositoriesPanel updatePanel, boolean editingAllowed) {
    super(wicketid, updatePanel, editingAllowed);
    setOutputMarkupId(true);
  }

  @Override
  protected void processAddSubmit() {
    DoapRepositoryType selectedType = inputModel.getcomboChoice();
    String browseAccess = inputModel.getBrowseAccess();

    if (selectedType != null && !StringUtils.isEmpty(browseAccess)) {
      try {
        IDoapRepository repo = SimalRepositoryFactory.getRepositoryService()
            .create(browseAccess, selectedType);
        String anonymousAccess = inputModel.getAnonymousAccess();
        if (!StringUtils.isEmpty(anonymousAccess)) {
          Set<String> allAnonRoots = new HashSet<String>();
          allAnonRoots.add(anonymousAccess);
          repo.setAnonRoots(allAnonRoots);
        }
        String devAccess = inputModel.getDevAccess();
        if (!StringUtils.isEmpty(devAccess)) {
          Set<IDocument> allDevAccesses = new HashSet<IDocument>();
          IDocument devAccessDocument = SimalRepositoryFactory
              .getHomepageService().getOrCreate(devAccess);
          allDevAccesses.add(devAccessDocument);
          repo.setLocations(allDevAccesses);
        }

        getUpdatePanel().addToDisplayList(repo);
        getUpdatePanel().addToModel(repo);
      } catch (SimalRepositoryException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (SimalException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (DuplicateURIException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (InvalidURIException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

  }

  @Override
  protected void addFormFields(AddDoapResourceForm addDoapResourceForm) {
    DropDownChoice<DoapRepositoryType> typeField;
    TextField<String> devAccessField;
    TextField<String> browseField;

    addDoapResourceForm.add(new RequiredTextField<String>(
        "anonymousAccess"));
    addDoapResourceForm.add(devAccessField = new RequiredTextField<String>(
        "devAccess"));
    devAccessField.add(new UrlValidator());
    addDoapResourceForm.add(browseField = new RequiredTextField<String>(
        "browseAccess"));
    browseField.add(new UrlValidator());

    List<DoapRepositoryType> allDoapRepositoryTypes = Arrays
        .asList(DoapRepositoryType.values());
    typeField = new DropDownChoice<DoapRepositoryType>("listedTypes",
        new PropertyModel<DoapRepositoryType>(inputModel, "comboChoice"),
        allDoapRepositoryTypes, new ChoiceRenderer<DoapRepositoryType>("label",
            "label")) {

      private static final long serialVersionUID = 8793472442509893863L;

      /**
       * Whether this component's onSelectionChanged event handler should called
       * using javascript if the selection changes.
       * 
       * @return True if this component's onSelectionChanged event handler
       *         should called using javascript if the selection changes
       */
      protected boolean wantOnSelectionChangedNotifications() {
        return false;
      }
    };
    addDoapResourceForm.add(typeField);
  }

  @Override
  public Object getInputModel() {
    if (inputModel == null) {
      inputModel = new SourceRepositoryInputModel();
    }
    return inputModel;
  }

}

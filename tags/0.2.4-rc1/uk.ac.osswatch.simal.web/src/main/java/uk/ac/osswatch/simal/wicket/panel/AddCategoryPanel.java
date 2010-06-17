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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.utils.DoapResourceByNameComparator;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.doap.ProjectDetailPage;

/**
 * Container for adding a new category. This is an AJAX enabled container that
 * either shows the form for selecting a category or a command link to display
 * the form.
 */
public class AddCategoryPanel extends AbstractAddDoapResourcePanel {

  private static final long serialVersionUID = -7126291323723696950L;

  public static final Logger LOGGER = LoggerFactory
      .getLogger(AddCategoryPanel.class);

  private SelectCategoryInputModel inputModel;
  DropDownChoice<IDoapCategory> categoryField;

  private CategoryListPanel updatePanel;

  private IProject project;

  /**
   * Create a new container that will initially display the command link to show
   * the form.
   * 
   * @param wicketid
   *          the id of the HTML component to host the container
   * @param updatePanel
   *          the panel that should be updated when the category has been added
   *          (must have setOutputMarkupId(true)
   */
  public AddCategoryPanel(String wicketid, IProject project, CategoryListPanel updatePanel) {
    super(wicketid, updatePanel);
    this.project = project;
    this.updatePanel = updatePanel;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * uk.ac.osswatch.simal.wicket.panel.AbstractAddPanel#addFormFields(uk.ac.
   * osswatch.simal.wicket.panel.AbstractAddPanel.AddDoapResourceForm)
   */
  @Override
  protected void addFormFields(AddDoapResourceForm addDoapResourceForm) {
    inputModel = new SelectCategoryInputModel();
    Set<IDoapCategory> allCategories;
    try {
      allCategories = SimalRepositoryFactory.getCategoryService().getAll();
    } catch (SimalRepositoryException e) {
      LOGGER.warn("Could not retreive all categories for adding new Category");
      allCategories = new HashSet<IDoapCategory>();
    }
    List<IDoapCategory> allCategoriesListed = new ArrayList<IDoapCategory>(
        allCategories);
    Collections.sort(allCategoriesListed, new DoapResourceByNameComparator());

    categoryField = new DropDownChoice<IDoapCategory>("listedCategories",
        new PropertyModel<IDoapCategory>(inputModel, "comboChoice"),
        allCategoriesListed, new ChoiceRenderer<IDoapCategory>("name", "label")) {

      private static final long serialVersionUID = -6627368071258835746L;

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
    addDoapResourceForm.add(categoryField);
  }

  /**
   * Return the inputModel for this panel.
   * 
   * @see uk.ac.osswatch.simal.wicket.panel.AbstractAddPanel#getInputModel()
   */
  @Override
  public Object getInputModel() {
    return this.inputModel;
  }

  /**
   * Add category to the project and to the existing list. 
   * 
   * @see uk.ac.osswatch.simal.wicket.panel.AbstractAddPanel#processAddSubmit()
   */
  @Override
  protected void processAddSubmit() {
    IDoapCategory selectedCategory = inputModel.getcomboChoice();
    if (selectedCategory != null) {
      project.addCategory(selectedCategory);
      updatePanel.addCategory(selectedCategory);
    } else {
      UserReportableException error = new UserReportableException(
          "Unable to generate a category from the given form data",
          ProjectDetailPage.class);
      setResponsePage(new ErrorReportPage(error));
    }
  }

}
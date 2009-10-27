package uk.ac.osswatch.simal.wicket.panel;

/*
 * Copyright 2008 University of Oxford
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
import java.util.List;
import java.util.Set;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.data.SortableProjectDataProvider;
import uk.ac.osswatch.simal.wicket.doap.ProjectDetailPage;
import uk.ac.osswatch.simal.wicket.doap.ProjectFilterInputModel;
import uk.ac.osswatch.simal.wicket.markup.html.repeater.data.table.LinkPropertyColumn;

/**
 * A panel that will list all the Projects in the repository.
 */
public class ProjectListPanel extends Panel {
  private static final long serialVersionUID = -890741585742505383L;
  private static final Logger logger = LoggerFactory.getLogger(ProjectListPanel.class);
  private static ProjectFilterInputModel inputModel = new ProjectFilterInputModel();
  AjaxFallbackDefaultDataTable<SortableProjectDataProvider> projectTable;
  SortableProjectDataProvider dataProvider;
  /**
   * List all projects in the repository.
   * 
   * @param id the wiketID of this component
   * @param numberOfProjects number of projects to display per page
   * @throws SimalRepositoryException
   */
  @SuppressWarnings("serial")
  public ProjectListPanel(String id, int numberOfProjects) throws SimalRepositoryException {
    super(id);
    populatePanel(null, numberOfProjects);
  }

  /**
   * List a set of given projects.
   * 
   * @param string
   * @param projects the projects to display, if null all projects in the repo will be displayed
   * @param numberOfProjects number of projects to display per page
   * @throws SimalRepositoryException
   */
  public ProjectListPanel(String id, Set<IProject> projects, int numberOfProjects)
      throws SimalRepositoryException {
    super(id);
    populatePanel(projects, numberOfProjects);
  }

  /**
   * Update the data displayed by filtering on the name field using a
   * regular expression.
   * 
   * @param nameFilter
   * @throws SimalRepositoryException
   */
  public void filterProjectsByName(String nameFilter) throws SimalRepositoryException {
    dataProvider.filterProjectsByName(nameFilter);
  }

  /**
   * Populate the panel with data.
   * 
   * @param projects -
   *          the list of projects to display or null if all projects in the
   *          repo are to be displayed.
   * @param numberOfProjects 
   * @throws SimalRepositoryException
   */
  @SuppressWarnings("unchecked")
  private void populatePanel(Set<IProject> projects, int numberOfProjects)
      throws SimalRepositoryException {
    add(new ProjectFilterForm("projectFilterForm"));    
    
    List<AbstractColumn> columns = new ArrayList<AbstractColumn>();
    columns.add(new LinkPropertyColumn(new Model("Name"), "name", "name") {
      private static final long serialVersionUID = -2174061702366979017L;

      @Override
      public void onClick(Item item, String componentId, IModel model) {
        IProject project = (IProject) model.getObject();
        getRequestCycle().setResponsePage(new ProjectDetailPage(project));
      }

    });
    columns.add(new PropertyColumn(new Model("Description"), "shortDesc",
        "shortDesc"));

    if (projects == null) {
      dataProvider = new SortableProjectDataProvider();
    } else {
      dataProvider = new SortableProjectDataProvider(projects);
    }
    dataProvider.setSort("name", true);
    projectTable = new AjaxFallbackDefaultDataTable("dataTable", columns, dataProvider, numberOfProjects);
    add(projectTable);
  }
  
  private class ProjectFilterForm extends Form<ProjectFilterInputModel> {
    private static final long serialVersionUID = 4350446873545711199L;
    private TextField<String> nameFilter;
    
    public ProjectFilterForm(String name) {
      super(name, new CompoundPropertyModel<ProjectFilterInputModel>(inputModel));
      nameFilter = new TextField<String>("nameFilter");
      add(nameFilter);
      String[] defaultValue = { "" };
      nameFilter.setModelValue(defaultValue);
    }

    @Override
    protected void onSubmit() {
      super.onSubmit();
      logger.error("Not yet fully implemented on Submit on filter form");
      try {
        filterProjectsByName(nameFilter.getValue());
      } catch (SimalRepositoryException e) {
        logger.error("Unable to perform search", e);
        nameFilter.setModel(new Model<String>("ERROR: contact support"));
      }        
    }
  }
}

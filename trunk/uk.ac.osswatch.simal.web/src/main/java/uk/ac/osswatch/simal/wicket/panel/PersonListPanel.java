package uk.ac.osswatch.simal.wicket.panel;

/*
 * Copyright 2008,2010 University of Oxford
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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IInternetAddress;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.data.SortablePersonDataProvider;
import uk.ac.osswatch.simal.wicket.doap.PersonFilterInputModel;
import uk.ac.osswatch.simal.wicket.foaf.AddPersonPanel;
import uk.ac.osswatch.simal.wicket.markup.html.repeater.data.table.LinkPropertyColumn;
import uk.ac.osswatch.simal.wicket.panel.project.AbstractEditableResourcesPanel;

/**
 * A panel for listing people. This panel allows the user to navigate the people
 * the Simal repository and, optionally, allows some manipulation of those
 * records.
 */
public class PersonListPanel extends AbstractEditableResourcesPanel<IPerson> {
  private static final long serialVersionUID = 1L;
  private static final Logger logger = LoggerFactory
      .getLogger(PersonListPanel.class);
  private Set<IPerson> people;
  private String filter = "";
  private static PersonFilterInputModel inputModel = new PersonFilterInputModel();
  SortablePersonDataProvider<IPerson> dataProvider;

  /**
   * Create a panel that lists all people in the repository.
   * 
   * @param id
   *          the wicket ID for the component
   * @param title
   *          the title if this list
   * @param numberOfPeople
   *          the number of people to display per page
   * @throws SimalRepositoryException
   */
  public PersonListPanel(String id, String title, int numberOfPeople)
      throws SimalRepositoryException {
    super(id, title);
    this.people = SimalRepositoryFactory.getPersonService().getAll();
    populatePanel(numberOfPeople);
    add(new Label("addPersonPanel", ""));
  }

  /**
   * Create a panel that lists people filtered with a given filter string.
   * 
   * @param id
   *          the wicket ID for the component
   * @param title
   *          the title if this list
   * @param numberOfPeople
   *          the number of people to display per page
   * @param filter
   *          the filter to use
   * 
   * @throws SimalRepositoryException
   */
  public PersonListPanel(String id, String title, int numberOfPeople,
      String filter) throws SimalRepositoryException {
    super(id, title);
    this.people = SimalRepositoryFactory.getPersonService()
        .filterByName(filter);
    this.filter = filter;
    populatePanel(numberOfPeople);
    add(new Label("addPersonPanel", ""));
  }

  /**
   * Create a panel that lists a set of people.
   * 
   * @param id
   *          the wicket ID for the component
   * @param title
   *          the title if this list
   * @param people
   *          the people to include in the list
   * @param numberOfPeople
   *          the number of people to display per page
   * @throws SimalRepositoryException
   */
  public PersonListPanel(String id, String title, Set<IPerson> people,
      int numberOfPeople) {
    super(id, title);
    this.people = people;
    populatePanel(numberOfPeople);
    add(new Label("addPersonPanel", ""));
  }

  public PersonListPanel(String id, String title,
      Set<IPerson> people, int numberOfPeople, IProject project, int role,
      boolean isLoggedIn) {
    super(id, title, isLoggedIn);
    this.people = people;
    populatePanel(numberOfPeople);
    addAddDoapResourcePanel(new AddPersonPanel("addPersonPanel", project,
        role, this));
  }

  private void populatePanel(int numberOfPeople) {
    add(new PersonFilterForm("personFilterForm", this.filter));
    addPersonList(people, numberOfPeople);
  }

  @SuppressWarnings("rawtypes")
  private void addPersonList(Set<IPerson> people, int numberOfPeople) {
    List<AbstractColumn> columns = new ArrayList<AbstractColumn>();
    columns.add(new LinkPropertyColumn(new Model<String>("Name"), "label", "label"));

    columns.add(new PropertyColumn<IPerson>(new Model<String>("Email"), "email", "email") {
      private static final long serialVersionUID = 1L;

      @Override
      public void populateItem(Item<ICellPopulator<IPerson>> cellItem, String componentId, IModel<IPerson> model) {
        IPerson person = model.getObject();
        String label = "";
        
        if (person != null) {
          Iterator<IInternetAddress> emails = ((IPerson) model.getObject())
              .getEmail().iterator();
          if (emails.hasNext()) {
            IInternetAddress email = emails.next();
            label = email.getObfuscatedAddress();
            if (label.startsWith("mailto:")) {
              label = label.substring(7);
            }
          }
        }
        cellItem.add(new Label(componentId, label));
      }
    });

    columns.add(new ProjectsPropertyColumn(new Model<String>("Project"), "projects",
        "projects"));

    dataProvider = new SortablePersonDataProvider<IPerson>(people);
    dataProvider.setSort(SortablePersonDataProvider.SORT_PROPERTY_LABEL, true);
    add(new AjaxFallbackDefaultDataTable("dataTable", columns, dataProvider,
        numberOfPeople));
  }

  /**
   * Update the data displayed by filtering on the name field using a regular
   * expression.
   * 
   * @param nameFilter
   * @throws SimalRepositoryException
   */
  public void filterPeopleByName(String nameFilter)
      throws SimalRepositoryException {
    dataProvider.filterPeopleByName(nameFilter);
  }

  /**
   * Add a person to the list being displayed. This method does not add the
   * person to the underlying data storage mechanism, it only adds it to the
   * GUI.
   * 
   * @param person
   */
  public void addToDisplayList(IPerson person) {
    this.people.add(person);
  }

  /**
   *
   */
  private final class ProjectsPropertyColumn extends PropertyColumn<IPerson> {

    private static final long serialVersionUID = 6506230050408318191L;

    /**
     * @param displayModel
     * @param sortProperty
     * @param propertyExpression
     */
    private ProjectsPropertyColumn(IModel<String> displayModel, String sortProperty,
        String propertyExpression) {
      super(displayModel, sortProperty, propertyExpression);
    }

    @Override
    public void populateItem(Item<ICellPopulator<IPerson>> cellItem, String componentId, IModel<IPerson> model) {
      IPerson person = model.getObject();
      Iterator<IProject> projects;
      StringBuffer label = new StringBuffer();
      
      if(person != null) {
        try {
          projects = person.getProjects().iterator();
          while (projects.hasNext()) {
            IProject project = projects.next();
            label.append(project.getLabel());
            if (projects.hasNext()) {
              label.append(", ");
            }
          }
        } catch (SimalRepositoryException e) {
          logger.warn("Unable to retrieve projects for person : " + e.getMessage(), e);
        }
      }

      cellItem.add(new Label(componentId, label.toString()));
    }
  }

  /**
   * Form for filtering the people returned to a component.
   * 
   */
  private class PersonFilterForm extends Form<PersonFilterInputModel> {
    private static final long serialVersionUID = 4350446873545711199L;
    private TextField<String> nameFilter;

    public PersonFilterForm(String name, String filter) {
      super(name, new CompoundPropertyModel<PersonFilterInputModel>(inputModel));
      nameFilter = new TextField<String>("nameFilter");
      add(nameFilter);
      String[] defaultValue = { filter };
      nameFilter.setModelValue(defaultValue);
    }

    @Override
    protected void onSubmit() {
      super.onSubmit();
      try {
        filterPeopleByName(nameFilter.getValue());
      } catch (SimalRepositoryException e) {
        logger.error("Unable to perform search", e);
        nameFilter.setModel(new Model<String>("ERROR: contact support"));
      }
    }
  }

  @Override
  public void addToModel(IPerson doapResource) throws SimalException {
    // TODO Auto-generated method stub
    
  }
}

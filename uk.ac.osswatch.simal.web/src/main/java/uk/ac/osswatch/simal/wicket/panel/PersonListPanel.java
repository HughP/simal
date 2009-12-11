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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
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
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.data.SortablePersonDataProvider;
import uk.ac.osswatch.simal.wicket.doap.PersonFilterInputModel;
import uk.ac.osswatch.simal.wicket.foaf.PersonDetailPage;
import uk.ac.osswatch.simal.wicket.markup.html.repeater.data.table.LinkPropertyColumn;

/**
 * A panel for listing people. This panel allows the user to navigate the people
 * the Simal repository and, optionally, allows some manipulation of those
 * records.
 */
public class PersonListPanel extends Panel {
  private static final long serialVersionUID = 1L;
  private static final Logger logger = LoggerFactory.getLogger(PersonListPanel.class);
  private Set<IPerson> people;
  private String title;
  private String filter = "";
  private static PersonFilterInputModel inputModel = new PersonFilterInputModel();
  SortablePersonDataProvider dataProvider;

  /**
   * Create a panel that lists all people in the repository.
   * 
   * @param id
   *          the wicket ID for the component
   * @param title
   *          the title if this list
   * @param numberOfPeople the number of people to display per page
   * @throws SimalRepositoryException
   */
  public PersonListPanel(String id, String title, int numberOfPeople)
      throws SimalRepositoryException {
    super(id);
    this.title = title;
    this.people = SimalRepositoryFactory.getPersonService().getAll();
    populatePanel(numberOfPeople);
  }  
  
  /**
   * Create a panel that lists people filtered with a given filter string.
   * 
   * @param id
   *          the wicket ID for the component
   * @param title
   *          the title if this list
   * @param numberOfPeople the number of people to display per page
   * @param filter the filter to use 
   * 
   * @throws SimalRepositoryException
   */
  public PersonListPanel(String id, String title, int numberOfPeople, String filter)
      throws SimalRepositoryException {
    super(id);
    this.title = title;
    this.people = SimalRepositoryFactory.getPersonService().filterByName(filter);
    this.filter = filter;
    populatePanel(numberOfPeople);
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
   * @param numberOfPeople the number of people to display per page
   * @throws SimalRepositoryException
   */
  public PersonListPanel(String id, String title, Set<IPerson> people, int numberOfPeople) {
    super(id);
    this.title = title;
    this.people = people;
    populatePanel(numberOfPeople);
  }

  private void populatePanel(int numberOfPeople) {
    add(new Label("title", title));
    add(new PersonFilterForm("personFilterForm", this.filter));    
    addPersonList(people, numberOfPeople);   
  }

  @SuppressWarnings("unchecked")
private void addPersonList(Set<IPerson> people, int numberOfPeople) {
    List<AbstractColumn> columns = new ArrayList<AbstractColumn>();
    columns.add(new LinkPropertyColumn(new Model("Name"), "label", "label") {
		private static final long serialVersionUID = 1L;

	@Override
      public void onClick(Item item, String componentId, IModel model) {
        IPerson person = (IPerson) model.getObject();
        getRequestCycle().setResponsePage(new PersonDetailPage(person));
      }

    });

    columns.add(new PropertyColumn(new Model("EMail"), "email", "email") {
		private static final long serialVersionUID = 1L;

		@Override
        public void populateItem(Item cellItem, String componentId, IModel model) {
			IPerson person = (IPerson)model.getObject();
			Set<IInternetAddress> emailSet = person.getEmail();
        	Iterator<IInternetAddress> emails = ((IPerson)model.getObject()).getEmail().iterator();
        	if (emails.hasNext()) {
	        	while (emails.hasNext()) {
	        		IInternetAddress email = emails.next();
	        		String label = email.getLabel();
	        		if (label.startsWith("mailto:")) {
	        			label = label.substring(7);
	        		}
	                ExternalLink link = new ExternalLink(componentId,
	                  email.getAddress(),
	                  label);
	
	                cellItem.add(link);
	                break;
	        	}
        	} else {
        		cellItem.add(new Label(componentId, ""));
        	}
		}
    }); 
    
    columns.add(new PropertyColumn(new Model("Project"), "projects", "projects") {
		private static final long serialVersionUID = 1L;

		@Override
        public void populateItem(Item cellItem, String componentId, IModel model) {
        	Iterator<IProject> projects;
			try {
				projects = ((IPerson)model.getObject()).getProjects().iterator();
	        	StringBuffer label = new StringBuffer();
				while (projects.hasNext()) {
	        		IProject project = projects.next();
	        		label.append(project.getLabel());
	        		if (projects.hasNext()) {
	        			label.append(", ");
	        		}
	        	}
	            cellItem.add(new Label(componentId, label.toString()));
			} catch (SimalRepositoryException e) {
				cellItem.add(new Label(componentId, "ERROR"));
			}
        }
    }); 
    
    dataProvider = new SortablePersonDataProvider(people);
    dataProvider.setSort(SortablePersonDataProvider.SORT_PROPERTY_LABEL, true);
    add(new AjaxFallbackDefaultDataTable("dataTable", columns, dataProvider, numberOfPeople));
  }

  /**
   * Update the data displayed by filtering on the name field using a
   * regular expression.
   * 
   * @param nameFilter
   * @throws SimalRepositoryException
   */
  public void filterPeopleByName(String nameFilter) throws SimalRepositoryException {
    dataProvider.filterPeopleByName(nameFilter);
  }

  /**
   * Add a person to the list being displayed. This method does not add the
   * person to the underlying data storage mechanism, it only adds it to the
   * GUI.
   * 
   * @param person
   */
  public void addPerson(IPerson person) {
    this.people.add(person);
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
}

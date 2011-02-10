package uk.ac.osswatch.simal.wicket.data;

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

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IInternetAddress;
import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * A person data provider that allows the persons to be sorted.
 * 
 */
public class SortablePersonDataProvider<IPerson> extends
    SortableFoafResourceDataProvider<IPerson> {
  private static final long serialVersionUID = -2975592768329164790L;

  private static final Logger LOGGER = LoggerFactory
      .getLogger(SortableFoafResourceDataProvider.class);

  public static final String SORT_PROPERTY_LABEL = "label";
  public static final String SORT_PROPERTY_EMAIL = "email";

  /**
   * The set of FoafResources we are providing access to.
   */
  private Set<uk.ac.osswatch.simal.model.IPerson> persons;

  /**
   * Create a SortableDataProvider containing all people in the repository
   * 
   * @param size
   * @throws SimalRepositoryException
   */
  public SortablePersonDataProvider() throws SimalRepositoryException {
    this.persons = SimalRepositoryFactory.getPersonService().getAll();
  }

  /**
   * Create a SortableDataProvider containing all people supplied.
   * 
   * @param size
   * @throws SimalRepositoryException
   */
  public SortablePersonDataProvider(Set<uk.ac.osswatch.simal.model.IPerson> people) {
    this.persons = people;
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
    Set<uk.ac.osswatch.simal.model.IPerson> people = SimalRepositoryFactory.getPersonService()
        .filterByName(nameFilter);
    resetData(people);
  }

  public IModel<IPerson> model(IPerson object) {
    try {
      return new DetachablePersonModel<IPerson>(object);
    } catch (SimalRepositoryException e) {
      LOGGER.warn("Unable to read person object from repository", e);
      return new Model("Error");
    }
  }

  public int size() {
    return persons.size();
  }
  
  public Iterator<IPerson> iterator(int first, int count) {
    IPersonComparator comparator = new IPersonComparator();
    TreeSet<IPerson> treeSet = new TreeSet<IPerson>(comparator);
    treeSet.addAll((Collection<? extends IPerson>) persons);
    TreeSet<IPerson> result = new TreeSet<IPerson>(comparator);
    int idx = 0;
    Iterator<IPerson> all = treeSet.iterator();
    IPerson current;
    while (all.hasNext() && idx - (first + count) < 0) {
      current = all.next();
      if (idx >= first && current != null) {
        result.add(current);
      }
      idx++;
    }
    return result.iterator();
  }

  /**
   * Get an iterator over all resources. The resources will be sorted
   * accordingly.
   * 
   * @return
   */
  public Iterator<IPerson> iterator() {
    return iterator(0, persons.size());
  }

  /**
   * Update the data in this provider to only include those supplied.
   * 
   * @param persons
   */
  public void resetData(Set<uk.ac.osswatch.simal.model.IPerson> persons) {
    this.persons = persons;
  }

  /**
   * Ensures a sort property is valid, that is IDoapResourceBehaviours can be
   * sorted by it.
   * 
   * @param property
   * @return
   */
  protected boolean validateSortProperty(String property) {
    return property.equals(SORT_PROPERTY_LABEL);
  }

  private class IPersonComparator implements
      Comparator<IPerson>, Serializable {
    private static final long serialVersionUID = -8067880844650642351L;

    public int compare(IPerson resource1, IPerson resource2) {
      if (resource1.equals(resource2)) {
        return 0;
      }

      int result = 0;

      String sortField;
      if (getSort() == null) {
        sortField = SORT_PROPERTY_LABEL;
      } else {
        sortField = getSort().getProperty();
      }
      if (sortField.equals(SORT_PROPERTY_LABEL)) {
        String name1 = (String) ((IResource) resource1).getLabel();
        String name2 = (String) ((IResource) resource2).getLabel();
        result = name1.compareTo(name2);
      } else if (sortField.equals(SORT_PROPERTY_EMAIL)) {
        String email1;
        String email2;
        Set<IInternetAddress> internetAdd = ((uk.ac.osswatch.simal.model.IPerson) resource1).getEmail();
        email1 = ((IInternetAddress) internetAdd.toArray()[0]).getAddress();
        internetAdd = ((uk.ac.osswatch.simal.model.IPerson) resource2).getEmail();
        email2 = ((IInternetAddress) internetAdd.toArray()[0]).getAddress();
        if (email1 == null) {
          result = 1;
        } else if (email2 == null) {
          result = -1;
        } else {
          result = email1.compareTo(email2);
        }
      }
      if (result == 0) {
        result = 1;
      }
      return result;
    }
  }

}

package uk.ac.osswatch.simal.wicket.foaf;

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

import org.apache.wicket.IClusterable;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;

/**
 * An input model for managing a FOAF object.
 * 
 */
public class FoafFormInputModel implements IClusterable {
  private static final long serialVersionUID = -9089647575258232806L;
  // private static final Logger logger =
  // LoggerFactory.getLogger(FoafFormInputModel.class);
  private String name;
  private String email;

  public FoafFormInputModel() {
  }

  /**
   * Get the name for this person.
   * 
   * @return
   */
  public String getName() {
    return name;
  }

  /**
   * Set the name for this person.
   * 
   * @return
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get the person defined by this form.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public IPerson getPerson() throws SimalRepositoryException {
    IPerson duplicate = SimalRepositoryFactory.getPersonService().getDuplicate(getEmail());
    if (duplicate != null) {
      populatePerson(duplicate);
      return duplicate;
    }

    String id = SimalRepositoryFactory.getPersonService().getNewID();
    String uri = RDFUtils.getDefaultPersonURI(id);
    IPerson person;
    try {
      person = SimalRepositoryFactory.getPersonService().create(uri);
      person.setSimalID(id);
      populatePerson(person);
    } catch (DuplicateURIException e) {
      throw new SimalRepositoryException("Unable to create a new person", e);
    }
    return person;
  }

  /**
   * Populate the person by adding all the data in this form to it.
   * 
   * @param duplicate
   */
  private void populatePerson(IPerson person) {
    person.addName(getName());
    person.addEmail(getEmail());
  }

  /**
   * Get the primary email for this person.
   * 
   * @return
   */
  public String getEmail() {
    return email;
  }

  /**
   * Set the primary email for this person.
   * 
   * @return
   */
  public void setEmail(String email) {
    this.email = email;
  }
}

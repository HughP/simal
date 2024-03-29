package uk.ac.osswatch.simal.rest;

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

import java.util.Set;

import org.junit.BeforeClass;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public abstract class AbstractAPITest {

  private static ISimalRepository repo;
  protected static String testDeveloperID;
  protected static String testDeveloperEMail;
  protected static String testProjectID;

  @BeforeClass
  public static void setUpRepo() throws SimalRepositoryException {
    repo = SimalRepositoryFactory.getInstance();
    if (!repo.isInitialised()) {
      repo.setIsTest(true);
      repo.initialise();
    }

    IPerson developer = SimalRepositoryFactory.getPersonService().findBySeeAlso("http://foo.org/~developer/#me");
    testDeveloperID = developer.getSimalID();
    testDeveloperEMail = developer.getEmail().toArray()[0].toString();
    Set<IProject> projects = developer.getProjects();
	testProjectID = ((IProject) projects.toArray()[0])
        .getSimalID();
  }

  public ISimalRepository getRepo() {
    return repo;
  }
}

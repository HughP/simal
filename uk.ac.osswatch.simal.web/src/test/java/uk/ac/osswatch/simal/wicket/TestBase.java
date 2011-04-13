package uk.ac.osswatch.simal.wicket;

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

import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public abstract class TestBase {
  public static final String TEST_PROJECT_SEEALSO = "http://simal.oss-watch.ac.uk/simalTest#";
  protected static final int NUMBER_OF_TEST_CATEGORIES = 55;
  protected static final int NUMBER_OF_TEST_PROJECTS = 9;
  protected static final int NUMBER_OF_TEST_PEOPLE = 22;
  protected static String projectURI;
  protected static String developerURI;

  protected SimalTester tester;

  private static final Logger logger = LoggerFactory.getLogger(TestBase.class);

  @BeforeClass
  public static void setUpBeforeClass() throws SimalRepositoryException {
    logger.error(" *************  QQQQQQQQQQQQQQ ********");
    UserApplication.setIsTest(true);
    UserApplication.initRepository(); // this initialises the repository and adds test data

    IProject project = SimalRepositoryFactory.getProjectService().findProjectBySeeAlso(TEST_PROJECT_SEEALSO);
    projectURI = project.getURI();

    IPerson developer = SimalRepositoryFactory.getPersonService().findBySeeAlso("http://foo.org/~developer/#me");
    developerURI = developer.getURI();

    logProjectData("before");
  }

  @Before
  public void setUp() throws SimalRepositoryException {
    tester = SimalTester.get();
  }

  public static void logProjectData(String beforeOrAfter)
      throws SimalRepositoryException {
    Set<IProject> projects = UserApplication.getRepository().getAllProjects();
    logger.debug("Number of projects " + beforeOrAfter + " test: "
        + projects.size());

    logger.debug("Project: ");
    Iterator<IProject> itr = projects.iterator();
    while (itr.hasNext()) {
      logger.debug(itr.next().toString());
    }
  }
}

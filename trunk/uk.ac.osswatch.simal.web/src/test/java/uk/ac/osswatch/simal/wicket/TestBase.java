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

import org.apache.wicket.util.tester.WicketTester;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public abstract class TestBase {
  public static final String TEST_PROJECT_SEEALSO = "http://simal.oss-watch.ac.uk/simalTest#";
  protected static final int NUMBER_OF_TEST_CATEGORIES = 55;
  protected static final int NUMBER_OF_TEST_PROJECTS = 10;
  protected static final int NUMBER_OF_TEST_PEOPLE = 22;
  protected static WicketTester tester;
  protected static String projectURI;
  protected static String developerURI;

  private static final Logger logger = LoggerFactory.getLogger(TestBase.class);

  @BeforeClass
  public static void setUpBeforeClass() throws SimalRepositoryException {
    UserApplication.setIsTest(true);
    ISimalRepository repository = UserApplication.getRepository();

    IProject project = SimalRepositoryFactory.getProjectService().findProjectBySeeAlso(TEST_PROJECT_SEEALSO);
    projectURI = project.getURI();

    IPerson developer = repository
        .findPersonBySeeAlso("http://foo.org/~developer/#me");
    developerURI = developer.getURI();

    tester = new WicketTester();
    logProjectData("before");
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

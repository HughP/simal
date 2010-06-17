package uk.ac.osswatch.simal.wicket.doap;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;

import org.apache.wicket.util.file.File;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;
import uk.ac.osswatch.simal.wicket.TestBase;
import uk.ac.osswatch.simal.wicket.UserApplication;
import uk.ac.osswatch.simal.wicket.UserHomePage;

public class TestDoapFormPage extends TestBase {
  private final static String INVALID_URL = "this is not a valid URL";
  private static final String DOAP_FORM_FILE = "doapFormFile.xml";
  private static final String TEST_NAME = "Form Project";
  private static final String TEST_SHORT_DESC = "A project added by filling in the DOAP form";
  private static final String TEST_DESCRIPTION = "The long description og a project added by filling in the DOAP form";
  private static final String TEST_RAW_RDF_URI = "simal:99999";
  private static final String TEST_RAW_RDF_PROJECT_NAME = "Load From RAW RDF Test";
  private static final String TEST_RAW_RDF = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
	  + "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\""
	  + " xmlns=\"http://usefulinc.com/ns/doap#\""
	  + " xmlns:foaf=\"http://xmlns.com/foaf/0.1/\""
	  + " xmlns:labs=\"http://labs.apache.org/doap-ext/1.0#\""
      + " xmlns:dc=\"http://purl.org/dc/elements/1.1/\""
      + " xmlns:projects=\"http://projects.apache.org/ns/asfext#\">"
	  + "<Project xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\""
	  + " xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\""
	  + " xmlns=\"http://usefulinc.com/ns/doap#\""
	  + " xmlns:foaf=\"http://xmlns.com/foaf/0.1/\""
	  + " xmlns:admin=\"http://webns.net/mvcb/\">"
	  + "<name>" + TEST_RAW_RDF_PROJECT_NAME + "</name>"
      + "<shortname>Issue_245_test</shortname>" 
      + "<shortdesc>Issue_245_test is an Open Source Web-based Learning Content Management System (LCMS/LMS) and social networking environment designed with accessibility and adaptability in mind.</shortdesc>"
      + "<description>Issue_245_test is an Open Source Web-based Learning Content Management System (LCMS/LMS) and social networking environment designed with accessibility and adaptability in mind. Administrators can install or update Issue_245_test in minutes, develop custom themes to give Issue_245_test a new look, and easily extend its functionality with feature modules. Educators can quickly assemble, package, and redistribute Web-based instructional content, easily import prepackaged content, and conduct their courses online. Students learn in an adaptive, social learning environment.</description>"
      + "<homepage rdf:resource=\"http://www.Issue_245_test.ca/\" />"
      + "<category rdf:resource=\"http://jisc.ac.uk/programme#38\" />"
      + "<category rdf:resource=\"http://simal.oss-watch.ac.uk/category/coursemanagement\" rdfs:label=\"Course Issue_245_test management\" />"
      + "<category rdf:resource=\"http://simal.oss-watch.ac.uk/category/learningenvironments\" rdfs:label=\"Learning Issue_245_test Environments\" />"
      + "<wiki rdf:resource=\"http://wiki.Issue_245_test.ca\" />" 
      + "<download-page rdf:resource=\"http://www.Issue_245_test.ca/Issue_245_test/download.php\" />" 
      + "<bug-database rdf:resource=\"http://www.Issue_245_test.ca/development/bugs/\" />"
      + "<programming-language>Issue_245_test</programming-language>"
      + "<license rdf:resource=\"http://usefulinc.com/doap/licenses/Issue_245_test\" />"
      + "<maintainer>  <foaf:Person>     <foaf:name>Greg Issue_245_testsvn</foaf:name>     <foaf:homepage rdf:resource=\"http://wiki.Issue_245_test.ca/display/~admin\"/>     <foaf:mbox_sha1sum>b673333bd79e34cf06334752fe9f792cbfe39736</foaf:mbox_sha1sum>  </foaf:Person> </maintainer> "
      + "<repository>    <SVNRepository>     <browse rdf:resource='http://Issue_245_testsvn.websvn.atrc.utoronto.ca/' />     <location rdf:resource='http://Issue_245_testsvn.atrc.utoronto.ca/repos/Issue_245_test/trunk' />   </SVNRepository> </repository>"
      + "</Project>"
      + "</rdf:RDF>";

  private static String formInputURI = RDFUtils.PROJECT_NAMESPACE_URI
      + TEST_NAME;

  /**
   * Delete the repository to ensure that subsequent tests have clean data.
   * 
   * @throws SimalRepositoryException
   */
  @AfterClass
  public static void deleteRepostiroy() throws SimalRepositoryException {
    UserApplication.destroyRepository();
  }

  @Before
  public void initTester() throws SimalRepositoryException {
    logProjectData("before");
    tester = new WicketTester();
    tester.startPage(DoapFormPage.class);
    tester.assertRenderedPage(DoapFormPage.class);
  }

  @After
  public void cleanUp() throws SimalRepositoryException {
    logProjectData("after");
    IProject project = SimalRepositoryFactory.getProjectService().getProject(
        "http://simal.oss-watch.ac.uk/loadFromFormTest#");
    if (project != null) {
      project.delete();
    }
    project = SimalRepositoryFactory.getProjectService().getProject(formInputURI);
    if (project != null) {
      project.delete();
    }
  }
  
  @Test
  /**
   * Ensure the Raw RDF form is being cleared between executions.
   * ISSUE 104
   */
  public void testRawRDFFormCleared() {
    FormTester formTester = tester.newFormTester("rawRDFForm");
    formTester.setValue("rawRDF", "This is not a valid entry");
    formTester.submit();

    tester = new WicketTester();
    tester.startPage(DoapFormPage.class);
    tester.assertRenderedPage(DoapFormPage.class);
    formTester = tester.newFormTester("rawRDFForm");
    String value = formTester.getTextComponentValue("rawRDF");
    assertEquals("Raw RDF field should have been cleared", "", value);
  }

  /**
   * Test adding a project by a both a valid and invalid URL.
   * 
   * @throws SimalRepositoryException
   */
  @Test
  public void testAddProjectByURL() throws SimalRepositoryException {
    tester.assertVisible("addByURLForm:sourceURL");

    FormTester formTester = tester.newFormTester("addByURLForm");
    formTester.setValue("sourceURL", INVALID_URL);
    formTester.submit();
    tester.assertRenderedPage(DoapFormPage.class);
    String[] errors = { "'" + INVALID_URL + "' is not a valid URL." };
    tester.assertErrorMessages(errors);

    initTester();
    URL doapURL = UserApplication.class.getClassLoader().getResource(
        DOAP_FORM_FILE);
    formTester = tester.newFormTester("addByURLForm");
    formTester.setValue("sourceURL", doapURL.toString());

    // FIXME: since adding the URLConverter the onSubmit method does not work. I
    // (RG) think that this is because the URLConverter is not being loaded.
    //int numProjectsBefore = UserApplication.getRepository().getAllProjects().size();
    formTester.submit();
    //int numProjectsAfter = UserApplication.getRepository().getAllProjects().size();

    // assertTrue("Loading data by URL does not appear to have added any
    // projects", numProjectsAfter > numProjectsBefore);
  }

  /**
   * Test that a file uploaded to the server is correctly loaded into the
   * repository.
   * 
   * @throws SimalRepositoryException
   * @throws URISyntaxException
   */
  @Test
  public void testAddByUpload() throws SimalRepositoryException,
      URISyntaxException {
    tester.assertVisible("uploadForm:fileInput");

    uploadFile();
    tester.assertRenderedPage(UserHomePage.class);
  }

  @Test
  public void testProjectForm() throws SimalRepositoryException {
    tester.assertVisible("doapForm");
    tester.assertVisible("doapForm:name");
    tester.assertVisible("doapForm:shortDesc");
    tester.assertVisible("doapForm:description");
  }

  @Test
  public void testProjectFormRequiredFields() throws SimalRepositoryException {
    FormTester formTester = tester.newFormTester("doapForm");

    formTester.setValue("name", "");
    formTester.setValue("shortDesc", "");
    formTester.submit();

    tester.assertRenderedPage(DoapFormPage.class);
    String[] errors = { "Field 'name' is required.",
        "Field 'shortDesc' is required." };
    tester.assertErrorMessages(errors);
  }

  @Test
  public void testAddProjectByRawRDF() throws SimalRepositoryException {
    FormTester formTester = tester.newFormTester("rawRDFForm");
    formTester.setValue("rawRDF", TEST_RAW_RDF);
    formTester.submit();

    tester.assertRenderedPage(UserHomePage.class);
    tester.assertNoErrorMessage();
    
    Set<IProject> projects = SimalRepositoryFactory.getProjectService().filterByName(TEST_RAW_RDF_PROJECT_NAME);
    assertEquals("Should only have one project returned with the raw RDF project name", 1, projects.size());
    
    IProject project = (IProject) projects.toArray()[0];

    project.delete();
    project = SimalRepositoryFactory.getProjectService().findProjectBySeeAlso(
        TEST_RAW_RDF_URI);
    assertNull(project);
  }

  @Test
  public void testAddProjectByForm() throws SimalRepositoryException {
    FormTester formTester = tester.newFormTester("doapForm");

    formTester = tester.newFormTester("doapForm");
    formTester.setValue("name", TEST_NAME);
    formTester.setValue("shortDesc", TEST_SHORT_DESC);
    formTester.setValue("description", TEST_DESCRIPTION);
    formTester.submit();

    tester.assertRenderedPage(UserHomePage.class);
    tester.assertNoErrorMessage();

    IProject project = SimalRepositoryFactory.getProjectService().getProject(formInputURI);
    assertNotNull(project);
    assertEquals("Name is not correct", TEST_NAME, project.getName());
    assertEquals("Short descritpion is not correct", TEST_SHORT_DESC, project
        .getShortDesc());
    assertEquals("Description is not correct", TEST_DESCRIPTION, project
        .getDescription());
  }

  private void uploadFile() throws SimalRepositoryException, URISyntaxException {
    FormTester formTester = tester.newFormTester("uploadForm");
    URL doapURL = UserApplication.class.getClassLoader().getResource(
        DOAP_FORM_FILE);
    File doapFile = new File(doapURL.toURI());
    formTester.setFile("fileInput", doapFile, "text/xml");
    formTester.submit();
  }
}

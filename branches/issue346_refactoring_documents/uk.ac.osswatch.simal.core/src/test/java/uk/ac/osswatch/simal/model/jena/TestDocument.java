package uk.ac.osswatch.simal.model.jena;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.ModelSupport;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestDocument extends AbstractJenaModelTest {

  private static final String TEST_DOCUMENT_FILE = "testDocument.xml";

  private static final String TEST_DOCUMENT_PROJECT_URI = "http://simal.oss-watch.ac.uk/testDocument";

  @BeforeClass
  public static void initDocumentTests() {
    try {
      repository.addProject(TestDocument.class.getClassLoader()
          .getResource(TEST_DOCUMENT_FILE), ModelSupport.TEST_FILE_BASE_URL);
    } catch (SimalRepositoryException e) {
      fail("Could not load test file.");
    }
  }
  
  @AfterClass
  public static void removeTestProject() throws SimalRepositoryException {
    IProject project = getTestProject();
    project.delete();
  }

  @Test
  public void testDocumentGeneration() {
    try {
      String project = getTestProject().toXML();
      
      testExpectedOldHomepage(project);
      testExpectedHomepage(project);

    } catch (SimalRepositoryException e) {
      fail("Could not retreive test project's XML.");
    }
  }

  /**
   * Test if the doap:old-homepage property resulted in the creation
   * of a foaf:Document that wasn't there in the original XML.
   * @param project
   */
  private void testExpectedOldHomepage(String project) {
    String expectedTag = "<old-homepage";
    String expectedHomepage = "<foaf:Document rdf:about=\"http://www.old-testdocument.com\">";
    String label = "<rdfs:label>Webpage</rdfs:label>";
    String expectedEndTag = "</old-homepage>";

    assertTrue("No old-homepage start tag in project XML.",
        project.indexOf(expectedTag) > 0);
    assertTrue("No foaf:Document for the old-homepage in project XML.",
        project.indexOf(expectedHomepage) > 0);
    assertTrue("No homepage label in project XML.",
        project.indexOf(label) > 0);
    assertTrue("No old-homepage end tag in project XML.",
        project.indexOf(expectedEndTag) > 0);
  }

  private void testExpectedHomepage(String project) {
    String expectedTag = "<homepage>";
    String expectedHomepage = "<foaf:Document rdf:about=\"http://www.testdocument.com\">";
    String label = "<rdfs:label>Webpage</rdfs:label>";
    String expectedEndTag = "</homepage>";

    assertTrue("No homepage start tag in project XML.",
        project.indexOf(expectedTag) > 0);
    assertTrue("No homepage foaf:Document in project XML.",
        project.indexOf(expectedHomepage) > 0);
    assertTrue("No homepage label in project XML.",
        project.indexOf(label) > 0);
    assertTrue("No homepage end tag in project XML.",
        project.indexOf(expectedEndTag) > 0);
  }

  private static IProject getTestProject() throws SimalRepositoryException {
    return SimalRepositoryFactory.getProjectService().findProjectBySeeAlso(
        TEST_DOCUMENT_PROJECT_URI);
  }

}

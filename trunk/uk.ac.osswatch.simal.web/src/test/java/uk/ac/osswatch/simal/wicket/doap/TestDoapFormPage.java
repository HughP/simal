package uk.ac.osswatch.simal.wicket.doap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.namespace.QName;

import org.apache.wicket.util.file.File;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.TestBase;
import uk.ac.osswatch.simal.wicket.UserApplication;
import uk.ac.osswatch.simal.wicket.UserHomePage;

public class TestDoapFormPage extends TestBase {
	private final static String INVALID_URL = "this is not a valid URL";
	private static final String DOAP_FORM_FILE = "doapFormFile.xml";
	private static final String TEST_NAME = "Form Project";
	private static final String TEST_SHORT_DESC = "A project added by filling in the DOAP form";
	private static final String TEST_DESCRIPTION = "The long description og a project added by filling in the DOAP form";

	@Before
  public void initTester() throws SimalRepositoryException {
		tester = new WicketTester();
		tester.startPage(DoapFormPage.class);
		tester.assertRenderedPage(DoapFormPage.class);
	}
	
	/**
	 * Test adding a project by a both a valid and invalid URL.
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
    formTester.submit();
    
		tester.assertRenderedPage(UserHomePage.class);
		
		UserApplication.getRepository().remove(new QName("http://simal.oss-watch.ac.uk/loadFromFormTest#"));
	}	 
  
  /**
   * Test that a file uploaded to the server is correctly 
   * loaded into the repository.
   * 
   * @throws SimalRepositoryException
   * @throws URISyntaxException 
   */
  @Test
  public void testAddByUpload() throws SimalRepositoryException, URISyntaxException {
    tester.assertVisible("uploadForm:fileInput");
    
    uploadFile();
    tester.assertRenderedPage(UserHomePage.class);
    
    UserApplication.getRepository().remove(new QName("http://simal.oss-watch.ac.uk/loadFromFormTest#"));
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
    String[] errors = { "Field 'name' is required.", "Field 'shortDesc' is required." };
    tester.assertErrorMessages(errors);
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
    
    QName qname = new QName(SimalRepository.DEFAULT_PROJECT_NAMESPACE_URI + TEST_NAME);
    IProject project = UserApplication.getRepository().getProject(qname);
    assertNotNull(project);
    assertEquals("Name is not correct", TEST_NAME, project.getName());
    assertEquals("Short descritpion is not correct", TEST_SHORT_DESC, project.getShortDesc());
    assertEquals("Description is not correct", TEST_DESCRIPTION, project.getDescription());
    
    UserApplication.getRepository().remove(qname);
	}

  private void uploadFile() throws SimalRepositoryException, URISyntaxException {
    FormTester formTester = tester.newFormTester("uploadForm");   
    URL doapURL = UserApplication.class.getClassLoader().getResource(
        DOAP_FORM_FILE);
    File doapFile = new File(doapURL.toURI());
    formTester.setFile("fileInput", doapFile, "");
    formTester.submit();
  }
  
  @Test
  public void testUploadedFile() throws SimalRepositoryException, URISyntaxException {
    File file = new File(UserApplication.getRepository().getAnnotatedDoapFile(DOAP_FORM_FILE));
    file.delete();
    
    uploadFile();
    file = new File(UserApplication.getRepository().getAnnotatedDoapFile(DOAP_FORM_FILE));
    assertTrue("Local copy of DOAP file does not exist, expected " + file.getAbsolutePath(), file.exists());
    
    UserApplication.getRepository().remove(new QName("http://simal.oss-watch.ac.uk/loadFromFormTest#"));
  }
}



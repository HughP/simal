package uk.ac.osswatch.simal.wicket.doap;

import java.net.URL;

import javax.xml.namespace.QName;

import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

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

    private void initTester() {
		tester = new WicketTester();
		tester.startPage(DoapFormPage.class);
		tester.assertRenderedPage(DoapFormPage.class);
	}
	
	@Test
	public void testAddProjectByURL() throws SimalRepositoryException {
		tester.startPage(DoapFormPage.class);
		tester.assertRenderedPage(DoapFormPage.class);
		
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
	
	@Test
	public void testAddProjectByForm() throws SimalRepositoryException {
		initTester();

		tester.assertVisible("doapForm");
		tester.assertVisible("doapForm:name");
		tester.assertVisible("doapForm:shortDesc");
		
		FormTester formTester = tester.newFormTester("doapForm");
		formTester.setValue("name", "");
		formTester.setValue("shortDesc", "");
		formTester.submit();
		tester.assertRenderedPage(DoapFormPage.class);
		String[] errors = { "Field 'name' is required.", "Field 'shortDesc' is required." };
		tester.assertErrorMessages(errors);
		
		initTester();
		formTester = tester.newFormTester("doapForm");
		formTester.setValue("name", TEST_NAME);
		formTester.setValue("shortDesc", TEST_SHORT_DESC);
		formTester.submit();
		tester.assertRenderedPage(UserHomePage.class);
		tester.assertNoErrorMessage();

		UserApplication.getRepository().remove(new QName(SimalRepository.DEFAULT_NAMESPACE_URI + TEST_NAME));
	}
}



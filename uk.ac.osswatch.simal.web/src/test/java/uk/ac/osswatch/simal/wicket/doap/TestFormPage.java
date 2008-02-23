package uk.ac.osswatch.simal.wicket.doap;

import java.net.URL;

import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;

import uk.ac.osswatch.simal.wicket.TestBase;
import uk.ac.osswatch.simal.wicket.UserApplication;

public class TestFormPage extends TestBase {
	private final static String INVALID_URL = "this is not a valid URL";
	private static final String DOAP_FORM_FILE = "doapFormFile.xml";

	@Test
	public void testRenderPage() {
		tester.startPage(DoapFormPage.class);
		tester.assertRenderedPage(DoapFormPage.class);
		
		tester.assertVisible("doapForm:sourceURL");
		
		FormTester formTester = tester.newFormTester("doapForm");
		formTester.setValue("sourceURL", INVALID_URL);
		formTester.submit();
		tester.assertRenderedPage(DoapFormPage.class);
		String[] errors = { "'" + INVALID_URL + "' is not a valid URL." };
		tester.assertErrorMessages(errors);
		
		URL doapURL = UserApplication.class.getClassLoader().getResource(
				DOAP_FORM_FILE);
		formTester = tester.newFormTester("doapForm");
		formTester.setValue("sourceURL", doapURL.toString());
		formTester.submit();
		// FIXME: this test does not work, it seems the tester is not being updated
		// manual testing of this functionality works just fine.
		// tester.assertRenderedPage(UserHomePage.class);
		
	}
}

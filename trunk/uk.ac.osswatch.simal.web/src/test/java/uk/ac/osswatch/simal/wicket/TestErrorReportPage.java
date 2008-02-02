package uk.ac.osswatch.simal.wicket;

import junit.framework.TestCase;
import static junit.framework.Assert.*;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.util.tester.WicketTester;

/**
 * Simple test using the WicketTester
 */
public class TestErrorReportPage extends TestCase {
	private WicketTester tester;

	public void setUp() {
		tester = new WicketTester();
	}

	public void testRenderPage() {
		tester.startPage(ErrorReportPage.class);
		tester.assertRenderedPage(ErrorReportPage.class);
		tester.assertVisible("errorDetails");
		tester.assertVisible("footer");
		
		TextArea details = (TextArea) tester.getComponentFromLastRenderedPage("errorDetails");
		String strDetails = (String)details.getModel().getObject();
		assertTrue("Error report does not appear to contain the error message", strDetails.contains("Just testing"));
		assertTrue("Error report does not appear to contain the reporting class details", strDetails.contains("ErrorReportPage"));
		assertTrue("Error report does not appear to contain the cause report", strDetails.contains("IllegalArgumentException"));
		assertTrue("Error report does not appear to contain the stacktrace", strDetails.contains("at uk.ac.osswatch.simal.wicket.ErrorReportPage"));
	}
}

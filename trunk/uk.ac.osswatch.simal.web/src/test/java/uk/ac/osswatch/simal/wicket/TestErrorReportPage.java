package uk.ac.osswatch.simal.wicket;

import org.apache.wicket.markup.html.basic.Label;

/**
 * Simple test using the WicketTester
 */
public class TestErrorReportPage extends TestBase {

	public void testRenderPage() {
		tester.startPage(ErrorReportPage.class);
		tester.assertRenderedPage(ErrorReportPage.class);
		tester.assertVisible("errorDetails");
		tester.assertVisible("footer");
		
		Label details = (Label) tester.getComponentFromLastRenderedPage("errorDetails");
		String strDetails = (String)details.getModel().getObject();
		assertTrue("Error report does not appear to contain the error message", strDetails.contains("Just testing"));
		assertTrue("Error report does not appear to contain the reporting class details", strDetails.contains("ErrorReportPage"));
		assertTrue("Error report does not appear to contain the cause report", strDetails.contains("IllegalArgumentException"));
		assertTrue("Error report does not appear to contain the stacktrace", strDetails.contains("at uk.ac.osswatch.simal.wicket.ErrorReportPage"));
	}
}

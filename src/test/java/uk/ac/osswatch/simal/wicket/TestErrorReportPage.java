package uk.ac.osswatch.simal.wicket;

import static org.junit.Assert.assertTrue;

import org.apache.wicket.markup.html.basic.Label;
import org.junit.Test;

/**
 * Simple test using the WicketTester
 */
public class TestErrorReportPage extends TestBase {

	@Test
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

package uk.ac.osswatch.simal.wicket;

import junit.framework.TestCase;
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
	}
}

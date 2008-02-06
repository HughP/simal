package uk.ac.osswatch.simal.wicket;

import org.apache.wicket.util.tester.WicketTester;

/**
 * Simple test using the WicketTester
 */
public class TestExhibitProjectBrowserPage extends TestBase {

	public void testRenderPage() {
		tester.startPage(ExhibitProjectBrowserPage.class);
		tester.assertRenderedPage(ExhibitProjectBrowserPage.class);
		tester.assertVisible("footer");
		
	}
}

package uk.ac.osswatch.simal.wicket;

import org.junit.Test;

/**
 * Simple test using the WicketTester
 */
public class TestExhibitProjectBrowserPage extends TestBase {

	@Test
	public void testRenderPage() {
		tester.startPage(ExhibitProjectBrowserPage.class);
		tester.assertRenderedPage(ExhibitProjectBrowserPage.class);
		tester.assertVisible("footer");
		
	}
}

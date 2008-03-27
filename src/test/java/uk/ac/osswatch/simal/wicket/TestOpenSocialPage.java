package uk.ac.osswatch.simal.wicket;

import org.junit.Test;

/**
 * Simple test using the WicketTester
 */
public class TestOpenSocialPage extends TestBase {
	
	@Test
	public void testRenderPage() {
		tester.startPage(BasePage.class);
		tester.assertRenderedPage(BasePage.class);
		tester.assertVisible("footer");
		
		tester.clickLink("exhibitBrowserLink");
		tester.assertRenderedPage(ExhibitProjectBrowserPage.class);
	}
}

package uk.ac.osswatch.simal.wicket;



/**
 * Simple test using the WicketTester
 */
public class TestBasePage extends TestBase {
	public void testRenderPage() {
		tester.startPage(BasePage.class);
		tester.assertRenderedPage(BasePage.class);
		tester.assertVisible("footer");
		
		tester.clickLink("exhibitBrowserLink");
		tester.assertRenderedPage(ExhibitProjectBrowserPage.class);
	}
}

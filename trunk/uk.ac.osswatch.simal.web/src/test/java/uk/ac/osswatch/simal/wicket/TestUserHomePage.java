package uk.ac.osswatch.simal.wicket;

import org.apache.wicket.util.tester.WicketTester;

/**
 * Simple test using the WicketTester
 */
public class TestUserHomePage extends TestBase {

	public void testRenderPage() {
		tester.startPage(UserHomePage.class);
		tester.assertRenderedPage(UserHomePage.class);
		tester.assertVisible("message");
		tester.assertVisible("projectList");
		tester.assertVisible("featuredProject");
		tester.assertVisible("footer");
	}
}

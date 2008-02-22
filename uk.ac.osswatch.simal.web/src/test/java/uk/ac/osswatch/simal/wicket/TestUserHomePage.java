package uk.ac.osswatch.simal.wicket;

import org.junit.Test;

/**
 * Simple test using the WicketTester
 */
public class TestUserHomePage extends TestBase {

	@Test
	public void testRenderPage() {
		tester.startPage(UserHomePage.class);
		tester.assertRenderedPage(UserHomePage.class);
		tester.assertVisible("projectList");
		tester.assertVisible("featuredProject");
		tester.assertVisible("footer");
	}
}

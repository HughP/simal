package uk.ac.osswatch.simal.wicket;

import junit.framework.TestCase;
import org.apache.wicket.util.tester.WicketTester;

/**
 * Simple test using the WicketTester
 */
public class TestUserHomePage extends TestCase {
	private WicketTester tester;

	public void setUp() {
		tester = new WicketTester();
	}

	public void testRenderUserHomePage() {
		tester.startPage(UserHomePage.class);
		tester.assertRenderedPage(UserHomePage.class);
		tester.assertVisible("message");
	}
}

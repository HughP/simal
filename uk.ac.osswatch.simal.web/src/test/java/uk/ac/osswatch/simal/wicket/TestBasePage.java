package uk.ac.osswatch.simal.wicket;

import junit.framework.TestCase;
import org.apache.wicket.util.tester.WicketTester;

/**
 * Simple test using the WicketTester
 */
public class TestBasePage extends TestCase {
	private WicketTester tester;

	public void setUp() {
		tester = new WicketTester();
	}

	public void testRenderPage() {
		tester.startPage(BasePage.class);
		tester.assertRenderedPage(BasePage.class);
		tester.assertVisible("footer");
	}
}

package uk.ac.osswatch.simal.wicket;

import junit.framework.TestCase;
import org.apache.wicket.util.tester.WicketTester;

/**
 * Simple test using the WicketTester
 */
public class TestExhibitProjectBrowserPage extends TestCase {
	private WicketTester tester;

	public void setUp() {
		tester = new WicketTester();
	}

	public void testRenderPage() {
		tester.startPage(ExhibitProjectBrowserPage.class);
		tester.assertRenderedPage(ExhibitProjectBrowserPage.class);
		tester.assertVisible("footer");
		
	}
}

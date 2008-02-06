package uk.ac.osswatch.simal.wicket;

import org.apache.wicket.util.tester.WicketTester;

/**
 * Simple test using the WicketTester
 */
public class TestProjectDetailPage extends TestBase {

	public void testRenderPage() {
		tester.startPage(ProjectDetailPage.class);
		tester.assertRenderedPage(ProjectDetailPage.class);
		tester.assertVisible("projectName");
		tester.assertVisible("shortDesc");
		tester.assertVisible("footer");
	}
}

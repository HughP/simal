package uk.ac.osswatch.simal.wicket;

/**
 * Simple test using the WicketTester
 */
public class TestProjectDetailPage extends TestBase {

	public void testRenderPage() {
		tester.startPage(ProjectDetailPage.class);
		tester.assertRenderedPage(ProjectDetailPage.class);
		
		tester.assertVisible("projectName");
		tester.assertVisible("shortDesc");
		tester.assertVisible("created");
		tester.assertVisible("description");
		
		tester.assertVisible("mailingLists");
		
		tester.assertVisible("maintainers");
		tester.assertVisible("maintainers:1:maintainer");
		tester.assertVisible("maintainers:2:maintainer");
		
		tester.assertVisible("developers");
		
		tester.assertVisible("categories");
		tester.assertVisible("categories:1:category");
		tester.assertLabel("categories:1:category:label", "DOAP Test");
		
		tester.assertVisible("footer");
	}
}

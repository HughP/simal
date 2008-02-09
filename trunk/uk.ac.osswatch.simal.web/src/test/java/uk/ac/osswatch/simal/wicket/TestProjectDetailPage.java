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
		tester.assertLabel("mailingLists:1:mailingList:label", "http://foo.org/mailingList1");
		tester.assertLabel("mailingLists:2:mailingList:label", "http://foo.org/mailingList2");
		
		tester.assertVisible("footer");
	}
}

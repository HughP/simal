package uk.ac.osswatch.simal.wicket.panel;

import junit.framework.TestCase;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.TestPanelSource;
import org.apache.wicket.util.tester.WicketTester;

import uk.ac.osswatch.simal.wicket.ProjectDetailPage;
import uk.ac.osswatch.simal.wicket.UserApplication;

/**
 * Simple test using the WicketTester
 */
public class TestProjectSummaryPanel extends TestCase {
	private WicketTester tester;

	public void setUp() {
		tester = new WicketTester();
	}

	@SuppressWarnings("serial")
	public void testRenderUserHomePage() {
		 tester.startPanel(new TestPanelSource() {
		        public Panel getTestPanel(String panelId)
		        {
		                return new ProjectSummaryPanel(panelId, UserApplication.getProject(UserApplication.DEFAULT_PROJECT_QNAME));
		        }
		 });
		tester.assertVisible("panel:projectName");
		tester.assertLabel("panel:projectName", "Simal DOAP Test");
		
		tester.clickLink("panel:projectDetailLink");
		tester.assertRenderedPage(ProjectDetailPage.class);
	}
}

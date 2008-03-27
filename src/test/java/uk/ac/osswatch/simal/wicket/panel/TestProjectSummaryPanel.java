package uk.ac.osswatch.simal.wicket.panel;

import static org.junit.Assert.fail;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Test;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.ProjectDetailPage;
import uk.ac.osswatch.simal.wicket.TestBase;
import uk.ac.osswatch.simal.wicket.UserApplication;

/**
 * Simple test using the WicketTester
 */
public class TestProjectSummaryPanel extends TestBase {

	@Test
	@SuppressWarnings("serial")
	public void testRenderUserHomePage() {
		 tester.startPanel(new TestPanelSource() {
		        public Panel getTestPanel(String panelId)
		        {
		                try {
							return new ProjectSummaryPanel(panelId, UserApplication.getRepository().getProject(UserApplication.DEFAULT_PROJECT_QNAME));
						} catch (SimalRepositoryException e) {
							fail(e.getMessage());
							return null;
						}
		        }
		 });
		tester.assertVisible("panel:projectName");
		tester.assertLabel("panel:projectName", "Simal DOAP Test");
		
		tester.clickLink("panel:projectDetailLink");
		tester.assertRenderedPage(ProjectDetailPage.class);
	}
}

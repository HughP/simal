package uk.ac.osswatch.simal.wicket.panel;

import static org.junit.Assert.fail;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Test;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.ProjectDetailPage;
import uk.ac.osswatch.simal.wicket.TestBase;

/**
 * Simple test using the WicketTester
 */
public class TestProjectListPanel extends TestBase {

	@Test
	@SuppressWarnings("serial")
	public void testProjectListPanel() {
		 tester.startPanel(new TestPanelSource()
		 {
		        public Panel getTestPanel(String panelId)
		        {
		                try {
							return new ProjectListPanel(panelId);
						} catch (SimalRepositoryException e) {
							fail(e.getMessage());
							return null;
						}
		        }
		 });
		 
		tester.assertVisible("panel:dataTable:rows:1");
		tester.assertVisible("panel:dataTable:rows:2");
		tester.assertVisible("panel:dataTable:rows:3");
		tester.assertLabel("panel:dataTable:rows:1:cells:1:cell:link:label", "CodeGoo");
		
		tester.clickLink("panel:dataTable:rows:1:cells:1:cell:link");
		tester.assertRenderedPage(ProjectDetailPage.class);
	}
}

package uk.ac.osswatch.simal.wicket.panel;

import static org.junit.Assert.fail;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Test;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.TestBase;
import uk.ac.osswatch.simal.wicket.UserApplication;

/**
 * Simple test using the WicketTester
 */
public class TestReleasesPanel extends TestBase {

	@Test
	@SuppressWarnings("serial")
	public void testRenderPanel() {
		 tester.startPanel(new TestPanelSource() {
		        public Panel getTestPanel(String panelId)
		        {
		                try {
							return new ReleasesPanel(panelId, UserApplication.getRepository().getProject(UserApplication.DEFAULT_PROJECT_QNAME).getReleases());
						} catch (SimalRepositoryException e) {
							fail(e.getMessage());
							return null;
						}
		        }
		 });
		tester.assertVisible("panel:releases");
		tester.assertVisible("panel:releases:1:name");
		// FIXME: we can't predict which release will be first
		//tester.assertLabel("panel:releases:1:name", "Simal Project Registry");
		
		tester.assertVisible("panel:releases:1:revisions");
    // FIXME: we can't predict which release will be first
    // tester.assertLabel("panel:releases:1:revisions:1:revision", "0.1");
		
		tester.assertVisible("panel:releases:1:created");
    // FIXME: we can't predict which release will be first
    //tester.assertLabel("panel:releases:1:created", "2007-08-30");
	}
}

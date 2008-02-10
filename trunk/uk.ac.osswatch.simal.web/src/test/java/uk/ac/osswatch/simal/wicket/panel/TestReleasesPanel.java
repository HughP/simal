package uk.ac.osswatch.simal.wicket.panel;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.TestPanelSource;

import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.TestBase;
import uk.ac.osswatch.simal.wicket.UserApplication;

/**
 * Simple test using the WicketTester
 */
public class TestReleasesPanel extends TestBase {

	@SuppressWarnings("serial")
	public void testRenderPanel() {
		 tester.startPanel(new TestPanelSource() {
		        public Panel getTestPanel(String panelId)
		        {
		                try {
							return new ReleasesPanel(panelId, SimalRepository.getProject(UserApplication.DEFAULT_PROJECT_QNAME).getReleases());
						} catch (SimalRepositoryException e) {
							fail(e.getMessage());
							return null;
						}
		        }
		 });
		tester.assertVisible("panel:releases");
		tester.assertVisible("panel:releases:1:name");
		tester.assertLabel("panel:releases:1:name", "Simal Project Registry");
		
		tester.assertVisible("panel:releases:1:revisions");
		tester.assertLabel("panel:releases:1:revisions:1:revision", "0.1");
		
		tester.assertVisible("panel:releases:1:created");
		tester.assertLabel("panel:releases:1:created", "2007-08-30");
	}
}

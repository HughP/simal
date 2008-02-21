package uk.ac.osswatch.simal.wicket.panel;

import static org.junit.Assert.*;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.TestPanelSource;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.TestBase;
import uk.ac.osswatch.simal.wicket.UserApplication;

/**
 * Simple test using the WicketTester
 */
public class TestSourceRepositoriesPanel extends TestBase {

	@SuppressWarnings("serial")
	public void testRenderPanel() {
		 tester.startPanel(new TestPanelSource() {
		        public Panel getTestPanel(String panelId)
		        {
		                try {
							return new SourceRepositoriesPanel(panelId, UserApplication.getRepository().getProject(UserApplication.DEFAULT_PROJECT_QNAME).getRepositories());
						} catch (SimalRepositoryException e) {
							fail(e.getMessage());
							return null;
						}
		        }
		 });
		tester.assertVisible("panel:sourceRepositories");
		tester.assertVisible("panel:sourceRepositories:1:anonRoots");
		tester.assertVisible("panel:sourceRepositories:1:devLocations:1:devLink");
		tester.assertVisible("panel:sourceRepositories:1:browseRoots:1:browseLink");
	}
}

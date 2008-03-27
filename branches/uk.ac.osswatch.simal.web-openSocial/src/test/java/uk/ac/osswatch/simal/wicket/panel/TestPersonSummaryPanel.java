package uk.ac.osswatch.simal.wicket.panel;

import static org.junit.Assert.fail;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Test;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.TestBase;
import uk.ac.osswatch.simal.wicket.UserApplication;

/**
 * Simple test using the WicketTester
 */
public class TestPersonSummaryPanel extends TestBase {

	@Test
	@SuppressWarnings("serial")
	public void testRenderUserHomePage() {
		 tester.startPanel(new TestPanelSource() {
		        public Panel getTestPanel(String panelId)
		        {
		                try {
							return new PersonSummaryPanel(panelId, (IPerson)UserApplication.getRepository().getProject(UserApplication.DEFAULT_PROJECT_QNAME).getDevelopers().toArray()[0]);
						} catch (SimalRepositoryException e) {
							fail(e.getMessage());
							return null;
						}
		        }
		 });
		tester.assertVisible("panel:personName");
		tester.assertLabel("panel:personName", "developer");
		
		tester.assertVisible("panel:homepages");
	}
}

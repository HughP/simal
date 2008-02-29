package uk.ac.osswatch.simal.wicket.panel;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Test;

import uk.ac.osswatch.simal.wicket.TestBase;

/**
 * Simple test using the WicketTester
 */
public class TestGadgetPanel extends TestBase {

	@Test
	@SuppressWarnings("serial")
	public void testRenderPanel() {
		tester.startPanel(new TestPanelSource() {
			public Panel getTestPanel(String panelId) {
				return new GadgetPanel(panelId, "http://hosting.gmodules.com/ig/gadgets/file/102930279039750035480/myexperiment-tag-cloud.xml");
			}
		});
	}
}

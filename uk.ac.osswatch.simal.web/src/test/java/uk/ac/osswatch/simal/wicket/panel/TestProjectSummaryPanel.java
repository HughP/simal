package uk.ac.osswatch.simal.wicket.panel;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.TestPanelSource;
import org.apache.wicket.util.tester.WicketTester;

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
		 tester.startPanel(new TestPanelSource()
		 {
		        public Panel getTestPanel(String panelId)
		        {
		                QName qname = new QName("http://simal.oss-watch.ac.uk/simalTest#");
		                return new ProjectSummaryPanel(panelId, qname);
		        }
		 });
		tester.assertVisible("panel:projectName");
		tester.assertLabel("panel:projectName", "Simal DOAP Test");
	}
}

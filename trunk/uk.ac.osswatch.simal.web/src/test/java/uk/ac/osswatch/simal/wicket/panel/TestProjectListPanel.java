package uk.ac.osswatch.simal.wicket.panel;

import junit.framework.TestCase;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.TestPanelSource;
import org.apache.wicket.util.tester.WicketTester;

/**
 * Simple test using the WicketTester
 */
public class TestProjectListPanel extends TestCase {
	private WicketTester tester;

	public void setUp() {
		tester = new WicketTester();
	}

	@SuppressWarnings("serial")
	public void testProjectListPanel() {
		 tester.startPanel(new TestPanelSource()
		 {
		        public Panel getTestPanel(String panelId)
		        {
		                return new ProjectListPanel(panelId);
		        }
		 });
		 
		tester.assertVisible("panel:dataTable:rows:1");
		tester.assertVisible("panel:dataTable:rows:2");
		tester.assertVisible("panel:dataTable:rows:3");
		tester.assertLabel("panel:dataTable:rows:1:cells:1:cell", "CodeGoo");
	}
}

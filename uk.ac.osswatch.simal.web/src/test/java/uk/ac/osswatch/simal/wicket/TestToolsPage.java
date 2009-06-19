package uk.ac.osswatch.simal.wicket;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestToolsPage extends TestBasePage {

	  @Before
	  public void initTester() throws SimalRepositoryException {
	    tester = new WicketTester();
	    tester.startPage(ToolsPage.class);
	    tester.assertRenderedPage(ToolsPage.class);
	  }
	  
	  @Test
	  public void testNumberOfReviews() {
		    tester.assertVisible("numOfReviews");
		    tester.assertLabel("numOfReviews", "1");
	  }
}

package uk.ac.osswatch.simal.wicket.doap;

import org.junit.Test;

import uk.ac.osswatch.simal.wicket.TestBase;

/**
 * Simple test using the WicketTester
 */
public class TestCategoryBrowsePage extends TestBase {

	@Test
	public void testRenderPage() {
		tester.startPage(CategoryBrowserPage.class);
		tester.assertRenderedPage(CategoryBrowserPage.class);
		
		
	}
}

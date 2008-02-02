package uk.ac.osswatch.simal.wicket;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

/**
 * The base page for a standard simal web page. It contains the
 * standard markup for the site, such as headers and footers.
 * All pages in the Simal site should extend this page.
 */
public class BasePage extends WebPage {
	private static final long serialVersionUID = 7789964685286908825L;
	private static final CompressedResourceReference DEFAULT_CSS = new CompressedResourceReference(UserApplication.class, "default.css");
	
	public BasePage() {
		add(HeaderContributor.forCss( DEFAULT_CSS ));
		add(new BookmarkablePageLink("exhibitBrowserLink", ExhibitProjectBrowserPage.class));
		add(new Label("footer", "This is in the footer"));
	}
}


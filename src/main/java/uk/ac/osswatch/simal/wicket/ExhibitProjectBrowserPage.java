package uk.ac.osswatch.simal.wicket;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

/**
 * Creates a page which contains an <a
 * href="http://simile.mit.edu/wiki/Exhibit">Exhibit 2.0</a> Browser. This is a
 * faceted browser for Projects.
 */
public class ExhibitProjectBrowserPage extends BasePage {
	private static final long serialVersionUID = 2675836864409849552L;
	private static final CompressedResourceReference EXHIBIT_CSS = new CompressedResourceReference(
			UserApplication.class, "exhibit.css");

	public ExhibitProjectBrowserPage() {
		URL dir = UserApplication.class.getResource("default.css");
		try {
			File outFile = new File(new File(dir.toURI()).getParent() + File.separator + "projects.js");
			FileWriter out = new FileWriter(outFile);
			out.write(UserApplication.getRepository().getAllProjectsAsJSON());
			out.close();
		} catch (Exception e) {
			UserReportableException error = new UserReportableException("Unable to write JSON file", ExhibitProjectBrowserPage.class, e);
			setResponsePage(new ErrorReportPage(error));
		}
		add(HeaderContributor.forCss(EXHIBIT_CSS));
		add(HeaderContributor
				.forJavaScript("http://static.simile.mit.edu/exhibit/api-2.0/exhibit-api.js"));
		add(new StringHeaderContributor(
				"<link href=\"/resources/uk.ac.osswatch.simal.wicket.UserApplication/projects.js\" type=\"application/json\" rel=\"exhibit/data\" />"));
	}
}

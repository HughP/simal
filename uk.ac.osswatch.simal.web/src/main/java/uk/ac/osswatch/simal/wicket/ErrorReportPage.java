package uk.ac.osswatch.simal.wicket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.Model;

/**
 * An error report page is used to provide more information about an
 * error in the Simal system. It is designed to allow users to 
 * provide as much information as possible about the problem that
 * they encountered.
 */
public class ErrorReportPage extends WebPage {
	private static final long serialVersionUID = -8879369835743158631L;

	/**
	 * Creates a test ErrorReportPage, this constructor should not
	 * be used in production as there is no way to customise the
	 * error details.
	 */
	public ErrorReportPage() {
		this(new UserReportableException("Just testing the error reporting page", ErrorReportPage.class));
	}
	
	public ErrorReportPage(UserReportableException e) {
		add(new TextArea("errorDetails", new Model("Just Testing")));
	}
}


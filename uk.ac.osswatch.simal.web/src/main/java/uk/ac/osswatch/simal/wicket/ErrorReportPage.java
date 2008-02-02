package uk.ac.osswatch.simal.wicket;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.Model;

/**
 * An error report page is used to provide more information about an error in
 * the Simal system. It is designed to allow users to provide as much
 * information as possible about the problem that they encountered.
 */
public class ErrorReportPage extends BasePage {
	private static final long serialVersionUID = -8879369835743158631L;

	/**
	 * Creates a test ErrorReportPage, this constructor should not be used in
	 * production as there is no way to customise the error details.
	 */
	public ErrorReportPage() {
		this(new UserReportableException(
				"Just testing the error reporting page", ErrorReportPage.class,
				new IllegalArgumentException("An IllegalArgumentException for testing")));
	}

	public ErrorReportPage(UserReportableException e) {
		StringBuffer sb = new StringBuffer();
		sb.append(e.getMessage());
		if (e.getReportingClass() != null) {
			sb.append("\n\n");
			sb.append("Error reported by: ");
			sb.append(e.getReportingClass().toString());
		}
		if (e.getCause() != null) {
			sb.append("\n\n");
			sb.append("Caused by: ");
			sb.append(e.getCause());
		}
		if (e.getStackTrace() != null) {
			sb.append("\n\n");
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			sb.append(sw.getBuffer());
		}
		add(new TextArea("errorDetails", new Model(sb.toString())));
	}
}

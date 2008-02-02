package uk.ac.osswatch.simal.wicket;

public class UserReportableException extends Exception {
	private static final long serialVersionUID = -9166363556893990689L;
	private Class<?> reportingClass;

	public UserReportableException(String message, Class<?> reportingClass) {
		super(message);
		this.reportingClass = reportingClass;
	}
	
	public Class<?> getReportingClass() {
		return reportingClass;
	}
}

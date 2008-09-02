package uk.ac.osswatch.simal.wicket;
/*
 * Copyright 2008 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");   *
 * you may not use this file except in compliance with the License.  *
 * You may obtain a copy of the License at                           *
 *                                                                   *
 *   http://www.apache.org/licenses/LICENSE-2.0                      *
 *                                                                   *
 * Unless required by applicable law or agreed to in writing,        *
 * software distributed under the License is distributed on an       *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY            *
 * KIND, either express or implied.  See the License for the         *
 * specific language governing permissions and limitations           *
 * under the License.                                                *
 */


import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An error report page is used to provide more information about an error in
 * the Simal system. It is designed to allow users to provide as much
 * information as possible about the problem that they encountered.
 */
public class ErrorReportPage extends BasePage {
	private static final long serialVersionUID = -8879369835743158631L;
	private static final Logger logger = LoggerFactory.getLogger(ErrorReportPage.class);

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
	  logger.error("Reporting an error to the user", e);
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
		add(new Label("errorDetails", new Model(sb.toString())));
	}
}

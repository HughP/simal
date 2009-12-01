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

public class UserReportableException extends Exception {
  private static final long serialVersionUID = -9166363556893990689L;
  private Class<?> reportingClass;

  public UserReportableException(String message, Class<?> reportingClass) {
    super(message);
    this.reportingClass = reportingClass;
  }

  public UserReportableException(String message, Class<?> reportingClass,
      Throwable e) {
    super(message, e);
    this.reportingClass = reportingClass;
  }

  public Class<?> getReportingClass() {
    return reportingClass;
  }
}

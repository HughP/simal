package uk.ac.osswatch.simal.wicket;
/*
 * Copyright 2011 University of Oxford
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

import org.apache.wicket.Page;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.authentication.LoginPage;

public class SimalTester extends WicketTester {

  private static SimalTester instance;

  private SimalTester() throws SimalRepositoryException {
    super(new UserApplication());
  }

  /**
   * Get the tester for this run.
   * 
   * @return
   * @throws SimalRepositoryException 
   */
  public static SimalTester get() throws SimalRepositoryException {
    if (instance == null) {
      instance = new SimalTester();
    }
    return instance;
  }

  /**
   * If we are on a login page then enter login details and submit.
   * 
   * @throws SimalRepositoryException
   */
  public void login() throws SimalRepositoryException {
    Page page = getLastRenderedPage();
    if (page instanceof LoginPage) {
      FormTester formTester = newFormTester("loginForm");
      formTester.setValue("username", "simal");
      formTester.setValue("password", "simal");
      formTester.submit("login");
    }
  }

}
